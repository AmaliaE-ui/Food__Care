package ispw.foodcare.cli;


import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.AppointmentController;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class AppointmentsNutritionistCli {
    private final Scanner scanner;
    private final UserBean currentUser;
    private final AppointmentController controller = new AppointmentController();

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentsNutritionistCli(Scanner scanner, UserBean currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void show() {
        // reset del badge
        controller.markAppointmentsAsViewedForNutritionist(currentUser.getUsername());

        while (true) {
            System.out.println("\n=== Appuntamenti prenotati ===");
            List<AppointmentBean> appointments =
                    controller.getAppointmentsForNutritionist(currentUser.getUsername());

            if (appointments == null || appointments.isEmpty()) {
                System.out.println("Nessun appuntamento presente.");
            } else {
                printTable(appointments);
            }

            System.out.println("\n[0] Aggiorna \n[1] Indietro");
            System.out.print("Seleziona un'opzione:: ");
            String line = scanner.nextLine().trim();

            if (line.equals("1")) {
                return; // torna alla HomeNutritionistCli
            }
            if (line.equalsIgnoreCase("0")) {
                // ricarica il loop
                continue;
            }
            System.out.println("Comando non riconosciuto.");
        }
    }

    private void printTable(List<AppointmentBean> list) {
        System.out.printf("%-4s | %-15s | %-15s | %-12s | %-6s | %-30s%n",
                "#", "Nome", "Cognome", "Data", "Ora", "Note");
        System.out.println("-----------------------------------------------------------------------------------------");
        int i = 1;
        for (AppointmentBean a : list) {
            String name = safe(a.getPatientName());
            String surname = safe(a.getPatientSurname());
            String date = a.getDate() != null ? a.getDate().format(DATE_FMT) : "";
            String time = a.getTime() != null ? a.getTime().format(TIME_FMT) : "";
            String notes = safe(a.getNotes());

            // tronco le note per non sballare la tabella
            if (notes.length() > 28) notes = notes.substring(0, 27) + "…";

            System.out.printf("%-4d | %-15s | %-15s | %-12s | %-6s | %-30s%n",
                    i++, name, surname, date, time, notes);
        }
    }

    private void showDetails(AppointmentBean a) {
        System.out.println("\n--- Dettagli appuntamento ---");
        System.out.println("Paziente    : " + safe(a.getPatientName()) + " " + safe(a.getPatientSurname()));
        System.out.println("Data        : " + (a.getDate() != null ? a.getDate().format(DATE_FMT) : ""));
        System.out.println("Ora         : " + (a.getTime() != null ? a.getTime().format(TIME_FMT) : ""));
        System.out.println("Note        : " + safe(a.getNotes()));
        System.out.println("------------------------------");
        System.out.print("Premi Invio per continuare...");
        scanner.nextLine();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
