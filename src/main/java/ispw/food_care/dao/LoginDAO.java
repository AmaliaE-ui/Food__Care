package ispw.food_care.dao;

import ispw.food_care.bean.LoginBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
    public boolean checkCredentials(LoginBean bean) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM user WHERE Username = ? AND Password = ? ")) {

            stmt.setString(1, bean.getUsername());
            stmt.setString(2, bean.getPassword());

            ResultSet rs = stmt.executeQuery();
            return rs.next(); //true se esite un utente
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
