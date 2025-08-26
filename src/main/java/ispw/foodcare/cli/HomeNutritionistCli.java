package ispw.foodcare.cli;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.AppointmentController;
import ispw.foodcare.exception.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;
import java.util.Scanner;

public class HomeNutritionistCli {

    private final Scanner scanner = new Scanner(System.in);
    private final UserBean currentUser;
    private final AppointmentController appointmentController = new AppointmentController();

    public HomeNutritionistCli(UserBean user) {
        this.currentUser = user;
    }

    public void show() throws AccountAlreadyExistsException {
        // Mostra badge notifica se ci sono appuntamenti non visionati
        checkAndDisplayNotification();

        boolean running = true;

        while (running) {
            System.out.println("\n=== FoodCare - Home NUTRIZIONISTA ===");
            System.out.println("Benvenuto, Dr. " + currentUser.getSurname());
            System.out.println("\n[1] Area personale");
            System.out.println("[2] Gestisci disponibilità");
            System.out.println("[3] Appuntamenti");
            System.out.println("[4] Logout");
            System.out.print("Seleziona un'opzione: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handlePersonalArea();
                    break;
                case "2":
                    handleAvailability();
                    break;
                case "3":
                    handleAppointments();
                    break;
                case "4":
                    handleLogout();
                    running = false;
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }

    private void checkAndDisplayNotification() {
        boolean hasUnviewed = appointmentController.hasUnviewedAppointmentsForNutritionist(currentUser.getUsername());
        if (hasUnviewed) {
            System.out.println("\u26A0 Hai appuntamenti non ancora visionati! Controlla nella sezione 'Appuntamenti'.");
        }
    }

    private void handlePersonalArea() {

        new PersonalAreaNutritionistCli(currentUser).show();
    }

    private void handleAvailability() throws AccountAlreadyExistsException {

        new ManageAvailabilityCli(scanner,currentUser).show();

    }

    private void handleAppointments() {
        System.out.println("\n=== FoodCare -  Visualizzazione appuntamenti ===");
        new AppointmentsNutritionistCli(scanner, currentUser).show();
    }

    private void handleLogout() throws AccountAlreadyExistsException {
        Session.getInstance().logout();
        System.out.println("Logout effettuato.");
        new InitializeCli().initialize();
    };

}
