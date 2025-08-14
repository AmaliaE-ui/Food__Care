package ispw.foodcare;

import ispw.foodcare.cli.InitializeCli;
import ispw.foodcare.dao.AppointmentDAO;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override public void start(Stage stage) {
        NavigationManager.switchScene(stage, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        logger.info("Avvio FoodCare");

        /*Istanzio la Sssione*/
        Session s = Session.getInstance();

        /*Scegli tra CLI o FXML*/
        boolean useCli = askYesNo(scanner, "Vuoi usare cli(s) o fxml(n)? (S/N): ");

        /*Scelta persistenza*/
        boolean enablePersistence = askYesNo(scanner, "Vuoi abilitare la persistenza dei dati? (S/N): ");


        if (enablePersistence) {
            String type = askChoice(scanner, "Scegli il tipo di persistenza: [1] Database, [2] File System: ", "1", "2");
            if ("1".equals(type)) {
                s.setDB(true);
                s.setRam(false);
                logger.info("Modalità persistente: Database.");
            } else {
                s.setDB(false);
                s.setRam(false);
            }
        } else {
            s.setDB(false);
            s.setRam(true);
            logger.info("Modalità RAM (no persistenza).");
        }

        s.setCLI(useCli);

        /*Avvia modalità scelta*/
        if (useCli) {
            logger.info("Avvio modalità CLI...");
            try {
                new InitializeCli().initialize();
            } catch (AccountAlreadyExistsException e) {
                logger.info("Errore inizializzazione CLI: " + e.getMessage());
            }
        } else {
            logger.info(" Avvio modalità grafica...");
            launch(args);
        }
    }

    /*Metodi helper*/
    private static boolean askYesNo(Scanner scanner, String prompt) {
        while (true) {
            logger.info(prompt);
            String in = scanner.nextLine().trim().toLowerCase();
            if (in.equals("s")) return true;
            if (in.equals("n")) return false;
            logger.info("Inserimento non valido. Inserisci 's' o 'n'.");
        }
    }

    private static String askChoice(Scanner scanner, String prompt, String... accepted) {
        while (true) {
            logger.info(prompt);
            String in = scanner.nextLine().trim().toLowerCase();
            for (String a : accepted) if (a.equals(in)) return in;
            logger.info("Inserimento non valido. Opzioni: " + String.join("/", accepted));
        }
    }
}
