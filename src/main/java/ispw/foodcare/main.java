package ispw.foodcare;

import ispw.foodcare.cli.InitializeCli;
import ispw.foodcare.dao.AppointmentDAO;
import ispw.foodcare.dao.AvailabilityDAO;
import ispw.foodcare.dao.NutritionistDAO;
import ispw.foodcare.dao.UserDAO;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        NavigationManager.switchScene(stage, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }

    public static void main(String[] args) throws AccountAlreadyExistsException {
        Scanner scanner = new Scanner(System.in);
        InitializeCli initializeCli = new InitializeCli();

        System.out.println("Avvio FoodCare");

        // Scegli tra CLI o FXML
        String cliorfxml;
        while (true) {
            System.out.print("Vuoi usare cli(s) o fxml(n)? (S/N): ");
            cliorfxml = scanner.nextLine().trim().toLowerCase();
            if (cliorfxml.equals("s") || cliorfxml.equals("n")) break;
            System.out.println("Inserimento non valido. Inserisci 's' o 'n'.");
        }

        // Scelta persistenza
        String enablePersistence;
        while (true) {
            System.out.print("Vuoi abilitare la persistenza dei dati? (S/N): ");
            enablePersistence = scanner.nextLine().trim().toLowerCase();
            if (enablePersistence.equals("s") || enablePersistence.equals("n")) break;
            System.out.println(" Inserimento non valido. Inserisci 's' o 'n'.");
        }


        if (enablePersistence.equals("s")) {
            Session.getInstance().setRam(false); // Non uso RAM, abilito persistenza

            String typeOfPersistance;
            while (true) {
                System.out.print("Scegli il tipo di persistenza: [1] Database, [2] File System: ");
                typeOfPersistance = scanner.nextLine().trim();
                if (typeOfPersistance.equals("1") || typeOfPersistance.equals("2")) break;
                System.out.println(" Inserimento non valido. Inserisci '1' o '2'.");
            }

            if (typeOfPersistance.equals("1")) {
                Session.getInstance().setDB(true); // uso DB
                Session.getInstance().setUserDAO(new UserDAO());
                Session.getInstance().setNutritionistDAO(new NutritionistDAO());
                Session.getInstance().setAvailabilityDAO(new AvailabilityDAO());
                Session.getInstance().setAppointmentDAO(new AppointmentDAO());
            } else {
                Session.getInstance().setDB(false); // uso file system
            }


        } else {
            Session.getInstance().setRam(true); // uso solo RAM
            Session.getInstance().setUserDAO(new UserDAO());
            Session.getInstance().setNutritionistDAO(new NutritionistDAO());
            Session.getInstance().setAvailabilityDAO(new AvailabilityDAO());
            Session.getInstance().setAppointmentDAO(new AppointmentDAO());
        }

        // Avvia modalità scelta
        if (cliorfxml.equals("s")) {
            System.out.println("Avvio modalità CLI...");
            Session.getInstance().setCLI(true);
            initializeCli.initialize();

        } else {
            System.out.println(" Avvio modalità grafica...");
            Session.getInstance().setCLI(false);
            launch();
        }
    }
}
