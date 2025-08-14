package ispw.foodcare.utils.factory;

import ispw.foodcare.Role;

public class GuiFactory {

    private GuiFactory() {}

    public static String getHomePath(Role role) {
        return switch (role) {
            case PATIENT -> "/ispw/foodcare/BookAppointment/homePatient.fxml";
            case NUTRITIONIST -> "/ispw/foodcare/BookAppointment/homeNutritionist.fxml";
            default -> throw new IllegalArgumentException("Ruolo non supportato: " + role);
        };
    }
}
