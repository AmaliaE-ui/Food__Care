package ispw.foodcare.validation;

import ispw.foodcare.utils.FieldValidator;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.AddressBean;

public class NutritionistValidator {

    public static String validate(NutritionistBean bean, AddressBean address, String confirmPassword) {
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
        if (FieldValidator.isEmpty(bean.getPiva())) return "La partita IVA è obbligatoria.";
        if (FieldValidator.isEmpty(bean.getTitoloStudio())) return "Il titolo di studio è obbligatorio.";
        if (FieldValidator.isEmpty(bean.getSpecializzazione())) return "La specializzazione è obbligatoria.";

        if (FieldValidator.isEmpty(address.getVia()) ||
                FieldValidator.isEmpty(address.getCivico()) ||
                FieldValidator.isEmpty(address.getCap()) ||
                FieldValidator.isEmpty(address.getCitta()) ||
                FieldValidator.isEmpty(address.getProvincia()) ||
                FieldValidator.isEmpty(address.getRegione())) {
            return "Tutti i campi dell'indirizzo sono obbligatori.";
        }

        return null; // Validazione superata
    }
}
