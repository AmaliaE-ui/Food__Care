package ispw.foodcare;

import ispw.foodcare.cli.InitializeCli;
import ispw.foodcare.exception.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String PROMPT_USE_CLI            = "Vuoi usare CLI? (s/n): ";
    private static final String PROMPT_ENABLE_PERSISTENCE = "Vuoi abilitare la persistenza dei dati? (s/n): ";
    private static final String PROMPT_PERSISTENCE_TYPE   = "Scegli il tipo di persistenza: [1] Database, [2] File System: ";

    @Override public void start(Stage stage) {
        NavigationManager.switchScene(stage, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        logger.info("Avvio FoodCare");

        /*Istanzio la Sessione*/
        Session s = Session.getInstance();

        /*Scegli tra CLI o FXML*/
        boolean useCli = askYesNo(scanner, PROMPT_USE_CLI);

        /*Scelta persistenza*/
        boolean enablePersistence = askYesNo(scanner, PROMPT_ENABLE_PERSISTENCE);


        if (enablePersistence) {
            String type = askChoice(scanner);
            if ("1".equals(type)) {
                s.setDB(true);
                s.setRam(false);
                logger.info("Modalità persistente: Database.");
            } else {
                s.setDB(false);
                s.setRam(false);
                s.setFs(true);
                logger.info("Modalità persistente: FileSystem.(solo availability)");
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
            logger.info("Inserimento non valido. Inserisci s/n");
        }
    }

    private static String askChoice(Scanner scanner) {
        while (true) {
            logger.info(PROMPT_PERSISTENCE_TYPE);
            String in = scanner.nextLine().trim().toLowerCase();
            if (in.equals("1") || in.equals("2")) return in;
            logger.info("Inserimento non valido. Inserisci 1/2" );
        }
    }
}
