package ispw.foodcare.dao;

import ispw.foodcare.AppointmentStatus;
import ispw.foodcare.exception.AppointmentAlreadyExistsException;
import ispw.foodcare.exception.AppointmentPersistenceException;
import ispw.foodcare.model.Appointment;
import ispw.foodcare.model.Availability;
import ispw.foodcare.model.Session;
import ispw.foodcare.query.QueryAppointment;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentDAO {

    private static final Logger logger = Logger.getLogger(AppointmentDAO.class.getName());

    /*injection*/
    private final ConnectionProvider cp;
    private final AvailabilityDAO availabilityDAO;

    public AppointmentDAO(ConnectionProvider cp) {
        this(cp, null);
    }

    public AppointmentDAO(ConnectionProvider cp, AvailabilityDAO availabilityDAO) {
        this.cp = cp;
        this.availabilityDAO = availabilityDAO;
    }

    // --------------------------
    // PUBLIC API
    // --------------------------
    public void saveAppointment(Appointment appointment) {
        if (Session.getInstance().isRam()) {
            saveAppointmentRam(appointment);
        } else if (Session.getInstance().isDB()) {
            saveAppointmentDB(appointment);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    // --------------------------
    // RAM MODE
    // --------------------------
    private final java.util.Map<Integer, Appointment> appointmentsRam = new java.util.HashMap<>();
    private int nextId = 1;

    private void saveAppointmentRam(Appointment appointment) {
        appointmentsRam.put(nextId++, appointment);

        /*Rimuovo dalle disponibilità*/
        Availability toRemove = new Availability();
        toRemove.setNutritionistUsername(appointment.getNutritionistUsername());
        toRemove.setDate(appointment.getDate());
        toRemove.setStartTime(appointment.getTime());
        toRemove.setEndTime(appointment.getTime().plusMinutes(45));

        AvailabilityDAO availDAO = (availabilityDAO != null)
                ? availabilityDAO
                : Session.getInstance().getAvailabilityDAO();

        availDAO.deleteAvailability(toRemove);
    }

    /*Controllo se lo slot è già prenotato*/
    private boolean isSlotAlreadyBookedRam(String nutritionistUsername, LocalDate date, LocalTime time) {
        return appointmentsRam.values().stream().anyMatch(a ->
                a.getNutritionistUsername().equals(nutritionistUsername)
                        && a.getDate().equals(date)
                        && a.getTime().equals(time)
        );
    }

    /*Controllo gli slot già prenotato*/
    private List<LocalTime> getBookedTimesForNutritionistRam(String nutritionistUsername, LocalDate date) {
        List<LocalTime> out = new ArrayList<>();
        for (Appointment a : appointmentsRam.values()) {
            if (a.getNutritionistUsername().equals(nutritionistUsername) && a.getDate().equals(date)) {
                out.add(a.getTime());
            }
        }
        return out;
    }

    /*Prendo appuntamento per un paziente*/
    private List<Appointment> getAppointmentsForPatientRam(String patientUsername) {
        List<Appointment> out = new ArrayList<>();
        for (Appointment a : appointmentsRam.values()) {
            if (a.getPatientUsername().equals(patientUsername)) {
                out.add(a);
            }
        }
        return out;
    }

    /*Elimina appuntamento*/
    private void deleteAppointmentRam(String nutritionistUsername, LocalDate date, LocalTime time) {
        // rimuovi l'appuntamento corrispondente
        Integer keyToRemove = null;
        for (var entry : appointmentsRam.entrySet()) {
            Appointment a = entry.getValue();
            if (a.getNutritionistUsername().equals(nutritionistUsername)
                    && a.getDate().equals(date)
                    && a.getTime().equals(time)) {
                keyToRemove = entry.getKey();
                break;
            }
        }
        if (keyToRemove != null) {
            appointmentsRam.remove(keyToRemove);
        }
    }

    // --------------------------
    // DB MODE
    // --------------------------
    private void saveAppointmentDB(Appointment appointment) {
        try (Connection conn = cp.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.INSERT_APPOINTMENT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, appointment.getPatientUsername());
            stmt.setString(2, appointment.getNutritionistUsername());
            stmt.setDate(3, Date.valueOf(appointment.getDate()));
            stmt.setTime(4, Time.valueOf(appointment.getTime()));
            stmt.setString(5, appointment.getStatus().toString());
            stmt.setString(6, appointment.getNotes());
            stmt.setBoolean(7, false);

            stmt.executeUpdate();

            /*Rimuovo disponibilità dopo inserimento*/
            Availability toRemove = new Availability();
            toRemove.setNutritionistUsername(appointment.getNutritionistUsername());
            toRemove.setDate(appointment.getDate());
            toRemove.setStartTime(appointment.getTime());
            toRemove.setEndTime(appointment.getTime().plusMinutes(45));

            /*usa l'AvailabilityDAO iniettato, altrimenti crea un DAO con lo stesso cp*/
            AvailabilityDAO availDao = (availabilityDAO != null) ? availabilityDAO : new AvailabilityDAO(cp);
            availDao.deleteAvailability(toRemove);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    logger.info("Appuntamento salvato con ID: " + generatedId);
                }
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new AppointmentAlreadyExistsException("Impossibile prenotare: appuntamento già esistente per questa data e ora.", e);
            } else {
                logger.log(Level.SEVERE, "Errore salvataggio appuntamento su DB", e);
                throw new AppointmentPersistenceException("Errore nel salvataggio dell'appuntamento", e);
            }
        }
    }

    public List<Appointment> getAppointmentsForPatient(String patientUsername) {
        if(Session.getInstance().isRam()){
            return getAppointmentsForPatientRam(patientUsername);
        }else {
            List<Appointment> appointments = new ArrayList<>();
            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_APPOINTMENTS_FOR_PATIENT)) {

                stmt.setString(1, patientUsername);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        appointments.add(new Appointment(
                                rs.getString("patient_username"),
                                rs.getString("nutritionist_username"),
                                rs.getDate("date").toLocalDate(),
                                rs.getTime("time").toLocalTime(),
                                rs.getString("notes")
                        ));
                    }
                }

            } catch (SQLException e) {
                logger.severe("Errore recupero appuntamenti del paziente: " + e.getMessage());
            }
            return appointments;
        }
    }

    public boolean isSlotAlreadyBooked(String nutritionistUsername, LocalDate date, LocalTime time) {
        if(Session.getInstance().isRam()){
            return isSlotAlreadyBookedRam(nutritionistUsername, date, time);
        }else {
            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_APPOINTMENTS_FOR_STATUS)) {
                stmt.setString(1, nutritionistUsername);
                stmt.setDate(2, Date.valueOf(date));
                stmt.setTime(3, Time.valueOf(time));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                logger.severe("Errore durante la verifica disponibilità slot: " + e.getMessage());
            }
            return false;
        }
    }

    public List<LocalTime> getBookedTimesForNutritionist(String nutritionistUsername, LocalDate date) {
        if(Session.getInstance().isRam()){
            return getBookedTimesForNutritionistRam(nutritionistUsername, date);
        }
        else{
            List<LocalTime> bookedTimes = new ArrayList<>();
            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_NUTRITIONIST_FOR_STATUS)) {
                stmt.setString(1, nutritionistUsername);
                stmt.setDate(2, Date.valueOf(date));
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        bookedTimes.add(rs.getTime("time").toLocalTime());
                    }
                }
            } catch (SQLException e) {
                logger.severe("Errore durante il recupero degli slot già prenotati: " + e.getMessage());
            }
            return bookedTimes;
        }
    }

    public void deleteAppointment(String nutritionistUsername, LocalDate date, LocalTime time) {
        if(Session.getInstance().isRam()){
            deleteAppointmentRam(nutritionistUsername, date, time);
        }else {
            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(QueryAppointment.DELETE_APPOINTMENT)) {
                stmt.setString(1, nutritionistUsername);
                stmt.setDate(2, Date.valueOf(date));
                stmt.setTime(3, Time.valueOf(time));
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.severe("Errore eliminazione appuntamento da DB: " + e.getMessage());
            }
        }
    }

    public List<Appointment> getAppointmentForNutritionistWithUsername(String nutritionistUsername) {
        // Nota: architetturalmente i DAO dovrebbero restituire Entity, non Bean.
        if(Session.getInstance().isRam()){
            // Compongo i dati leggendo i pazienti da UserDAO
            var userDAO = Session.getInstance().getUserDAO();
            List<Appointment> list = new ArrayList<>();
            for (Appointment a : appointmentsRam.values()) {
                if (a.getNutritionistUsername().equals(nutritionistUsername)) {
                    var model = new Appointment();
                    model.setPatientUsername(a.getPatientUsername());
                    model.setNutritionistUsername(a.getNutritionistUsername());
                    model.setDate(a.getDate());
                    model.setTime(a.getTime());
                    model.setStatus(a.getStatus());
                    model.setNotes(a.getNotes());

                    var u = userDAO.getUserByUsername(a.getPatientUsername());
                    if (u != null) {
                        model.setPatientName(u.getName());
                        model.setPatientSurname(u.getSurname());
                    }
                    list.add(model);
                }
            }
            return list;
        }else {
            List<Appointment> list = new ArrayList<>();

            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_APPOINTMENTS_FOR_NUTRITIONIST_WITH_PATIENT_NAME)) {

                stmt.setString(1, nutritionistUsername);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Appointment a = new Appointment();
                        a.setPatientUsername(rs.getString("patient_username"));
                        a.setNutritionistUsername(nutritionistUsername);
                        a.setDate(rs.getDate("date").toLocalDate());
                        a.setTime(rs.getTime("time").toLocalTime());
                        a.setStatus(AppointmentStatus.valueOf(rs.getString("status")));
                        a.setNotes(rs.getString("notes"));
                        a.setPatientName(rs.getString("nome"));
                        a.setPatientSurname(rs.getString("cognome"));
                        list.add(a);
                    }
                }

            } catch (SQLException e) {
                logger.severe("Errore recupero appuntamenti con nome paziente per nutrizionista: " + e.getMessage());
            }

            return list;}
    }



    /*Metodi nuovi - Notifiche nuovi appuntamenti*/
    public boolean hasUnviewedAppointmentsForNutritionist(String nutritionistUsername) {
        if (Session.getInstance().isRam()) {
            // In RAM non gestiamo il flag "is_new": restituisco false.
            return false;
        } else if (Session.getInstance().isDB()) {
            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt =
                         conn.prepareStatement(QueryAppointment.HAS_UNVIEWED_APPOINTMENTS_FOR_NUTRITIONIST)) {
                stmt.setString(1, nutritionistUsername);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            } catch (SQLException e) {
                logger.severe("Errore nel controllo appuntamenti non letti: " + e.getMessage());
            }
            return false;
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    public void markAppointmentsAsViewedForNutritionist(String nutritionistUsername) {
        if (Session.getInstance().isRam()) {
            // No-op in RAM
        } else if (Session.getInstance().isDB()) {
            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt =
                         conn.prepareStatement(QueryAppointment.MARK_APPOINTMENTS_VIEWED_FOR_NUTRITIONIST)) {
                stmt.setString(1, nutritionistUsername);
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.severe("Errore aggiornamento is_new: " + e.getMessage());
            }
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

}
