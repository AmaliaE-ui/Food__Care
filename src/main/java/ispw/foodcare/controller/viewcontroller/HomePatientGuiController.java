package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class HomePatientGuiController extends BaseGuiController {

    @FXML private AnchorPane contentArea;

    @FXML public void initialize() {
        // Pannello di default
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/searchNutritionist.fxml",
                contentArea);
    }

    @FXML public void onHomeClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/searchNutritionist.fxml",
                (Node) event.getSource());
    }

    @FXML private void onPersonalAreaClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/personalAreaPatient.fxml",
                (Node) event.getSource());
    }

    @FXML private void onAppointmentsClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/appointmentsPatient.fxml",
                (Node) event.getSource());
    }

    @FXML private void onHistoryClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/historyPatient.fxml",
                (Node) event.getSource());
    }

    @FXML private void onLogoutClick(ActionEvent event) {
        Session.getInstance().logout();
        NavigationManager.switchScene(event,
                "/ispw/foodcare/Login/login.fxml",
                "FoodCare - Login");
    }
}
