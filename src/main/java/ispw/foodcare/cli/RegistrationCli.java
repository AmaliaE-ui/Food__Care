package ispw.foodcare.cli;

import ispw.foodcare.Role;
import ispw.foodcare.bean.*;
import ispw.foodcare.controller.applicationcontroller.RegistrationController;
import java.time.LocalDate;
import java.util.Scanner;

public class RegistrationCli {

    private final RegistrationController registrationController;

    public RegistrationCli() {
        var s = ispw.foodcare.model.Session.getInstance();
        this.registrationController = new RegistrationController(s.getUserDAO());
    }

    Scanner scanner = new Scanner(System.in);

    public void register() {

        System.out.println("Registrati come:");
        System.out.println("[1] Paziente");
        System.out.println("[2] Nutrizionista");
        System.out.print("Seleziona un'opzione: ");
        String tipo = scanner.nextLine();

        switch (tipo) {
            case "1" -> registraPaziente();
            case "2" -> registraNutrizionista();
            default -> System.out.println("Scelta non valida.");
        }
    }

    public void registraPaziente() {
        PatientBean bean = new PatientBean();

        promptValid(scanner, "Nome", bean::setName);
        promptValid(scanner, "Cognome", bean::setSurname);
        promptValid(scanner, "Telefono", bean::setPhoneNumber);
        promptValid(scanner, "Username", bean::setUsername);
        promptValid(scanner, "Email", bean::setEmail);
        promptValid(scanner,"Password", bean::setPassword);
        promptDateValid(scanner, "Data di nascita (YYYY-MM-DD)", bean::setBirthDate);
        bean.setGender(prompt(scanner, "Genere"));
        bean.setRole(Role.PATIENT);

        boolean result = registrationController.registrationPatient(bean);
        System.out.println(result ? "✅ Registrazione completata!" : "❌ Errore nella registrazione.");
    }

    public void registraNutrizionista() {
        NutritionistBean bean = new NutritionistBean();
        AddressBean address = new AddressBean();

        promptValid(scanner, "Nome", bean::setName);
        promptValid(scanner, "Cognome", bean::setSurname);
        promptValid(scanner, "Telefono", bean::setPhoneNumber);
        promptValid(scanner, "Username", bean::setUsername);
        promptValid(scanner, "Email", bean::setEmail);
        promptValid(scanner,"Password", bean::setPassword);
        bean.setRole(Role.NUTRITIONIST);
        promptValid(scanner, "P.IVA", bean::setPiva);
        bean.setTitoloStudio(prompt(scanner, "Titolo di studio"));
        bean.setSpecializzazione(prompt(scanner, "Specializzazione"));

        System.out.print("Inserisci indirizzo: ");
        promptValid(scanner, "Via", address::setVia);
        promptValid(scanner, "Civico", address::setCivico);
        promptValid(scanner, "CAP", address::setCap);
        promptValid(scanner, "Città", address::setCitta);
        promptValid(scanner, "Provincia", address::setProvincia);
        promptValid(scanner, "Regione", address::setRegione);

        bean.setAddress(address);

        boolean result = registrationController.registrationNutritionist(bean);
        System.out.println(result ? "✅ Registrazione completata!" : "❌ Errore nella registrazione.");
    }

    /*Metodi di supporto
    * Metodo senza validazione*/
    private String prompt(Scanner sc, String label) {
        System.out.print(label + ": ");
        return sc.nextLine();
    }

    /*Metodo per setter che fanno validazione sintattica*/
    private String promptValid(Scanner sc, String label, java.util.function.Consumer<String> setter) {
        while (true) {
            String s = prompt(sc, label);
            try {
                setter.accept(s);
                return s;           // ok: esco
            } catch (IllegalArgumentException ex) {
                System.out.println("❌ " + ex.getMessage() + " (riprovare)");
            }
        }
    }

    /*Metodo per date*/
    private LocalDate promptDateValid(Scanner sc, String label, java.util.function.Consumer<LocalDate> setter) {
        while (true) {
            String s = prompt(sc, label);
            try {
                LocalDate d = LocalDate.parse(s.trim());
                setter.accept(d);
                return d;                                // tutto ok → esco
            } catch (java.time.format.DateTimeParseException ex) {
                System.out.println("❌ Data non valida. Formato richiesto: YYYY-MM-DD (riprovare)");
            } catch (IllegalArgumentException ex) {
                System.out.println("❌ " + ex.getMessage() + " (riprovare)");
            }
        }
    }
}


