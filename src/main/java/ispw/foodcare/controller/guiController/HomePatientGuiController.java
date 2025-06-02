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

    @FXML private AnchorPane contentArea; // deve esistere nel mio FXML

    @FXML
    public void initialize() {
        loadContent("/ispw/foodcare/homePatient.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            //var resource = getClass().getResource(fxmlPath);
            //System.out.println("Caricamento FXML: " + fxmlPath);
            //System.out.println("Risorsa trovata: " + resource);

            //if (resource == null) {
              //  System.err.println("❌ FXML non trovato: " + fxmlPath);
                //return;
            //}

           // AnchorPane newContent = FXMLLoader.load(resource);
            //contentArea.getChildren().setAll(newContent);

            AnchorPane newContent = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(newContent);
        } catch (IOException e) {
           // System.err.println("❌ Errore nel caricamento FXML: " + fxmlPath);
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

