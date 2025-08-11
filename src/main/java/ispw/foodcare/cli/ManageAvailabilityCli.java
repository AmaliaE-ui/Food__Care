package ispw.foodcare.cli;

import ispw.foodcare.bean.AvailabilityBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ManageAvailabilityCli {

    private final UserBean currentUser;
    private final Scanner scanner;
    private final BookAppointmentController controller = new BookAppointmentController();
    private final List<AvailabilityBean> availabilityList = new ArrayList<>();
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ManageAvailabilityCli(Scanner scanner, UserBean currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    /** Entry point CLI */
    public void show() throws AccountAlreadyExistsException {
        // pulizia disponibilità scadute come in initialize()
        controller.deleteExpiredAvailabilities();
        loadAvailabilities();

        while (true) {
            System.out.println("\n=== Gestisci Disponibilità ===");
            printTable();

            System.out.println("""
                    \n[1] Aggiungi disponibilità
                    [2] Elimina disponibilità
                    [3] Refresh
                    [0] Indietro""");
            System.out.print("Seleziona un'opzione: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleAddAvailability();
                case "2" -> handleDeleteAvailability();
                case "3" -> {
                    controller.deleteExpiredAvailabilities();
                    loadAvailabilities();
                    System.out.println("↻ Aggiornato.");
                }
                case "0" ->  new HomeNutritionistCli(currentUser).show();
                default -> System.out.println("Opzione non valida.");
            }
        }
    }

    private void loadAvailabilities() {
        availabilityList.clear();
        String username = Session.getInstance().getCurrentUser().getUsername();
        List<AvailabilityBean> loaded = new ArrayList<>(controller.getAvailabilitiesForNutritionist(username));
        // come in GUI: filtra passato rispetto a oggi
        loaded.removeIf(b -> b.getDate().isBefore(LocalDate.now()));
        availabilityList.addAll(loaded);
        sortAvailabilities();
    }

    private void sortAvailabilities() {
        availabilityList.sort(Comparator
                .comparing(AvailabilityBean::getDate)
                .thenComparing(AvailabilityBean::getStartTime));
    }

    private void printTable() {
        if (availabilityList.isEmpty()) {
            System.out.println("(Nessuna disponibilità futura)");
            return;
        }
        System.out.printf("%-4s | %-12s | %-6s | %-6s%n", "#", "Data", "Inizio", "Fine");
        System.out.println("--------------------------------------");
        int i = 1;
        for (AvailabilityBean b : availabilityList) {
            System.out.printf("%-4d | %-12s | %-6s | %-6s%n",
                    i++,
                    b.getDate().format(DMY),
                    b.getStartTime(),
                    b.getEndTime());
        }
    }

    private void handleAddAvailability() {
        LocalDate date = askDate();
        if (date == null) return;

        // stessa business rule della GUI: solo giorni feriali
        if (!BookAppointmentController.isWeekday(date)) {
            System.out.println("✖ Data non valida: inserire solo giorni lavorativi (lun-ven).");
            return;
        }

        // slot fissi
        List<LocalTime> fixed = BookAppointmentController.generateFixedSlots();
        if (fixed.isEmpty()) {
            System.out.println("✖ Nessuno slot disponibile (configurazione).");
            return;
        }

        List<LocalTime> chosen = askSlots(fixed);
        if (chosen.isEmpty()) {
            System.out.println("Nessuno slot selezionato.");
            return;
        }

        String username = Session.getInstance().getCurrentUser().getUsername();
        int inserted = 0;

        for (LocalTime slot : chosen) {
            // evita duplicati come in GUI
            boolean exists = availabilityList.stream()
                    .anyMatch(a -> a.getDate().equals(date) && a.getStartTime().equals(slot));
            if (exists) {
                System.out.println("• Skip " + slot + " (già presente).");
                continue;
            }

            try {
                AvailabilityBean bean = new AvailabilityBean();
                bean.setDate(date); // può lanciare IllegalArgumentException (date nel passato)
                bean.setStartTime(slot);
                bean.setEndTime(slot.plusMinutes(45)); // stessa durata GUI
                bean.setNutritionistUsername(username);

                controller.addAvailability(bean);
                availabilityList.add(bean);
                inserted++;
            } catch (IllegalArgumentException iae) {
                System.out.println("✖ Errore di validazione: " + iae.getMessage());
            } catch (Exception ex) {
                System.out.println("✖ Errore salvataggio: " + ex.getMessage());
            }
        }

        sortAvailabilities();
        if (inserted > 0) {
            System.out.println("✓ Disponibilità aggiunte con successo! (" + inserted + ")");
        }
    }

    private void handleDeleteAvailability() {
        if (availabilityList.isEmpty()) {
            System.out.println("Niente da eliminare.");
            return;
        }
        Integer idx = askIndex(availabilityList.size(), "Seleziona la riga da eliminare (0 per annullare): ");
        if (idx == null || idx == 0) return;

        AvailabilityBean toDelete = availabilityList.get(idx - 1);
        try {
            controller.deleteAvailability(toDelete);
            availabilityList.remove(idx - 1);
            System.out.println("✓ Disponibilità rimossa.");
        } catch (Exception e) {
            System.out.println("✖ Errore eliminazione: " + e.getMessage());
        }
    }


    private LocalDate askDate() {
        System.out.print("Inserisci la data (dd/MM/yyyy) oppure invio per annullare: ");
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            LocalDate d = LocalDate.parse(s, DMY);
            return d;
        } catch (DateTimeParseException e) {
            System.out.println("Formato data non valido.");
            return null;
        }
    }

    private List<LocalTime> askSlots(List<LocalTime> fixed) {
        // stampa elenco numerato
        System.out.println("Seleziona uno o più slot (es. 1,3,5) — invio per annullare:");
        for (int i = 0; i < fixed.size(); i++) {
            System.out.printf("%2d) %s%n", i + 1, fixed.get(i));
        }
        System.out.print("Scelta: ");
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) return Collections.emptyList();

        try {
            Set<Integer> idxs = Arrays.stream(line.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            List<LocalTime> chosen = new ArrayList<>();
            for (Integer i : idxs) {
                if (i >= 1 && i <= fixed.size()) {
                    chosen.add(fixed.get(i - 1));
                } else {
                    System.out.println("Indice fuori range: " + i);
                }
            }
            return chosen;
        } catch (NumberFormatException e) {
            System.out.println("Input non valido.");
            return Collections.emptyList();
        }
    }

    private Integer askIndex(int max, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return null;
            try {
                int v = Integer.parseInt(line);
                if (v >= 0 && v <= max) return v;
                System.out.println("Valore non valido (0.." + max + ").");
            } catch (NumberFormatException e) {
                System.out.println("Input non valido.");
            }
        }
    }
}
