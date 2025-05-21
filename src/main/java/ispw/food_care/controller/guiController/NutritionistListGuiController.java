package ispw.food_care.controller.guiController;


import ispw.food_care.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class NutritionistListGuiController {

    @FXML
    private void handleLogout(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/food_care/Login/login.fxml", "Login");
    }
}

