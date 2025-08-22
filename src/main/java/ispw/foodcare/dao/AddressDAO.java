package ispw.foodcare.dao;

import ispw.foodcare.model.Address;
import ispw.foodcare.model.Session;
import ispw.foodcare.query.QueryAddress;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AddressDAO {

    private static final Logger logger = Logger.getLogger(AddressDAO.class.getName());

    private final ConnectionProvider cp;

    // ---- RAM store ----
    private final Map<Integer, Address> ram = new HashMap<>();
    private int nextId = 1;

    public AddressDAO(ConnectionProvider cp) {
        this.cp = cp;
    }

    /* Salva un indirizzo e ritorna l'ID */
    public int saveRam(Address address) {
            int id = nextId++;
            ram.put(id, address);
            return id;
    }

    /* Variante che partecipa a una transazione esistente */
    public int save(Address address, Connection conn) throws SQLException {
        if (Session.getInstance().getRam()) {
            return saveRam(address);
        } else if (Session.getInstance().getDB()) {
            return insertDB(address, conn);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    public Address findByIdRam(int id) {
            return ram.get(id);
    }

    /* Variante transazionale */
    public Address findById(int id, Connection conn) throws SQLException {
        if (Session.getInstance().getRam()) {
            return findByIdRam(id);
        } else if (Session.getInstance().getDB()) {
            return loadDB(id, conn);
        } else {
            throw new IllegalStateException("Modalità persistenza non configurata.");
        }
    }

    // ----------- PRIVATE DB helpers -----------


    private int insertDB(Address a, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                QueryAddress.INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getVia());
            stmt.setString(2, a.getCivico());
            stmt.setString(3, a.getCap());
            stmt.setString(4, a.getCitta());
            stmt.setString(5, a.getProvincia());
            stmt.setString(6, a.getRegione());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("Nessun ID generato per Address");
            }
        }
    }

    private Address loadDB(int id, Connection conn) throws SQLException {
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
                return null;
            }
        }
    }

}
