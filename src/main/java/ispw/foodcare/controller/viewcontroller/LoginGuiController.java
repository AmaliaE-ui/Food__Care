package ispw.foodcare.controller.viewcontroller;


import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.LoginController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginGuiController {

    @FXML private Label loginMessageLabel;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;

    /*Iniettiamo il controller applicativo*/
    private final LoginController loginController;

    /*Costruttore vuoto usato da FXML: prende il DAO dalla Session UNA volta (composition root minimale)*/
    public LoginGuiController() {
        var s = Session.getInstance();
        this.loginController = new LoginController(s.getUserDAO());
    }
    /*costruttore per test*/
    public LoginGuiController(LoginController loginController) {
        this.loginController = loginController;
    }

    /*Login*/
    @FXML private void onLoginClick(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        try {
            UserBean userBean = loginController.authenticateUser(username, password);

            if (userBean == null) {
                loginMessageLabel.setStyle("-fx-text-fill: red;");
                loginMessageLabel.setText("Login fallito. Credenziali non valide.");
                return;
            }

            /* Salva nella sessione */
            Session.getInstance().setCurrentUser(userBean);

            /* Home dinamica */
            Role role = userBean.getRole();
            String homePath = ispw.foodcare.utils.factory.GuiFactory.getHomePath(role);
            NavigationManager.switchScene(event, homePath, "FoodCare - Home");

        } catch (IllegalArgumentException e) {
            /*input vuoti*/
            loginMessageLabel.setStyle("-fx-text-fill: red;");
            loginMessageLabel.setText(e.getMessage());
        } catch (Exception e) {
            /*errori tecnici imprevisti (IO/DAO...)*/
            loginMessageLabel.setStyle("-fx-text-fill: red;");
            loginMessageLabel.setText("Errore durante il login: " + e.getMessage());
        }
    }

    /*Registrazione*/
    @FXML private void onRegistrationClick(ActionEvent event) {
        NavigationManager.switchScene(event, "/ispw/foodcare/Login/chooseRole.fxml", "Seleziona ruolo");
    }
}
