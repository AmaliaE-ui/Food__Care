package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class HomePatientGuiController{

    @FXML private AnchorPane contentArea;

    @FXML public void initialize() {
        // Verrà chiamato appena il root è attaccato alla Scene
        contentArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                NavigationManager.switchPane(
                        "/ispw/foodcare/BookAppointment/searchNutritionist.fxml",
                        contentArea // va bene come sourceNode
                );
            }
        });
    }

    @FXML public void onHomeClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/searchNutritionist.fxml",
                contentArea);
    }

    @FXML private void onPersonalAreaClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/personalAreaPatient.fxml",
                contentArea);
    }

    @FXML private void onAppointmentsClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/appointmentsPatient.fxml",
                contentArea);
    }

    @FXML private void onHistoryClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/historyPatient.fxml",
                contentArea);
    }

    @FXML private void onLogoutClick(ActionEvent event) {
        Session.getInstance().logout();
        NavigationManager.switchScene(event,
                "/ispw/foodcare/Login/login.fxml",
                "FoodCare - Login");
    }
}
