package ispw.foodcare.dao;

import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.model.Appointment;
import ispw.foodcare.model.Session;
import ispw.foodcare.query.QueryAppointment;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                int generatedId = rs.getInt(1);
                logger.info("Appuntamento salvato con ID: " + generatedId);
            }

        } catch (SQLException e) {
            logger.severe("Errore salvataggio appuntamento su DB: " + e.getMessage());
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
}
