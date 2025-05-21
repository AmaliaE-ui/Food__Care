package ispw.food_care.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import ispw.food_care.bean.PatientBean;

import java.sql.*;

public class PatientDAO {
    public void savePatient(PatientBean patient) throws SQLException {
        String insertPatientSql = "INSERT INTO patient (username, data_nascita, genere) VALUES (?, ?, ?)";

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertPatientSql)) {

            stmt.setString(1, patient.getUsername());
            stmt.setDate(2, Date.valueOf(patient.getBirthDate()));
            stmt.setString(3, patient.getGender());

            stmt.executeUpdate();
        }
    }
}



