package ispw.foodcare.controller.viewcontroller;


import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomePatientGuiController extends BaseGuiController {


    @FXML private Button personalAreaButton;
    @FXML private Button appointmentButton;
    @FXML private Button historyButton;
    @FXML private Button logoutButton;
    @FXML private Button homeButton;

    @FXML private AnchorPane contentArea; // deve esistere nel mio FXML

    @FXML public void initialize() {
        loadContent("/ispw/foodcare/BookAppointment/searchNutritionist.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            javafx.scene.Node newNode = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(newNode);

            //Assicura il contenuto si addatti a tutto l'AncorePAne
            AnchorPane.setTopAnchor(newNode, 0.0);
            AnchorPane.setBottomAnchor(newNode, 0.0);
            AnchorPane.setLeftAnchor(newNode, 0.0);
            AnchorPane.setRightAnchor(newNode, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleHome(ActionEvent event) {
        loadContent("/ispw/foodcare/BookAppointment/searchNutritionist.fxml");
    }

    @FXML private void handlePersonalArea(ActionEvent event) {
        //carica dinamicamente solo l'AnchorPane dentro il BorderPane generale che non viene modificato
        loadContent("/ispw/foodcare/personalAreaPatient.fxml");
    }

    @FXML private void handleAppointments(ActionEvent event) { loadContent("/ispw/foodcare/appointmentsPatient.fxml"); }

    @FXML private void handleHistory(ActionEvent event) {
        loadContent("/ispw/foodcare/historyPatient.fxml");
    }

    @FXML private void handleLogout(ActionEvent event) {
        Session.getInstance().logout();
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }
}
