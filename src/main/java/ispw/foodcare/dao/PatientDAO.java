package ispw.foodcare.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.query.QueryPatient;

import java.sql.*;

public class PatientDAO {

    public void savePatient(PatientBean patient) throws SQLException {
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryPatient.insert_patient)) {

            stmt.setString(1, patient.getUsername());
            stmt.setDate(2, Date.valueOf(patient.getBirthDate()));
            stmt.setString(3, patient.getGender());

            stmt.executeUpdate();
        }
    }
}



