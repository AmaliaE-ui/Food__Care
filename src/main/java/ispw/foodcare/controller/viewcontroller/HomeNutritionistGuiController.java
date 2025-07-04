package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class HomeNutritionistGuiController extends BaseGuiController {

    @FXML private Button personalAreaButton;
    @FXML private Button availabilityButton;
    @FXML private Button appointmentsButton;
    @FXML private Button logoutButton;
    @FXML private AnchorPane contentArea;

    private static final double ANCHOR_ZERO = 0.0;

    @FXML
    public void initialize() {
        loadContent("/ispw/foodcare/personalAreaNutritionist.fxml");
    }

    private void loadContent(String fxmlPath) {
        Objects.requireNonNull(fxmlPath, "FXML path must not be null");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node newNode = loader.load();
            setAnchors(newNode);
            contentArea.getChildren().setAll(newNode);
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile caricare il file FXML: " + fxmlPath, e);
        }
    }

    private void setAnchors(Node node) {
        AnchorPane.setTopAnchor(node, ANCHOR_ZERO);
        AnchorPane.setBottomAnchor(node, ANCHOR_ZERO);
        AnchorPane.setLeftAnchor(node, ANCHOR_ZERO);
        AnchorPane.setRightAnchor(node, ANCHOR_ZERO);
    }

    @FXML
    private void handlePersonalArea(ActionEvent event) {
        loadContent("/ispw/foodcare/personalAreaNutritionist.fxml");
    }

    @FXML
    private void handleAvailability(ActionEvent event) {
        loadContent("/ispw/foodcare/BookAppointment/manageAvailability.fxml");
    }

    @FXML
    private void handleAppointments(ActionEvent event) {
        loadContent("/ispw/foodcare/appointmentsNutritionist.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Session.getInstance().logout();
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }
}
