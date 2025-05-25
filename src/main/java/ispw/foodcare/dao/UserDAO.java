package ispw.foodcare.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
