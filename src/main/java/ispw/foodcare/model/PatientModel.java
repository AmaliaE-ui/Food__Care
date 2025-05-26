package ispw.foodcare.model;

import java.sql.SQLException;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.dao.PatientDAO;
import ispw.foodcare.dao.UserDAO;


public class PatientModel {

    private final UserDAO userDAO = new UserDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    public void registerPatient(PatientBean bean) throws SQLException {
        userDAO.saveUser(bean, "PATIENT"); //  specifica ruolo
        patientDAO.savePatient(bean);
    }
}
