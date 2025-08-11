package ispw.foodcare.cli;


import ispw.foodcare.Role;
import ispw.foodcare.bean.*;
import ispw.foodcare.controller.applicationcontroller.RegistrationController;
import ispw.foodcare.exeption.AccountAlreadyExistsException;


import java.time.LocalDate;
import java.util.Scanner;

public class RegistrationCli {

    private RegistrationController regController=new RegistrationController();

    Scanner scanner = new Scanner(System.in);

    public void register() throws AccountAlreadyExistsException {

        System.out.println("Registrati come:");
        System.out.println("1. Paziente");
        System.out.println("2. Nutrizionista");
        System.out.print("Scelta: ");
        String tipo = scanner.nextLine();

        switch (tipo) {
            case "1" -> registraPaziente();
            case "2" -> registraNutrizionista();
            default -> System.out.println("Scelta non valida.");
        }
    }

    public void registraPaziente() throws AccountAlreadyExistsException {
        PatientBean bean = new PatientBean();

        System.out.print("Nome: ");
        bean.setName(scanner.nextLine());
        System.out.print("Cognome: ");
        bean.setSurname(scanner.nextLine());
        System.out.print("Telefono: ");
        bean.setPhoneNumber(scanner.nextLine());
        System.out.print("Username: ");
        bean.setUsername(scanner.nextLine());
        System.out.print("Email: ");
        bean.setEmail(scanner.nextLine());
        System.out.print("Password: ");
        bean.setPassword(scanner.nextLine());
        System.out.print("Data di nascita (YYYY-MM-DD): ");
        bean.setBirthDate(LocalDate.parse(scanner.nextLine()));
        System.out.print("Genere: ");
        bean.setGender(scanner.nextLine());

        bean.setRole(Role.PATIENT);

        boolean result = regController.registrationPatient(bean);
        System.out.println(result ? "✅ Registrazione completata!" : "❌ Errore nella registrazione.");
    }

    public void registraNutrizionista() throws AccountAlreadyExistsException {
        NutritionistBean bean = new NutritionistBean();
        AddressBean address = new AddressBean();

        System.out.print("Nome: ");
        bean.setName(scanner.nextLine());
        System.out.print("Cognome: ");
        bean.setSurname(scanner.nextLine());
        System.out.print("Telefono: ");
        bean.setPhoneNumber(scanner.nextLine());
        System.out.print("Username: ");
        bean.setUsername(scanner.nextLine());
        System.out.print("Email: ");
        bean.setEmail(scanner.nextLine());
        System.out.print("Password: ");
        bean.setPassword(scanner.nextLine());
        bean.setRole(Role.NUTRITIONIST);

        System.out.print("P.IVA: ");
        bean.setPiva(scanner.nextLine());
        System.out.print("Titolo di studio: ");
        bean.setTitoloStudio(scanner.nextLine());
        System.out.print("Specializzazione: ");
        bean.setSpecializzazione(scanner.nextLine());

        System.out.print("Via: ");
        address.setVia(scanner.nextLine());
        System.out.print("Civico: ");
        address.setCivico(scanner.nextLine());
        System.out.print("CAP: ");
        address.setCap(scanner.nextLine());
        System.out.print("Città: ");
        address.setCitta(scanner.nextLine());
        System.out.print("Provincia: ");
        address.setProvincia(scanner.nextLine());
        System.out.print("Regione: ");
        address.setRegione(scanner.nextLine());

        bean.setAddress(address);

        boolean result = regController.registrationNutritionist(bean);
        System.out.println(result ? "✅ Registrazione completata!" : "❌ Errore nella registrazione.");
    }

}


