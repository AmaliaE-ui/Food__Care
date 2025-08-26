package ispw.foodcare.cli;

import java.util.Scanner;

import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.LoginController;
import ispw.foodcare.model.Session;

public class LoginCli {

    private final Scanner scanner = new Scanner(System.in);

    private final LoginController loginController;

    /*Costruttore “default”: prende il DAO dalla Session una volta sola*/
    public LoginCli() {
        var s = Session.getInstance();
        this.loginController = new LoginController(s.getUserDAO());
    }


    public void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            /*Uso l'istanza iniettata*/
            UserBean userBean = loginController.authenticateUser(username, password);

            if (userBean == null) { /*credenziali errate*/
                System.out.println("Login fallito. Credenziali non valide.");
                return;
            }

            /*Set dell'utente corrente (se mantieni Session come store globale)*/
            Session.getInstance().setCurrentUser(userBean);

            System.out.println("Login avvenuto con successo come " + userBean.getRole());
            if (userBean.getRole() == Role.PATIENT) {
                new HomePatientCli(userBean).show();
            } else if (userBean.getRole() == Role.NUTRITIONIST) {
                new HomeNutritionistCli(userBean).show();
            } else {
                System.out.println("Ruolo non riconosciuto.");
            }

        } catch (IllegalArgumentException e) {
            /*Input vuoti*/
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            /*Errori imprevisti*/
            System.out.println("❌ Errore inatteso durante il login: " + e.getMessage());
        }
    }
}
