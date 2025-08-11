package ispw.foodcare.cli;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.UserBean;

public class PersonalAreaNutritionistCli {

    private final UserBean currentUser;

    public PersonalAreaNutritionistCli(UserBean nutritionist) {
        this.currentUser = nutritionist;
    }

    public void show() {
        System.out.println("\n===== AREA PERSONALE NUTRIZIONISTA =====");

        if (currentUser instanceof NutritionistBean nutritionist) {
            System.out.println("Nome: " + nutritionist.getName());
            System.out.println("Cognome: " + nutritionist.getSurname());
            System.out.println("Username: " + nutritionist.getUsername());
            System.out.println("Email: " + nutritionist.getEmail());
            System.out.println("Telefono: " + nutritionist.getPhoneNumber());
            System.out.println("Specializzazione: " + nutritionist.getSpecializzazione());
        } else {
            System.out.println("Nome: null " );

        }

        System.out.println("\nPremi Invio per tornare al menu principale...");
        new java.util.Scanner(System.in).nextLine();
    }
}

