package ispw.foodcare.cli;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.exception.AccountAlreadyExistsException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SearchNutritionistCli {

    private final Scanner scanner;
    private final BookAppointmentController controller;
    private final UserBean currentUser;
    private static final String LINE = "---------------------------------------------------";

    public SearchNutritionistCli(Scanner scanner, UserBean currentUser) {
        this.scanner = scanner;
        this.controller = new BookAppointmentController();
        this.currentUser=currentUser;
    }

    public void show() throws AccountAlreadyExistsException {
        while (true) {
            System.out.println("\n=== Cerca nutrizionista per città ===");
            System.out.print("Inserisci il nome della città (oppure lascia vuoto per tornare indietro): ");
            String city = scanner.nextLine().trim();

            if (city.isEmpty()) {
                System.out.println("Torno al menu precedente.");
                return; // back
            }

            List<NutritionistBean> results = controller.searchNutritionistsByCity(city);

            if (results == null || results.isEmpty()) {
                System.out.println("⚠ Nessun nutrizionista trovato nella città \"" + city + "\".\n");
                continue; // ricomincia ciclo ricerca
            }

            printResults(results);

            Integer choice = askForIndexOrBack(results.size());
            if (choice == null) {
                // back alla ricerca (senza uscire a menu precedente)
                continue;
            }

            NutritionistBean selected = results.get(choice - 1);
            new NutritionistProfileCli(scanner, selected).show();
            // al ritorno dal profilo, si torna alla lista (ricerca corrente) per eventuale nuova selezione
        }
    }

    private void printResults(List<NutritionistBean> results) {
        System.out.println("\nRisultati trovati: " + results.size());
        System.out.println(LINE);
        System.out.printf("%-4s | %-15s | %-15s | %-15s | %-20s%n",
                "#", "Nome", "Cognome", "Città", "Specializzazione");
        System.out.println(LINE);

        int i = 1;
        for (NutritionistBean n : results) {
            String city = (n.getAddress() != null) ? n.getAddress().getCitta() : "";
            String spec = (n.getSpecializzazione() != null) ? n.getSpecializzazione() : "";
            System.out.printf("%-4d | %-15s | %-15s | %-15s | %-20s%n",
                    i++, safe(n.getName()), safe(n.getSurname()), safe(city), safe(spec));
        }
        System.out.println(LINE);
    }

    /**
     * @return indice selezionato (1..size) oppure null se l’utente ha scelto 'b' per tornare indietro
     */
    private Integer askForIndexOrBack(int size) throws AccountAlreadyExistsException {
        while (true) {

            System.out.println("\nSeleziona un nutrizionista (1-" + size + ")");
            System.out.println("[0] per nuova ricerca");
            System.out.println("[a] per tornare alla home");
            System.out.print("Seleziona un’opzione: ");
            String line = scanner.nextLine().trim().toLowerCase();

            if (line.equals("0")) {
                return null;
            }
            if(line.equals("a")){
                new HomePatientCli(currentUser).show();
            }

            try {
                int idx = Integer.parseInt(line);
                if (idx >= 1 && idx <= size) {
                    return idx;
                }
                System.out.println("Valore non valido. Inserisci un numero tra 1 e " + size + " oppure '0'.");
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Input non valido. Riprova.");
            }

        }
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
