package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChooseRoleGuiController {

    @FXML
    private void handlePatientChoice(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/registrationPatient.fxml", "FoodCare - Registrazione");
    }

    @FXML
    private void handleNutritionistChoice(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/registrationNutritionist.fxml", "FoodCare - Registrazione");
    }
}
