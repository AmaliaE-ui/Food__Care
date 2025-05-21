package ispw.foodcare.controller.guicontroller;


import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class NutritionistListGuiController {

    @FXML
    private void handleLogout(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "Login");
    }
}

