package ispw.foodcare.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.query.QueryUser;

public class UserDAO {

    public void saveUser(UserBean bean, String role) throws SQLException {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUser.INSERT_USER)) {

            stmt.setString(1, bean.getUsername());
            stmt.setString(2, bean.getPassword());
            stmt.setString(3, role);
            stmt.setString(4, bean.getName());
            stmt.setString(5, bean.getSurname());
            stmt.setString(6, bean.getPhoneNumber());
            stmt.setString(7, bean.getEmail());

            stmt.executeUpdate();
        }
    }

    public UserBean checkLogin(String username, String password) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUser.SELECT_BY_USERNAME_AND_PASSWORD)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("ruolo").toUpperCase();

                    UserBean user;
                    switch (role) {
                        case "PATIENT" -> user = new ispw.foodcare.bean.PatientBean();
                        case "NUTRITIONIST" -> user = new ispw.foodcare.bean.NutritionistBean();
                        default -> throw new IllegalArgumentException("Ruolo non riconosciuto: " + role);
                    }
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password")); // opzionale
                    user.setRole(role);
                    user.setName(rs.getString("nome"));
                    user.setSurname(rs.getString("cognome"));
                    user.setPhoneNumber(rs.getString("n_telefono"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
