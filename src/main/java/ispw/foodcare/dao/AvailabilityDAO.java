package ispw.foodcare.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ispw.foodcare.model.Availability;
import ispw.foodcare.model.Session;
import ispw.foodcare.query.QueryAvailability;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AvailabilityDAO {

    private static final Logger logger = Logger.getLogger(AvailabilityDAO.class.getName());

    /* injection (usata solo in DB) */
    private final ConnectionProvider cp;

    /* === FS store (JSON) === */
    private final Path dataDir = Paths.get("data");
    private final Path fsFile  = dataDir.resolve("availabilities.json");
    private final ObjectMapper om = new ObjectMapper()
            .findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static class Store {
        public final List<Record> items = new ArrayList<>();
    }
    private static class Record {
        private String nutritionistUsername;
        private LocalDate date;
        private java.time.LocalTime startTime;
        private java.time.LocalTime endTime;
    }

    public AvailabilityDAO(ConnectionProvider cp) {
        this.cp = cp;
    }

    // --------------------------
    // PUBLIC API
    // --------------------------
    public void addAvailability(Availability availability) {
        if (Session.getInstance().isRam()) {
            addAvailabilityRam(availability);
        } else if (Session.getInstance().isDB()) {
            addAvailabilityDB(availability);
        } else if (Session.getInstance().isFS()) {
            addAvailabilityFS(availability);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    public List<Availability> getAvailabilitiesForNutritionist(String nutritionistUsername) {
        if (Session.getInstance().isRam()) {
            return getAvailabilitiesRam(nutritionistUsername);
        } else if (Session.getInstance().isDB()) {
            return getAvailabilitiesDB(nutritionistUsername);
        } else if (Session.getInstance().isFS()) {
            return getAvailabilitiesFS(nutritionistUsername);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    public void deleteAvailability(Availability availability) {
        if (Session.getInstance().isRam()) {
            deleteAvailabilityRam(availability);
        } else if (Session.getInstance().isDB()) {
            deleteAvailabilityDB(availability);
        } else if (Session.getInstance().isFS()) {
            deleteAvailabilityFS(availability);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    public void deleteAvailabilitybydata(LocalDate cutoffDate) {
        if (Session.getInstance().isRam()) {
            // rimuovi tutto <= cutoffDate
            availabilitiesRam.removeIf(a -> !a.getDate().isAfter(cutoffDate));
        } else if (Session.getInstance().isDB()) {
            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(QueryAvailability.DELETE_AVAILABILITY_BY_DATE)) {
                stmt.setDate(1, java.sql.Date.valueOf(cutoffDate));
                int deleted = stmt.executeUpdate();
                logger.log(Level.INFO, () -> "Righe eliminate: " + deleted);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Errore cancellazione disponibilità da DB", e);
            }
        } else if (Session.getInstance().isFS()) {
            deleteAvailabilityByDateFS(cutoffDate);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    // --------------------------
    // RAM MODE
    // --------------------------
    private final List<Availability> availabilitiesRam = new ArrayList<>();

    private void addAvailabilityRam(Availability availability) {
        // evita duplicati esatti
        availabilitiesRam.removeIf(a -> matches(a, availability));
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
        // prima usavi remove(availability) (identità). Meglio per valori:
        availabilitiesRam.removeIf(a -> matches(a, availability));
    }

    // --------------------------
    // DB MODE
    // --------------------------
    private void addAvailabilityDB(Availability availability) {
        try (Connection conn = cp.getConnection();
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
        try (Connection conn = cp.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryAvailability.SELECT_AVAILABILITIES_FOR_NUTRITIONIST)) {
            stmt.setString(1, nutritionistUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Availability availability = new Availability();
                    availability.setDate(rs.getDate("data").toLocalDate());
                    availability.setStartTime(rs.getTime("ora_inizio").toLocalTime());
                    availability.setEndTime(rs.getTime("ora_fine").toLocalTime());
                    availability.setNutritionistUsername(nutritionistUsername);
                    list.add(availability);
                }
            }
        } catch (SQLException e) {
            logger.severe("Errore recupero disponibilità da DB: " + e.getMessage());
        }
        return list;
    }

    private void deleteAvailabilityDB(Availability availability) {
        try (Connection conn = cp.getConnection();
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

    // --------------------------
    // FS MODE (JSON)
    // --------------------------
    private synchronized Store loadStore() throws IOException {
        if (Files.notExists(dataDir)) Files.createDirectories(dataDir);
        if (Files.notExists(fsFile)) {
            Store empty = new Store();
            om.writeValue(fsFile.toFile(), empty);
            return empty;
        }
        return om.readValue(fsFile.toFile(), Store.class);
    }

    private synchronized void saveStore(Store s) throws IOException {
        om.writeValue(fsFile.toFile(), s);
    }

    private static Record toRecord(Availability a) {
        Record r = new Record();
        r.nutritionistUsername = a.getNutritionistUsername();
        r.date = a.getDate();
        r.startTime = a.getStartTime();
        r.endTime = a.getEndTime();
        return r;
    }

    private static Availability toModel(Record r) {
        Availability a = new Availability();
        a.setNutritionistUsername(r.nutritionistUsername);
        a.setDate(r.date);
        a.setStartTime(r.startTime);
        a.setEndTime(r.endTime);
        return a;
    }

    private static boolean matches(Availability a, Availability b) {
        return a.getNutritionistUsername().equals(b.getNutritionistUsername())
                && a.getDate().equals(b.getDate())
                && a.getStartTime().equals(b.getStartTime())
                && a.getEndTime().equals(b.getEndTime());
    }

    private static boolean matches(Record r, Availability a) {
        return r.nutritionistUsername.equals(a.getNutritionistUsername())
                && r.date.equals(a.getDate())
                && r.startTime.equals(a.getStartTime())
                && r.endTime.equals(a.getEndTime());
    }

    private void addAvailabilityFS(Availability availability) {
        try {
            Store s = loadStore();
            // evita duplicati esatti
            s.items.removeIf(r -> matches(r, availability));
            s.items.add(toRecord(availability));
            saveStore(s);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "FS addAvailability error", e);
        }
    }

    private List<Availability> getAvailabilitiesFS(String nutritionistUsername) {
        try {
            Store s = loadStore();
            List<Availability> out = new ArrayList<>();
            for (Record r : s.items) {
                if (r.nutritionistUsername.equals(nutritionistUsername)) {
                    out.add(toModel(r));
                }
            }
            return out;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "FS getAvailabilities error", e);
            return new ArrayList<>();
        }
    }

    private void deleteAvailabilityFS(Availability availability) {
        try {
            Store s = loadStore();
            int before = s.items.size();
            s.items.removeIf(r -> matches(r, availability));
            int removed = before - s.items.size();
            if (removed > 0) logger.log(Level.INFO, () -> "FS availability removed: " + removed);
            saveStore(s);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "FS deleteAvailability error", e);
        }
    }

    private void deleteAvailabilityByDateFS(LocalDate cutoffDate) {
        try {
            Store s = loadStore();
            int before = s.items.size();
            // elimina tutte le disponibilità con data <= cutoffDate
            s.items.removeIf(r -> !r.date.isAfter(cutoffDate));
            int removed = before - s.items.size();
            logger.log(Level.INFO, () -> "FS Righe eliminate: " + removed);
            saveStore(s);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "FS deleteAvailabilityByDate error", e);
        }
    }
}
