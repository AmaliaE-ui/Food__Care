package ispw.foodcare.dao;

import ispw.foodcare.Role;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.*;
import ispw.foodcare.query.QueryAddress;
import ispw.foodcare.query.QueryNutrtionist;
import ispw.foodcare.query.QueryPatient;
import ispw.foodcare.query.QueryUser;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO unificato per User (Patient, Nutritionist, Admin),
 * gestisce automaticamente persistenza in RAM o DB.
 */
public class UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    /*Injection del Provider*/
    private final ConnectionProvider cp;

    /*Buffer per la modalità RAM*/
    private final List<User> userList = new ArrayList<>();

    public UserDAO(ConnectionProvider cp) {
        this.cp = cp;
    }

    /*Salva un nuovo utente*/
    public void saveUser(User user) throws AccountAlreadyExistsException {
        if (Session.getInstance().isRam()) {
            if (getUserByUsername(user.getUsername()) != null) {
                throw new AccountAlreadyExistsException("User già esistente con username: " + user.getUsername());
            }
            userList.add(user);
        } else if (Session.getInstance().isDB()) {
            saveUserToDB(user);
        }
    }

    /*Carica un utente per username*/
    public User getUserByUsername(String username) {
        if (Session.getInstance().isRam()) {
            for (User user : userList) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
            return null;
        } else if (Session.getInstance().isDB()) {
            return loadUserFromDB(username);
        }
        return null;
    }

    /*Check login*/
    public User checkLogin(String username, String password) {
        if (Session.getInstance().isRam()) {
            for (User user : userList) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
            return null;
        } else if (Session.getInstance().isDB()) {
            try (Connection conn = cp.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(QueryUser.SELECT_BY_USERNAME)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getString("password").equals(password)) {
                        return loadUserFromDB(username); // carico l'entity completa
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Errore durante il login", e);
            }
            return null;
        }
        return null;
    }

    /*Update user (RAM)*/
    public void updateUserModelData(User user) {
        if (Session.getInstance().isRam()) {
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getUsername().equals(user.getUsername())) {
                    userList.set(i, user);
                    return;
                }
            }
            userList.add(user);
        } else if (Session.getInstance().isDB()) {
            /*Da implementare in caso di necessità*/
        }
    }

    public User loadUserData(User user) throws FileNotFoundException {
        return getUserByUsername(user.getUsername());
    }

    // ------------ GESTIONE DB INTERNA ----------------

    private void saveUserToDB(User user) throws AccountAlreadyExistsException {
        if (loadUserFromDB(user.getUsername()) != null) {
            throw new AccountAlreadyExistsException("Account già esistente con lo username: " + user.getUsername());
        }

        Connection conn = null;
        try {
            conn = cp.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(QueryUser.INSERT_USER)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getRole().toString());
                stmt.setString(4, user.getName());
                stmt.setString(5, user.getSurname());
                stmt.setString(6, user.getPhoneNumber());
                stmt.setString(7, user.getEmail());
                stmt.executeUpdate();
            }

            if (user instanceof Patient patient) {
                try (PreparedStatement stmt = conn.prepareStatement(QueryPatient.INSERT_PATIENT)) {
                    stmt.setString(1, patient.getUsername());
                    stmt.setDate(2, java.sql.Date.valueOf(patient.getBirthDate()));/*Conversione da LocalDate a java.sql.Date*/
                    stmt.setString(3, patient.getGender());
                    stmt.executeUpdate();
                }
            } else if (user instanceof Nutritionist nutritionist) {
                int addressId = insertAddress(conn, nutritionist.getAddress());
                try (PreparedStatement stmt = conn.prepareStatement(QueryNutrtionist.INSERT_NUTRITIONIST)) {
                    stmt.setString(1, nutritionist.getUsername());
                    stmt.setString(2, nutritionist.getPiva());
                    stmt.setString(3, nutritionist.getTitoloStudio());
                    stmt.setInt(4, addressId);
                    stmt.setString(5, nutritionist.getSpecializzazione());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Rollback fallito", ex);
                }
            }
            logger.log(Level.SEVERE, "Errore nel salvataggio dell'utente nel database", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {
                    logger.log(Level.WARNING, "Chiusura connessione fallita", e);
                }
            }
        }
    }

    private User loadUserFromDB(String username) {
        try (Connection conn = cp.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUser.SELECT_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = Role.valueOf(rs.getString("ruolo"));
                    String name = rs.getString("nome");
                    String surname = rs.getString("cognome");
                    String email = rs.getString("email");
                    String phone = rs.getString("n_telefono");
                    String password = rs.getString("password");

                    if (role == Role.PATIENT) {
                        return loadPatient(conn, username, name, surname, email, phone, password);
                    } else if (role == Role.NUTRITIONIST) {
                        return loadNutritionist(conn, username, name, surname, email, phone, password);
                    } else {
                        return new User(username, password, name, surname, email, phone, role);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nel recupero dell'utente dal DB", e);
        }
        return null;
    }

    private Patient loadPatient(Connection conn, String username, String name, String surname,
                                String email, String phone, String password) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(QueryPatient.SELECT_PATIENT)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patient(username, password, name, surname, email, phone, Role.PATIENT,
                            rs.getDate("data_nascita").toLocalDate(), rs.getString("genere"));
                }
            }
        }
        return null;
    }

    private Nutritionist loadNutritionist(Connection conn, String username, String name, String surname,
                                          String email, String phone, String password) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(QueryNutrtionist.SELECT_NUTRITIONIST)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int addressId = rs.getInt("indirizzo_studio");
                    Address address = loadAddress(conn, addressId);
                    return new Nutritionist(username, password, name, surname, email, phone, Role.NUTRITIONIST,
                            rs.getString("piva"), rs.getString("titolo_studio"), rs.getString("specializzazione"), address);
                }
            }
        }
        return null;
    }

    private Address loadAddress(Connection conn, int addressId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(QueryAddress.SELECT_ADDRESS)) {
            stmt.setInt(1, addressId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Address(rs.getString("via"), rs.getString("civico"), rs.getString("cap"),
                            rs.getString("citta"), rs.getString("provincia"), rs.getString("regione"));
                }
            }
        }
        return null;
    }


    private int insertAddress(Connection conn, Address address) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(QueryAddress.INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, address.getVia());
            stmt.setString(2, address.getCivico());
            stmt.setString(3, address.getCap());
            stmt.setString(4, address.getCitta());
            stmt.setString(5, address.getProvincia());
            stmt.setString(6, address.getRegione());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Errore nel recupero dell'ID dell'indirizzo");
                }
            }
        }
    }
}