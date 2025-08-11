package ispw.foodcare.cli;

import java.util.Scanner;

import ispw.foodcare.Role;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.LoginController;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;

public class LoginCli {

    Scanner scanner = new Scanner(System.in);

    public void login() throws AccountAlreadyExistsException {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserBean userBean = LoginController.getInstance().authenticateUser(username, password);

        if (userBean != null) {
            Session.getInstance().setCurrentUser(userBean);
            System.out.println("Login avvenuto con successo come " + userBean.getRole());
            if(userBean.getRole() == Role.PATIENT) {
                new HomePatientCli(userBean).show();
            }
            else if(userBean.getRole() == Role.NUTRITIONIST) {

                new HomeNutritionistCli(userBean).show();
            }
        } else {
            System.out.println("Login fallito. Credenziali non valide.");
        }
    }


}
