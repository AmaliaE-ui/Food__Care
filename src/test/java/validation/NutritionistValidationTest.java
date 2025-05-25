package validation;

import ispw.foodcare.bean.AddressBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.validation.NutritionistValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NutritionistValidationTest {

        @Test
        public void testValidNutritionist() {
            NutritionistBean bean = new NutritionistBean();
            bean.setName("Laura");
            bean.setSurname("Bianchi");
            bean.setUsername("lbianchi");
            bean.setEmail("laura.bianchi@email.com");
            bean.setPhoneNumber("3345678912");
            bean.setPassword("securePass!");
            bean.setPiva("12345678901");
            bean.setTitoloStudio("Laurea in Biologia");
            bean.setSpecializzazione("Sportiva");

            AddressBean address = new AddressBean();
            address.setVia("Via Roma");
            address.setCivico("10");
            address.setCap("00100");
            address.setCitta("Roma");
            address.setProvincia("RM");
            address.setRegione("Lazio");

            String result = NutritionistValidator.validate(bean, address, "securePass!");
            assertNull(result); // validazione superata
        }

        @Test
        public void testMissingSpecializzazione() {
            NutritionistBean bean = new NutritionistBean();
            bean.setName("Laura");
            bean.setSurname("Bianchi");
            bean.setUsername("lbianchi");
            bean.setEmail("laura.bianchi@email.com");
            bean.setPhoneNumber("3345678912");
            bean.setPassword("securePass!");
            bean.setPiva("12345678901");
            bean.setTitoloStudio("Laurea in Biologia");
            bean.setSpecializzazione(null); // <-- mancante

            AddressBean address = new AddressBean();
            address.setVia("Via Roma");
            address.setCivico("10");
            address.setCap("00100");
            address.setCitta("Roma");
            address.setProvincia("RM");
            address.setRegione("Lazio");

            String result = NutritionistValidator.validate(bean, address, "securePass!");
            assertEquals("La specializzazione è obbligatoria.", result);
        }

        @Test
        public void testInvalidPhoneNumber() {
            NutritionistBean bean = new NutritionistBean();
            bean.setName("Laura");
            bean.setSurname("Bianchi");
            bean.setUsername("lbianchi");
            bean.setEmail("laura.bianchi@email.com");
            bean.setPhoneNumber("abcde"); // <-- non valido
            bean.setPassword("securePass!");
            bean.setPiva("12345678901");
            bean.setTitoloStudio("Laurea in Biologia");
            bean.setSpecializzazione("Sportiva");

            AddressBean address = new AddressBean();
            address.setVia("Via Roma");
            address.setCivico("10");
            address.setCap("00100");
            address.setCitta("Roma");
            address.setProvincia("RM");
            address.setRegione("Lazio");

            String result = NutritionistValidator.validate(bean, address, "securePass!");
            assertEquals("Numero di telefono non valido.", result);
        }

        @Test
        public void testMissingAddressField() {
            NutritionistBean bean = new NutritionistBean();
            bean.setName("Laura");
            bean.setSurname("Bianchi");
            bean.setUsername("lbianchi");
            bean.setEmail("laura.bianchi@email.com");
            bean.setPhoneNumber("3345678912");
            bean.setPassword("securePass!");
            bean.setPiva("12345678901");
            bean.setTitoloStudio("Laurea in Biologia");
            bean.setSpecializzazione("Sportiva");

            AddressBean address = new AddressBean();
            address.setVia("Via Roma");
            address.setCivico(""); // <-- mancante
            address.setCap("00100");
            address.setCitta("Roma");
            address.setProvincia("RM");
            address.setRegione("Lazio");

            String result = NutritionistValidator.validate(bean, address, "securePass!");
            assertEquals("Tutti i campi dell'indirizzo sono obbligatori.", result);
        }
}
