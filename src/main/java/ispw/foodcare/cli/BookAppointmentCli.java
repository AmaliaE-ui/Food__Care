package ispw.foodcare.cli;

import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.bean.AvailabilityBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class BookAppointmentCli {

    private final Scanner scanner;
    private final NutritionistBean nutritionist;
    private final BookAppointmentController controller;

    public BookAppointmentCli(Scanner scanner, NutritionistBean nutritionist) {
        this.scanner = scanner;
        this.nutritionist = nutritionist;
        this.controller = new BookAppointmentController();
    }

    public void show() {
        while (true) {
            System.out.println("\n=== Prenotazione appuntamento con " + nutritionist.getName() + " " + nutritionist.getSurname() + " ===");

            // 1. Recupera tutte le disponibilità
            List<AvailabilityBean> availabilities = controller.getAvailabilitiesForNutritionist(nutritionist.getUsername());
            if (availabilities.isEmpty()) {
                System.out.println("⚠ Nessuna disponibilità trovata per questo nutrizionista.");
                return; // Torna indietro
            }

            // 2. Filtra date passate
            availabilities = availabilities.stream()
                    .filter(a -> !a.getDate().isBefore(LocalDate.now()))
                    .collect(Collectors.toList());

            // 3. Rimuovi slot già prenotati
            availabilities = availabilities.stream()
                    .filter(a -> {
                        List<LocalTime> bookedTimes = controller.getBookedTimes(nutritionist.getUsername(), a.getDate());
                        return !bookedTimes.contains(a.getStartTime());
                    })
                    .collect(Collectors.toList());

            if (availabilities.isEmpty()) {
                System.out.println("⚠ Tutti gli slot futuri sono già prenotati.");
                return;
            }

            // 4. Mostra slot disponibili numerati
            printAvailabilities(availabilities);

            // 5. Scelta slot
            Integer choice = askForIndexOrBack(availabilities.size());
            if (choice == null) {
                return; // back
            }

            AvailabilityBean selected = availabilities.get(choice - 1);

            // 6. Inserimento nota opzionale
            System.out.print("Inserisci una nota per l'appuntamento (opzionale): ");
            String note = scanner.nextLine().trim();

            // 7. Conferma prenotazione
            AppointmentBean appointment = new AppointmentBean();
            appointment.setDate(selected.getDate());
            appointment.setTime(selected.getStartTime());
            appointment.setNotes(note);
            appointment.setNutritionistUsername(nutritionist.getUsername());

            try {
                controller.bookAppointment(appointment);
                System.out.println("✅ Appuntamento prenotato correttamente!");
            } catch (RuntimeException e) {
                System.out.println("❌ Errore durante la prenotazione: " + e.getMessage());
            }

            // 8. Torna al profilo nutrizionista
            return;
        }
    }

    private void printAvailabilities(List<AvailabilityBean> availabilities) {
        System.out.println("\nSlot disponibili:");
        System.out.println("---------------------------------------------------");
        System.out.printf("%-4s | %-12s | %-5s - %-5s%n", "#", "Data", "Inizio", "Fine");
        System.out.println("---------------------------------------------------");
        int i = 1;
        for (AvailabilityBean a : availabilities) {
            System.out.printf("%-4d | %-12s | %-5s - %-5s%n",
                    i++,
                    a.getDate(),
                    a.getStartTime(),
                    a.getEndTime());
        }
        System.out.println("---------------------------------------------------");
    }

    private Integer askForIndexOrBack(int size) {
        while (true) {
            System.out.print("Seleziona uno slot (1-" + size + ") \n[0] Annulla: ");
            String line = scanner.nextLine().trim().toLowerCase();

            if (line.equals("0")) {
                return null;
            }
            try {
                int idx = Integer.parseInt(line);
                if (idx >= 1 && idx <= size) {
                    return idx;
                }
                System.out.println("Indice non valido. Riprova.");
            } catch (NumberFormatException e) {
                System.out.println("Input non valido. Riprova.");
            }
        }
    }
}
