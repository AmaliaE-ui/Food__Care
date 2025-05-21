package ispw.food_care.model;

import java.sql.SQLException;
import ispw.food_care.bean.PatientBean;
import ispw.food_care.dao.PatientDAO;
import ispw.food_care.dao.UserDAO;


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
