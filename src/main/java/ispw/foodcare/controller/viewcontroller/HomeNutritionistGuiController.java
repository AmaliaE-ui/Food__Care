package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.controller.applicationcontroller.AppointmentController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HomeNutritionistGuiController extends BaseGuiController {

    @FXML private Button personalAreaButton;
    @FXML private Button availabilityButton;
    @FXML private Button appointmentsButton;
    @FXML private Label  notificationBadge;
    @FXML private Button logoutButton;
    @FXML private AnchorPane contentArea;

    private final AppointmentController appointmentController = new AppointmentController();

    @FXML public void initialize() {
        // carica il pannello di default
        NavigationManager.switchPane(
                "/ispw/foodcare/personalAreaNutritionist.fxml",
                contentArea);
        checkAndDisplayNotification();
    }

    public void checkAndDisplayNotification() {
        String username = Session.getInstance().getCurrentUser().getUsername();
        boolean hasUnviewed = appointmentController.hasUnviewedAppointmentsForNutritionist(username);
        notificationBadge.setVisible(hasUnviewed);
    }

    @FXML private void onPersonalAreaClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/personalAreaNutritionist.fxml",
                (Node) event.getSource());
    }

    @FXML private void onAvailabilityClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/manageAvailability.fxml",
                (Node) event.getSource());
    }

    @FXML private void onAppointmentsClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/appointmentsNutritionist.fxml",
                (Node) event.getSource());
    }

    @FXML private void onLogoutClick(ActionEvent event) {
        Session.getInstance().logout();
        NavigationManager.switchScene(event,
                "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }
}
