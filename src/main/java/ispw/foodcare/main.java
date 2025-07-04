package ispw.foodcare;

import ispw.foodcare.dao.AppointmentDAO;
import ispw.foodcare.dao.AvailabilityDAO;
import ispw.foodcare.dao.NutritionistDAO;
import ispw.foodcare.dao.UserDAO;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        //Avvio scena JavaFX
        NavigationManager.switchScene(stage, "/ispw/foodcare/Login/login.fxml", "FoodCare - Login");
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Avvio FoodCare");

        //Selezione la modaltà: Demo (N persistenza) / Non-Demo (S persistenza)
        System.out.println("Vuoi abilitare la persistenza dei dati? (S/N) ");
        String enablePersistance = scanner.nextLine().trim().toLowerCase();

        if(enablePersistance.equals("s")){

            Session.getInstance().setRam(false); //Persistenza, Salvo DB o FileSystem
            System.out.println("Scegli il tipo di persistenza: [1] Database, [2] File System");
            String type = scanner.nextLine().trim();

            if(type.equals("1")){
                Session.getInstance().setDB(true); //Salvo in DB
                Session.getInstance().setUserDAO(new UserDAO()); //UserDAO unificato
                Session.getInstance().setNutritionistDAO(new NutritionistDAO());
                Session.getInstance().setAvailabilityDAO(new AvailabilityDAO());
                Session.getInstance().setAppointmentDAO(new AppointmentDAO());

            } else if(type.equals("2")){
                Session.getInstance().setDB(false); //Salvo in File

            } else {
                System.out.println("Inserimento non valido");
                return; //Uscita dal main
            }
        } else if(enablePersistance.equals("n")) {
            Session.getInstance().setRam(true);
            Session.getInstance().setUserDAO(new UserDAO());
            Session.getInstance().setNutritionistDAO(new NutritionistDAO());
            Session.getInstance().setAvailabilityDAO(new AvailabilityDAO());
            Session.getInstance().setAppointmentDAO(new AppointmentDAO());
        }else {
            System.out.println("Inserimento non valido");
            return;
        }

        //Avvia JavaFX
        System.out.println("Avvio modalità grafica ...");
        Session.getInstance().setCLI(false);
        launch();
    }
}
