package ispw.foodcare.validation;

import ispw.foodcare.bean.PatientBean;

public class PatientValidator {

    public static String validatePatient(PatientBean bean, String confirmPassword) {
        String commonCheck = UserValidator.validateBaseUser(bean, confirmPassword);
        if (commonCheck != null) return commonCheck;

        if (bean.getBirthDate() == null || bean.getBirthDate().isBlank()) return "La data di nascita è obbligatoria";
        if (bean.getGender() == null || bean.getGender().isBlank()) return "Seleziona un genere";
        return commonCheck;
    }
}
