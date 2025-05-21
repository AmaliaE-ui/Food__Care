package ispw.food_care.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ispw.food_care.bean.UserBean;

public class UserDAO {
    public void saveUser(UserBean bean, String role) throws SQLException {
        String insertUserSql = "INSERT INTO user (username, password, ruolo, nome, cognome, n_telefono, email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertUserSql)) {

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
}
