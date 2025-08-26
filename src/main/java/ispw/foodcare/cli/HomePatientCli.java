package ispw.foodcare.cli;

import ispw.foodcare.exception.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;
import ispw.foodcare.bean.UserBean;
import java.util.Scanner;

public class HomePatientCli {

    private final Scanner scanner = new Scanner(System.in);
    private final UserBean currentUser;

    public HomePatientCli(UserBean user) {
        this.currentUser = user;
    }

    public void show() throws AccountAlreadyExistsException {

        boolean running = true;

        while (running) {
            System.out.println("\n=== FoodCare - Home PAZIENTE ===");
            System.out.println("Benvenuto, " + currentUser.getName() + "!");
            System.out.println("\n[1] Cerca nutrizionista");
            System.out.println("[2] Area personale");
            System.out.println("[3] Appuntamenti");
            System.out.println("[4] Storico appuntamenti");
            System.out.println("[5] Logout");
            System.out.print("Seleziona un'opzione: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleSearchNutritionist();
                    break;
                case "2":
                    handlePersonalArea();
                    break;
                case "3":
                    handleAppointments();
                    break;
                case "4":
                    handleHistory();
                    break;
                case "5":
                    handleLogout();
                    running = false;
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }

    private void handleSearchNutritionist() throws AccountAlreadyExistsException {

        new SearchNutritionistCli(scanner,currentUser).show();

    }

    private void handlePersonalArea() {
        System.out.println("\n=== FoodCare - Area personale del paziente ===");
        new PersonalAreaPatientCli(currentUser).show();
    }

    private void handleAppointments() {
        System.out.println("\n=== FoodCare - Visualizzazione appuntamenti ===");
        new AppointmentsPatientCli(scanner, currentUser).show();

    }

    private void handleHistory() {
        System.out.println("\n[CLI] -> Visualizzazione storico appuntamenti. - Non implementato");
    }

    private void handleLogout() {
        Session.getInstance().logout();
        System.out.println("Logout effettuato. Torna al menu principale.");
        new InitializeCli().initialize();
    }
}

