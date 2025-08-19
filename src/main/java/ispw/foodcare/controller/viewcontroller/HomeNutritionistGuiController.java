package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.controller.applicationcontroller.AppointmentController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HomeNutritionistGuiController{

    @FXML private Button personalAreaButton;
    @FXML private Button availabilityButton;
    @FXML private Button appointmentsButton;
    @FXML private Label  notificationBadge;
    @FXML private Button logoutButton;
    @FXML private AnchorPane contentArea;

    private final AppointmentController appointmentController = new AppointmentController();
    /*per disiscriversi*/
    private AutoCloseable subscription;


    @FXML public void initialize() {
        // stato iniziale (covers DB: unseen salvati su tabelle)
        checkAndDisplayNotification();

        String me = Session.getInstance().getCurrentUser().getUsername();
        subscription = Session.getInstance().getAppointmentSubject()
                .subscribe(me, event -> {
                    // siamo forse su thread non-JavaFX → vai in UI thread???????
                    javafx.application.Platform.runLater(() -> notificationBadge.setVisible(true));
                });
    }

    public void checkAndDisplayNotification() {
        String username = Session.getInstance().getCurrentUser().getUsername();
        boolean hasUnviewed = appointmentController.hasUnviewedAppointmentsForNutritionist(username);
        notificationBadge.setVisible(hasUnviewed);
    }

    @FXML private void onPersonalAreaClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/personalAreaNutritionist.fxml",
                contentArea);
    }

    @FXML private void onAvailabilityClick(ActionEvent event) {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/manageAvailability.fxml",
                contentArea);
    }

    @FXML private void onAppointmentsClick(ActionEvent event) {
        /*quando apro la lista → segna come "visti" (DB) e togli badge in UI*/
        appointmentController.markAppointmentsAsViewedForNutritionist(
                Session.getInstance().getCurrentUser().getUsername()
        );
        notificationBadge.setVisible(false);
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/appointmentsNutritionist.fxml",
                contentArea);
    }

    @FXML private void onLogoutClick(ActionEvent event) {
        try { if (subscription != null) subscription.close(); } catch (Exception ignored) {}
        Session.getInstance().logout();
        NavigationManager.switchScene(event,
                "/ispw/foodcare/Login/login.fxml",
                "FoodCare - Login");
    }
}
