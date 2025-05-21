package ispw.foodcare.model;

import java.sql.SQLException;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.dao.PatientDAO;
import ispw.foodcare.dao.UserDAO;


public class PatientModel {

    public void registerPatient(String name, String surname, String phone, String username, String email, String password, String birthDate, String gender) throws SQLException {
        PatientBean bean = new PatientBean();
        bean.setName(name);
        bean.setSurname(surname);
        bean.setPhoneNumber(phone); //  metodo corretto
        bean.setUsername(username);
        bean.setEmail(email);
        bean.setPassword(password);
        bean.setBirthDate(birthDate);
        bean.setGender(gender);

        new UserDAO().saveUser(bean, "PATIENT"); //  specifica ruolo
        new PatientDAO().savePatient(bean);
    }


}
