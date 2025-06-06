package validation;

import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.validation.PatientValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PatientValidationTest {

    @Test
    public void testValidPatient() {
        PatientBean bean = new PatientBean();
        bean.setName("Mario");
        bean.setSurname("Rossi");
        bean.setUsername("mrossi");
        bean.setEmail("m.rossi@email.com");
        bean.setPhoneNumber("1234567890");
        bean.setPassword("password123");
        bean.setBirthDate("1990-01-01");
        bean.setGender("Maschio");

        String confirmPassword = "password123";

        String result = PatientValidator.validatePatient(bean, confirmPassword);
        assertNull(result); // nessun errore, validazione superata
    }

    @Test
    public void testEmptyName() {
        PatientBean bean = new PatientBean();
        bean.setName(""); // campo vuoto
        bean.setSurname("Rossi");
        bean.setUsername("mrossi");
        bean.setEmail("m.rossi@email.com");
        bean.setPhoneNumber("1234567890");
        bean.setPassword("password123");
        bean.setBirthDate("1990-01-01");
        bean.setGender("Maschio");

        String result = PatientValidator.validatePatient(bean, "password123");
        assertEquals("Il nome è obbligatorio.", result);
    }

    @Test
    public void testInvalidEmail() {
        PatientBean bean = new PatientBean();
        bean.setName("Mario");
        bean.setSurname("Rossi");
        bean.setUsername("mrossi");
        bean.setEmail("nonèunemail");
        bean.setPhoneNumber("1234567890");
        bean.setPassword("password123");
        bean.setBirthDate("1990-01-01");
        bean.setGender("Maschio");

        String result = PatientValidator.validatePatient(bean, "password123");
        assertEquals("Email non valida.", result);
    }

    @Test
    public void testPasswordsDoNotMatch() {
        PatientBean bean = new PatientBean();
        bean.setName("Mario");
        bean.setSurname("Rossi");
        bean.setUsername("mrossi");
        bean.setEmail("m.rossi@email.com");
        bean.setPhoneNumber("1234567890");
        bean.setPassword("password123");
        bean.setBirthDate("1990-01-01");
        bean.setGender("Maschio");

        String result = PatientValidator.validatePatient(bean, "altrapassword");
        assertEquals("Le password non corrispondono.", result);
    }
}

