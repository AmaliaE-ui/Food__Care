package ispw.foodcare.dao;

import ispw.foodcare.AppointmentStatus;
import ispw.foodcare.bean.AppointmentBean;
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
    private final AvailabilityDAO availabilityDAO; // opzionale

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
                    logger.log(Level.INFO, () -> "Appuntamento salvato con ID: " + generatedId);
                }
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new RuntimeException("Impossibile prenotare: appuntamento già esistente per questa data e ora.", e);
            } else {
                logger.log(Level.SEVERE, "Errore salvataggio appuntamento su DB", e);
                throw new RuntimeException("Errore nel salvataggio dell'appuntamento", e);
            }
        }
    }

    public List<Appointment> getAppointmentsForPatient(String patientUsername) {
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

    public boolean isSlotAlreadyBooked(String nutritionistUsername, LocalDate date, LocalTime time) {
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

    public List<LocalTime> getBookedTimesForNutritionist(String nutritionistUsername, LocalDate date) {
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

    public void deleteAppointment(String nutritionistUsername, LocalDate date, LocalTime time) {
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

    public List<AppointmentBean> getAppointmentBeansForNutritionistWithPatientName(String nutritionistUsername) {
        // Nota: architetturalmente i DAO dovrebbero restituire Entity, non Bean.
        // Mantengo la firma per compatibilità.
        List<AppointmentBean> list = new ArrayList<>();

        try (Connection conn = cp.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_APPOINTMENTS_FOR_NUTRITIONIST_WITH_PATIENT_NAME)) {

            stmt.setString(1, nutritionistUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AppointmentBean bean = new AppointmentBean();
                    bean.setPatientUsername(rs.getString("patient_username"));
                    bean.setNutritionistUsername(nutritionistUsername);
                    bean.setDate(rs.getDate("date").toLocalDate());
                    bean.setTime(rs.getTime("time").toLocalTime());
                    bean.setStatus(AppointmentStatus.valueOf(rs.getString("status")));
                    bean.setNotes(rs.getString("notes"));
                    bean.setPatientName(rs.getString("nome"));
                    bean.setPatientSurname(rs.getString("cognome"));
                    list.add(bean);
                }
            }

        } catch (SQLException e) {
            logger.severe("Errore recupero appuntamenti con nome paziente per nutrizionista: " + e.getMessage());
        }

        return list;
    }

    public void restoreAvailabilityAfterCancellation(int appointmentId) {
        try (Connection conn = cp.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(QueryAppointment.SELECT_NUTRITIONIST_BEFORE_DELETE)) {

            selectStmt.setInt(1, appointmentId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    String nutritionistUsername = rs.getString("username_nutrizionista");
                    LocalDate date = rs.getDate("data").toLocalDate();
                    LocalTime oraInizio = rs.getTime("ora_inizio").toLocalTime();
                    LocalTime oraFine = rs.getTime("ora_fine").toLocalTime();


                    Availability availability = new Availability();
                    availability.setNutritionistUsername(nutritionistUsername);
                    availability.setDate(date);
                    availability.setStartTime(oraInizio);
                    availability.setEndTime(oraFine);

                    AvailabilityDAO availDao = (availabilityDAO != null) ? availabilityDAO : new AvailabilityDAO(cp);
                    availDao.addAvailability(availability);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore ripristino disponibilità", e);
        }
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
