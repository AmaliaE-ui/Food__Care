package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChooseRoleGuiController {

    @FXML private void onPatientChoiceClick(ActionEvent event) {
        NavigationManager.switchScene(event,
                "/ispw/foodcare/Login/registrationPatient.fxml",
                "FoodCare - Registrazione");
    }

    @FXML private void onNutritionistChoiceClick(ActionEvent event) {
        NavigationManager.switchScene(event,
                "/ispw/foodcare/Login/registrationNutritionist.fxml",
                "FoodCare - Registrazione");
    }
}
