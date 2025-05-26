package ispw.foodcare.utils.factory;

public class GuiFactory {

    public static String getHomePath(String role) {
        return switch (role.toUpperCase()) {
            case "PATIENT" -> "/ispw/foodcare/homePatient.fxml";
            case "NUTRITIONIST" -> "/ispw/foodcare/homeNutritionist.fxml";
            default -> throw new IllegalArgumentException("Ruolo non supportato: " + role);
        };
    }
}
