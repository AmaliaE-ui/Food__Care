package ispw.foodcare.dao;

import ispw.foodcare.model.Availability;
import ispw.foodcare.model.Session;
import ispw.foodcare.query.QueryAvailability;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AvailabilityDAO {

    private static final Logger logger = Logger.getLogger(AvailabilityDAO.class.getName());

    // --------------------------
    // PUBLIC API
    // --------------------------
    public void addAvailability(Availability availability) {
        if (Session.getInstance().isRam()) {
            addAvailabilityRam(availability);
        } else if (Session.getInstance().isDB()) {
            addAvailabilityDB(availability);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    public List<Availability> getAvailabilitiesForNutritionist(String nutritionistUsername) {
        if (Session.getInstance().isRam()) {
            return getAvailabilitiesRam(nutritionistUsername);
        } else if (Session.getInstance().isDB()) {
            return getAvailabilitiesDB(nutritionistUsername);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    public void deleteAvailability(Availability availability) {
        if (Session.getInstance().isRam()) {
            deleteAvailabilityRam(availability);
        } else if (Session.getInstance().isDB()) {
            deleteAvailabilityDB(availability);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    // --------------------------
    // RAM MODE
    // --------------------------
    private final List<Availability> availabilitiesRam = new ArrayList<>();

    private void addAvailabilityRam(Availability availability) {
        availabilitiesRam.add(availability);
    }

    private List<Availability> getAvailabilitiesRam(String nutritionistUsername) {
        List<Availability> result = new ArrayList<>();
        for (Availability a : availabilitiesRam) {
            if (a.getNutritionistUsername().equals(nutritionistUsername)) {
                result.add(a);
            }
        }
        return result;
    }

    private void deleteAvailabilityRam(Availability availability) {
        availabilitiesRam.remove(availability);
    }

    // --------------------------
    // DB MODE
    // --------------------------
    private void addAvailabilityDB(Availability availability) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAvailability.INSERT_AVAILABILITY)) {
            stmt.setString(1, availability.getNutritionistUsername());
            stmt.setDate(2, Date.valueOf(availability.getDate()));
            stmt.setTime(3, Time.valueOf(availability.getStartTime()));
            stmt.setTime(4, Time.valueOf(availability.getEndTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Errore salvataggio disponibilità su DB: " + e.getMessage());
        }
    }

    private List<Availability> getAvailabilitiesDB(String nutritionistUsername) {
        List<Availability> list = new ArrayList<>();
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAvailability.SELECT_AVAILABILITIES_FOR_NUTRITIONIST)) {
            stmt.setString(1, nutritionistUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Availability availability = new Availability();
                availability.setData(rs.getDate("data").toLocalDate());
                availability.setStartTime(rs.getTime("ora_inizio").toLocalTime());
                availability.setEndTime(rs.getTime("ora_fine").toLocalTime());
                availability.setNutritionistUsername(nutritionistUsername);
                list.add(availability);

            }
        } catch (SQLException e) {
            logger.severe("Errore recupero disponibilità da DB: " + e.getMessage());
        }
        return list;
    }

    private void deleteAvailabilityDB(Availability availability) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAvailability.DELETE_AVAILABILITY)) {
            stmt.setString(1, availability.getNutritionistUsername());
            stmt.setDate(2, Date.valueOf(availability.getDate()));
            stmt.setTime(3, Time.valueOf(availability.getStartTime()));
            stmt.setTime(4, Time.valueOf(availability.getEndTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Errore cancellazione disponibilità su DB: " + e.getMessage());
        }
    }

    public void deleteAvailabilitybydata(LocalDate oggi) {

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAvailability.DELETE_AVAILABILITY_BY_DATE)) {

            LocalDate cutoffDate = LocalDate.now(); // oppure una data che scegli
            stmt.setDate(1, java.sql.Date.valueOf(cutoffDate)); // imposta il parametro nella query

            int deleted = stmt.executeUpdate();
            System.out.println("Righe eliminate: " + deleted);

        } catch (SQLException e) {
            e.printStackTrace();
    }
}
}
