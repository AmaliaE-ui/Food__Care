package ispw.foodcare.cli;

import ispw.foodcare.bean.NutritionistBean;
import java.util.Scanner;

public class NutritionistProfileCli {

    private final Scanner scanner;
    private final NutritionistBean nutritionist;

    public NutritionistProfileCli(Scanner scanner, NutritionistBean nutritionist) {
        this.scanner = scanner;
        this.nutritionist = nutritionist;
    }

    /**
     * Mostra i dati personali del nutrizionista (equivalente della view profilo in GUI)
     * e permette di tornare indietro.
     * In futuro possiamo aggiungere “Prenota appuntamento” qui.
     */
    public void show() {
        while (true) {
            System.out.println("\n=== Profilo Nutrizionista ===");
            System.out.println("Nome:            " + safe(nutritionist.getName()));
            System.out.println("Cognome:         " + safe(nutritionist.getSurname()));
            System.out.println("Email:           " + safe(nutritionist.getEmail()));
            System.out.println("Telefono:        " + safe(nutritionist.getPhoneNumber()));
            System.out.println("Specializzazione:" + safe(nutritionist.getSpecializzazione()));

            String address = "N/D";
            if (nutritionist.getAddress() != null) {
                var a = nutritionist.getAddress();
                address = String.format("%s %s, %s (%s) - %s",
                        safe(a.getVia()), safe(a.getCivico()), safe(a.getCitta()), safe(a.getProvincia()), safe(a.getRegione()));
            }
            System.out.println("Indirizzo studio: " + address);

            System.out.println("\n[0] Prenota appuntamento");
            System.out.println("[1] Torna indietro");
            System.out.print("Seleziona un’opzione: ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("0")) {
                new BookAppointmentCli(scanner, nutritionist).show();
            }
            if (choice.equals("1")) {
                return; // back alla lista
            } else {
                System.out.println("Opzione non valida. Riprova.");
            }
        }
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
