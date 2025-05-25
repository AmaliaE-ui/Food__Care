package ispw.foodcare.validation;

import ispw.foodcare.utils.FieldValidator;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.AddressBean;

public class NutritionistValidator {

    public static String validateNutritionist(NutritionistBean bean, AddressBean address, String confirmPassword) {
        String commonCheck = UserValidator.validateBaseUser(bean, confirmPassword);
        if (commonCheck != null) return commonCheck;

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

        return null;
    }
}
