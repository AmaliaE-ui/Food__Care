package ispw.foodcare.cli;

import ispw.foodcare.exception.AccountAlreadyExistsException;
import java.util.Scanner;

public class InitializeCli {

    Scanner scanner = new Scanner(System.in);
    private RegistrationCli regCli = new RegistrationCli();
    private LoginCli logCli=new LoginCli();

    public void initialize() throws AccountAlreadyExistsException {

        while (true) {
            System.out.println("\n--- Benvenuto in FoodCare CLI ---");
            System.out.println("[1] Login");
            System.out.println("[2] Registrazione");
            System.out.println("[3] Esci");
            System.out.print("Seleziona un'opzione: ");

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1" -> logCli.login();
                case "2" -> regCli.register();
                case "3" -> {
                    System.out.println("Uscita...");
                    return;
                }
                default -> System.out.println("Scelta non valida.");
            }
        }
    }


}


