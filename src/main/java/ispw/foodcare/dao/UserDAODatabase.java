package ispw.foodcare.dao;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.guicontroller.UserDAOInterface;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.Address;
import ispw.foodcare.model.Nutritionist;
import ispw.foodcare.model.Patient;
import ispw.foodcare.model.User;
import ispw.foodcare.query.QueryAddress;
import ispw.foodcare.query.QueryNutrtionist;
import ispw.foodcare.query.QueryPatient;
import ispw.foodcare.query.QueryUser;
import ispw.foodcare.utils.Converter;

public class UserDAODatabase implements UserDAOInterface {

    //Creazione del Logger
    private static final Logger logger = Logger.getLogger(UserDAODatabase.class.getName());

    //Metodo per salvare lo user nel DB
    @Override
    public void saveUser(User user) throws AccountAlreadyExistsException {
        if (getUserByEmail(user.getUsername()) != null) {
            throw new AccountAlreadyExistsException("Account già esistente per lo username: " + user.getUsername());
        }

        try (Connection conn = DBManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            //Inserimento User nel bd
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

            // Inserimento patient o nutritionist
            if (user instanceof Patient patient) {
                try (PreparedStatement stmt = conn.prepareStatement(QueryPatient.INSERT_PATIENT)) {
                    stmt.setString(1, patient.getUsername());
                    stmt.setString(2, patient.getBirthDate());
                    stmt.setString(3, patient.getGender());
                    stmt.executeUpdate();
                }
            } else if (user instanceof Nutritionist nutritionist) {

                // Inserimento address
                int addressId = -1;

                try (PreparedStatement stmt = conn.prepareStatement(QueryAddress.INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS)) {
                    Address a = nutritionist.getAddress();
                    stmt.setString(1, a.getVia());
                    stmt.setString(2, a.getCivico());
                    stmt.setString(3, a.getCap());
                    stmt.setString(4, a.getCitta());
                    stmt.setString(5, a.getProvincia());
                    stmt.setString(6, a.getRegione());
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        addressId = rs.getInt(1);
                    } else {
                        throw new SQLException("Errore nel recupero dell'ID di Address");
                    }
                }

                // Inserimento nutritionist con l'ID appena recuperato
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
            logger.log(Level.SEVERE, "Errore nel salvataggio dell'utente nel database", e);
        }
    }

    //Metodo per cercare un utente per email nel db
    @Override
    public User getUserByEmail(String username) {

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUser.SELECT_BY_USERNAME)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nel recupero dell'utente", e);
        }
        return null;
    }

    //Metodo recupera patient
    /*Metodo reso privsto perchè serve solo all'interno di questa classe*/
    private Patient loadPatient(Connection conn, String username, String name, String surname, String email, String phone, String password) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(QueryPatient.SELECT_PATIENT)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Patient(
                        username,
                        password,
                        name,
                        surname,
                        email,
                        phone,
                        Role.PATIENT,
                        rs.getString("data_nascita"),
                        rs.getString("genere")
                );
            }
        }
        return null;
    }

    private Nutritionist loadNutritionist(Connection conn, String username, String name, String surname, String email, String phone, String password) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(QueryNutrtionist.SELECT_NUTRITIONIST)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int addressId = rs.getInt("indirizzo_studio");
                Address address = loadAddress(conn, addressId);
                return new Nutritionist(
                        username,
                        password,
                        name,
                        surname,
                        email,
                        phone,
                        Role.NUTRITIONIST,
                        rs.getString("piva"),
                        rs.getString("titolo_studio"),
                        rs.getString("specializzazione"),
                        address
                );
            }
        }
        return null;
    }

    private Address loadAddress(Connection conn, int addressId) throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement(QueryAddress.SELECT_ADDRESS)) {
            stmt.setInt(1, addressId);
            ResultSet rs = stmt.executeQuery();
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
        return null;
    }

    @Override
    public UserBean checkLogin(String username, String password) {
        try{
            Connection conn = DBManager.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(QueryUser.SELECT_BY_USERNAME)){
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    String storedPassword = rs.getString("password");
                    if(storedPassword.equals(password)){
                        //Carico dell'utente completo
                        User user = getUserByEmail(username);
                        return Converter.userToBean(user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il login al db", e);
        }
        return null;
    }

    @Override
    public void updateUserModelData(User currentUser) {
        // TODO: implementazione se servono aggiornamenti dei dati
    }

    @Override
    public User loadUserData(User user) {
        return getUserByEmail(user.getUsername());
    }
}
