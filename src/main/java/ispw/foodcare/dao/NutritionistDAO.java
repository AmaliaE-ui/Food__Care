package ispw.foodcare.dao;

import ispw.foodcare.model.Nutritionist;
import ispw.foodcare.model.Address;
import ispw.foodcare.query.QueryAddress;
import ispw.foodcare.query.QueryNutrtionist;
import ispw.foodcare.Role;
import ispw.foodcare.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NutritionistDAO {

    private static final Logger logger = Logger.getLogger(NutritionistDAO.class.getName());

    private final ConnectionProvider cp;

    public NutritionistDAO(ConnectionProvider cp) {
        this.cp = cp;
    }

    /*Public interface for controller applicativi*/
    public List<Nutritionist> getByCity(String city) {
        if (Session.getInstance().isRam()) {
            return getByCityRam(city);
        } else if (Session.getInstance().isDB()) {
            return getByCityDB(city);
        } else {
            throw new IllegalStateException("Modalità di persistenza non configurata");
        }
    }

    // -----------------------
    // RAM MODE
    // -----------------------
    private final List<Nutritionist> nutritionistList = new ArrayList<>();

    private List<Nutritionist> getByCityRam(String city) {
        // Se è la prima chiamata, popolo vuoto per coerenza con la regola:
        // "nessun dato precaricato"
        return nutritionistList.stream()
                .filter(n -> n.getAddress() != null &&
                        n.getAddress().getCitta().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    public void saveNutritionistRam(Nutritionist nutritionist) {
        nutritionistList.add(nutritionist);
    }

    // -----------------------
    // DB MODE
    // -----------------------
    private List<Nutritionist> getByCityDB(String city) {
        List<Nutritionist> nutritionists = new ArrayList<>();

        try (Connection conn = cp.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryNutrtionist.SELECT_BY_CITY)) {

            stmt.setString(1, city);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String name = rs.getString("nome");
                    String surname = rs.getString("cognome");
                    String email = rs.getString("email");
                    String phone = rs.getString("n_telefono");
                    String password = rs.getString("password");
                    String piva = rs.getString("piva");
                    String titoloStudio = rs.getString("titolo_studio");
                    String specializzazione = rs.getString("specializzazione");
                    int addressId = rs.getInt("indirizzo_studio");

                    Address address = loadAddress(conn, addressId);

                    nutritionists.add(new Nutritionist(
                            username, password, name, surname, email, phone,
                            Role.NUTRITIONIST, piva, titoloStudio, specializzazione, address
                    ));
                }
            }
        } catch (SQLException e) {
            logger.severe("Errore nel recupero dei nutritionist da DB: " + e.getMessage());
        }

        return nutritionists;
    }

    private Address loadAddress(Connection conn, int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(QueryAddress.SELECT_ADDRESS)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Address(
                            rs.getString("via"),
                            rs.getString("civico"),
                            rs.getString("cap"),
                            rs.getString("citta"),
                            rs.getString("provincia"),
                            rs.getString("regione")
                    );
                }
            }
        }
        return null;
    }
}