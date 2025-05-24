package ispw.foodcare.dao;

import ispw.foodcare.bean.LoginBean;
import ispw.foodcare.query.QueryLogin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public boolean checkCredentials(LoginBean bean) {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryLogin.check_credentials)) {

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
