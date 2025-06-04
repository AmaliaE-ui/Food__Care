package ispw.foodcare.controller.guicontroller;

import ispw.foodcare.utils.NavigationManager;
import ispw.foodcare.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomeNutritionistGuiController extends BaseGuiController{

    @FXML private Button personalAreaButton;
    @FXML private Button availabilityButton;
    @FXML private Button appointmentsButton;
    @FXML private Button logoutButton;

    @FXML private AnchorPane contentArea;

    @FXML
    public void initialize() {
        loadContent("/ispw/foodcare/personalAreaNutritionist.fxml");
    }

    private void loadContent(String fxmlPath){
        try {
            javafx.scene.Node newNode = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(newNode);

            //Assicura il contenuto si addatti a tutto l'AncorePAne
            AnchorPane.setTopAnchor(newNode, 0.0);
            AnchorPane.setBottomAnchor(newNode, 0.0);
            AnchorPane.setLeftAnchor(newNode, 0.0);
            AnchorPane.setRightAnchor(newNode, 0.0);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePersonalArea(ActionEvent event) {
        loadContent("/ispw/foodcare/personalAreaNutritionist.fxml");
    }

    @FXML
    private void handleAvailability(ActionEvent event) {
        loadContent("/ispw/foodcare/availabilityNutritionist.fxml");
    }

    @FXML
    private void handleAppointments(ActionEvent event) {
        loadContent("/ispw/foodcare/appointmentsNutritionist.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.getInstance().logout();
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }
}
