package ispw.food_care.controller.guiController;

import ispw.food_care.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseRoleGuiController {

    @FXML
    private void handlePatientChoice(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/food_care/Login/registrationPatient.fxml", "FoodCare - Registrazione");
        //loadRegistrationScene("/com/ispw/foodcare/view/registrationPatient.fxml", event);
    }

    @FXML
    private void handleNutritionistChoice(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/food_care/Login/registrationNutritionist.fxml", "FoodCare - Registrazione");
        //loadRegistrationScene("/com/ispw/foodcare/view/registrationNutritionist.fxml", event);
    }

    /*private void loadRegistrationScene(String fxmlPath, ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrazione");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
