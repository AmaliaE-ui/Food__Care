package ispw.foodcare.validation;

import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.utils.FieldValidator;

public class PatientValidator {

    public static String validate(PatientBean bean, String confirmPassword) {
        if (FieldValidator.isEmpty(bean.getName())) return "Il nome è obbligatorio.";
        if (FieldValidator.isEmpty(bean.getSurname())) return "Il cognome è obbligatorio.";
        if (FieldValidator.isEmpty(bean.getUsername())) return "Lo username è obbligatorio.";
        if (FieldValidator.isEmpty(bean.getEmail())) return "L'email è obbligatoria.";
        if (!FieldValidator.isEmailValid(bean.getEmail())) return "Email non valida.";
        if (FieldValidator.isEmpty(bean.getPhoneNumber())) return "Il numero di telefono è obbligatorio.";
        if (!FieldValidator.isPhoneValid(bean.getPhoneNumber())) return "Numero di telefono non valido.";
        if (FieldValidator.isEmpty(bean.getPassword()) || FieldValidator.isEmpty(confirmPassword))
            return "Inserisci e conferma la password.";
        if (!FieldValidator.doPasswordsMatch(bean.getPassword(), confirmPassword))
            return "Le password non corrispondono.";
        if (bean.getBirthDate() == null || bean.getBirthDate().isBlank()) return "La data di nascita è obbligatoria.";
        if (bean.getGender() == null || bean.getGender().isBlank()) return "Seleziona un genere.";
        return null; // Validazione superata
    }
}
