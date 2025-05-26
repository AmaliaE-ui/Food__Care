package ispw.foodcare.controller.guicontroller;


import ispw.foodcare.utils.NavigationManager;
import ispw.foodcare.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomePatientGuiController extends BaseGuiController{


    @FXML private Button personalAreaButton;
    @FXML private Button appointmentButton;
    @FXML private Button historyButton;
    @FXML private Button logoutButton;

    @FXML private AnchorPane contentArea; // deve esistere nel tuo FXML

    @FXML
    public void initialize() {
        loadContent("/ispw/foodcare/Patient/personalAreaPatient.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            AnchorPane newContent = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePersonalArea(ActionEvent event) {
        loadContent("/ispw/foodcare/Patient/personalAreaPatient.fxml");
    }

    @FXML
    private void handleAppointments(ActionEvent event) {
        loadContent("/ispw/foodcare/Patient/appointmentsPatient.fxml");
    }

    @FXML
    private void handleHistory(ActionEvent event) {
        loadContent("/ispw/foodcare/Patient/historyPatient.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.getInstance().logout();
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }
}

