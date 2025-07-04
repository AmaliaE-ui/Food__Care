package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.Role;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class BaseGuiController {

    @FXML
    protected AnchorPane myAnchorPane;

    @FXML
    protected Button homeButton;

    // Pulsante Home: torna alla dashboard dell’utente loggato
    @FXML
    protected void goHomePage(ActionEvent event) {
        Role role = Session.getInstance().getCurrentRole();
        if (role == null) {
            NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
            return;
        }

        String path = ispw.foodcare.utils.factory.GuiFactory.getHomePath(role); // Factory per routing dinamico
        NavigationManager.switchScene(event, path, "FoodCare - Home");
        }

        // Pulsante Indietro al login
        @FXML
        protected void handleBackToLogin(ActionEvent event) {
            NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
        }
}
