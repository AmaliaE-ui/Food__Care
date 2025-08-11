package ispw.foodcare.dao;

import ispw.foodcare.AppointmentStatus;
import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.bean.AvailabilityBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.model.Appointment;
import ispw.foodcare.model.Availability;
import ispw.foodcare.model.Session;
import ispw.foodcare.query.QueryAppointment;
import ispw.foodcare.query.QueryAvailability;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentDAO {

    private static final Logger logger = Logger.getLogger(AppointmentDAO.class.getName());

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

    public List<LocalTime> getAvailableTimesForNutritionist(String nutritionistUsername, LocalDate date) {
        if (Session.getInstance().isRam()) {
            return getAvailableTimesRam(nutritionistUsername, date);
        } else if (Session.getInstance().isDB()) {
            return getAvailableTimesDB(nutritionistUsername, date);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    // --------------------------
    // RAM MODE
    // --------------------------
    //per mantenere id-appuntamento
    private final Map<Integer, Appointment> appointmentsRam = new HashMap<>();
    private int nextId = 1;

    private void saveAppointmentRam(Appointment appointment) {
        appointmentsRam.put(nextId++, appointment);
    }

    private List<LocalTime> getAvailableTimesRam(String nutritionistUsername, LocalDate date) {
        List<LocalTime> slots = BookAppointmentController.generateFixedSlots();

        for (Appointment a : appointmentsRam.values()) {
            if (a.getNutritionistUsername().equals(nutritionistUsername) && a.getDate().equals(date)) {
                slots.remove(a.getTime());
            }
        }
        return slots;
    }

    // --------------------------
    // DB MODE
    // --------------------------
    private void saveAppointmentDB(Appointment appointment) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.INSERT_APPOINTMENT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, appointment.getPatientUsername());
            stmt.setString(2, appointment.getNutritionistUsername());
            stmt.setDate(3, Date.valueOf(appointment.getDate()));
            stmt.setTime(4, Time.valueOf(appointment.getTime()));
            stmt.setString(5, appointment.getStatus().toString());
            stmt.setString(6, appointment.getNotes());
            stmt.setBoolean(7, false);

            stmt.executeUpdate();

            /*Rimuovo disponibilità dopo inserimento avvebuto con successo*/
            AvailabilityDAO availabilityDAO = Session.getInstance().getAvailabilityDAO();
            Availability toRemove = new Availability();
            toRemove.setNutritionistUsername(appointment.getNutritionistUsername());
            toRemove.setData(appointment.getDate());
            toRemove.setStartTime(appointment.getTime());
            toRemove.setEndTime(appointment.getTime().plusMinutes(45));

            availabilityDAO.deleteAvailability(toRemove);


            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                int generatedId = rs.getInt(1);
                logger.info("Appuntamento salvato con ID: " + generatedId);
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new RuntimeException("Impossibile prenotare: appuntamento già esistente per questa data e ora.");
            } else {
                logger.severe("Errore salvataggio appuntamento su DB: " + e.getMessage());
                throw new RuntimeException("Errore nel salvataggio dell'appuntamento: " + e.getMessage());
            }
        }
    }

    private List<LocalTime> getAvailableTimesDB(String nutritionistUsername, LocalDate date) {
        List<LocalTime> slots = BookAppointmentController.generateFixedSlots();

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_TIMES_FOR_NUTRITIONIST)) {
            stmt.setString(1, nutritionistUsername);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalTime time = rs.getTime("orario").toLocalTime();
                slots.remove(time);
            }

        } catch (SQLException e) {
            logger.severe("Errore nel recupero orari disponibili DB: " + e.getMessage());
        }

        return slots;
    }

    public List<Appointment> getAppointmentsForPatient(String patientUsername) {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_APPOINTMENTS_FOR_PATIENT)) {

            stmt.setString(1, patientUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getString("patient_username"),
                        rs.getString("nutritionist_username"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("time").toLocalTime(),
                        rs.getString("notes")
                ));
            }

        } catch (SQLException e) {
            logger.severe("Errore recupero appuntamenti del paziente: " + e.getMessage());
        }
        return appointments;
    }

    public boolean isSlotAlreadyBooked(String nutritionistUsername, LocalDate date, LocalTime time) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_APPOINTMENTS_FOR_STATUS)) {
            stmt.setString(1, nutritionistUsername);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(time));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.severe("Errore durante la verifica disponibilità slot: " + e.getMessage());
        }
        return false;
    }

    public List<LocalTime> getBookedTimesForNutritionist(String nutritionistUsername, LocalDate date) {
        List<LocalTime> bookedTimes = new ArrayList<>();
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_NUTRITIONIST_FOR_STATUS)) {
            stmt.setString(1, nutritionistUsername);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookedTimes.add(rs.getTime("time").toLocalTime());
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero degli slot già prenotati: " + e.getMessage());
        }
        return bookedTimes;
    }

    public void deleteAppointment(String nutritionistUsername, LocalDate date, LocalTime time) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.DELETE_APPOINTMENT)) {
            stmt.setString(1, nutritionistUsername);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(time));
            stmt.executeUpdate();

        } catch (SQLException e) {
                logger.severe("Errore eliminazione appuntamento da DB: " + e.getMessage());
        }
    }

    public void deleteAppointment(int appointmentId) throws SQLException {
        try (
                Connection conn = DBManager.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(QueryAppointment.DELETE_APPOINTMENT_BY_ID)
        ) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        }
    }


    public List<Appointment> getAppointmentsForNutritionist(String nutritionistUsername) {
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_APPOINTMENTS_FOR_NUTRITIONIST_WITH_PATIENT_NAME)) {
            stmt.setString(1, nutritionistUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getString("patient_username"),
                        nutritionistUsername,
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("time").toLocalTime(),
                        rs.getString("notes")
                );
                appointment.setPatientName(rs.getString("nome")+" "+rs.getString("cognome"));
                appointment.setStatus(AppointmentStatus.valueOf(rs.getString("status")));
                list.add(appointment);
            }
        } catch (SQLException e) {
            logger.severe("Errore recupero appuntamenti per nutrizionista: " + e.getMessage());
        }
        return list;
    }

    public void markAppointmentsAsViewedForNutritionist(String nutritionistUsername) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.MARK_APPOINTMENTS_VIEWED_FOR_NUTRITIONIST)) {
            stmt.setString(1, nutritionistUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Errore aggiornamento is_new: " + e.getMessage());
        }
    }

    public boolean hasUnviewedAppointmentsForNutritionist(String nutritionistUsername) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.HAS_UNVIEWED_APPOINTMENTS_FOR_NUTRITIONIST)) {
            stmt.setString(1, nutritionistUsername);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public List<AppointmentBean> getAppointmentBeansForNutritionistWithPatientName(String nutritionistUsername) {
        List<AppointmentBean> list = new ArrayList<>();

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAppointment.SELECT_APPOINTMENTS_FOR_NUTRITIONIST_WITH_PATIENT_NAME)) {

            stmt.setString(1, nutritionistUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AppointmentBean bean = new AppointmentBean();

                bean.setPatientUsername(rs.getString("patient_username"));
                bean.setNutritionistUsername(nutritionistUsername);
                bean.setDate(rs.getDate("date").toLocalDate());
                bean.setTime(rs.getTime("time").toLocalTime());
                bean.setStatus(AppointmentStatus.valueOf(rs.getString("status")));/*Conversione stringa letta da DB in enum*/
                bean.setNotes(rs.getString("notes"));

                // Aggiungi nome e cognome del paziente
                bean.setPatientName(rs.getString("nome"));
                bean.setPatientSurname(rs.getString("cognome"));

                list.add(bean);
            }

        } catch (SQLException e) {
            logger.severe("Errore recupero appuntamenti con nome paziente per nutrizionista: " + e.getMessage());
        }

        return list;
    }

    public void restoreAvailabilityAfterCancellation(int appointmentId) throws SQLException {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(QueryAppointment.SELECT_NUTRITIONIST_BEFORE_DELETE)) {

            selectStmt.setInt(1, appointmentId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String nutritionistUsername = rs.getString("username_nutrizionista");
                LocalDate date = rs.getDate("data").toLocalDate();
                LocalTime ora_inizio = rs.getTime("ora_inizio").toLocalTime();
                LocalTime ora_fine = rs.getTime("ora_fine").toLocalTime();

                // Usa AvailabilityDAO per reinserire la disponibilità
                Availability availability = new Availability();
                availability.setNutritionistUsername(nutritionistUsername);
                availability.setData(date);
                availability.setStartTime(ora_inizio);
                availability.setEndTime(ora_fine);

                AvailabilityDAO availabilityDAO = new AvailabilityDAO();
                availabilityDAO.addAvailability(availability);
            }
        }catch (SQLException e){
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, "Errore ripristino disponibilità" + e.getMessage(), e);
        }
    }

}
