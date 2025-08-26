package ispw.foodcare.cli;

import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppointmentsPatientCli {

    private final Scanner scanner;
    private final UserBean currentUser;
    private final BookAppointmentController controller = new BookAppointmentController();

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentsPatientCli(Scanner scanner, UserBean currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void show() {
        while (true) {
            List<AppointmentBean> list = loadAppointments();

            if (list.isEmpty()) {
                System.out.println("\nNessun appuntamento prenotato.");
                System.out.print("Premi ENTER per tornare alla Home…");
                scanner.nextLine();
                return; // torna alla Home
            }

            printTable(list);

            System.out.print(
                    "\nSeleziona un appuntamento da cancellare (1-" + list.size() + ") " +
                            "\n[0] Aggiorna \n[a] Indietro" +
                            "\nSeleziona un'opzione: "
            );
            String in = scanner.nextLine().trim().toLowerCase();

            // indietro
            if ("a".equals(in)) {
                return;
            }

            // 0 = aggiorna: non fa nulla, rientra nel while
            if (!"0".equals(in)) {
                Integer idx = parseIndex(in, list.size());
                if (idx == null) {
                    System.out.println("Input non valido. Riprova.");
                } else {
                    AppointmentBean selected = list.get(idx - 1);
                    if (confirmDeletion(selected)) {
                        try {
                            controller.deleteAppointment(selected);
                            System.out.println("✔ Appuntamento eliminato con successo.");
                        } catch (Exception e) {
                            System.out.println("✖ Errore durante l'eliminazione: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }


    private List<AppointmentBean> loadAppointments() {
        List<AppointmentBean> raw = controller.getPatientAppointments(currentUser.getUsername());
        return new ArrayList<>(raw);
    }

    private void printTable(List<AppointmentBean> list) {
        System.out.println("\n=== Appuntamenti prenotati ===");
        System.out.printf("%-4s | %-10s | %-5s | %-18s | %-10s | %s%n",
                "#", "Data", "Ora", "Nutritionist", "Stato", "Note");
        System.out.println("--------------------------------------------------------------------------");
        int i = 1;
        for (AppointmentBean a : list) {
            String date = a.getDate().format(DATE_FMT);
            String time = a.getTime().format(TIME_FMT);
            String nutr = safe(a.getNutritionistUsername());
            String status = safe(a.getStatus().name());/*.name() - Converto Enum in stringa*/
            String notes = safe(a.getNotes());
            System.out.printf("%-4d | %-10s | %-5s | %-18s | %-10s | %s%n",
                    i++, date, time, nutr, status, notes);
        }
    }

    private Integer parseIndex(String in, int maxSize) {
        try {
            int idx = Integer.parseInt(in);
            if (idx >= 1 && idx <= maxSize) return idx;
            return null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean confirmDeletion(AppointmentBean a) {
        System.out.printf("Confermi l’eliminazione dell’appuntamento del %s alle %s? (S/N): ",
                a.getDate().format(DATE_FMT), a.getTime().format(TIME_FMT));
        String c = scanner.nextLine().trim().toLowerCase();
        return c.equals("s") || c.equals("si") || c.equals("sì");
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
