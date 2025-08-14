package ispw.foodcare.cli;

import ispw.foodcare.bean.UserBean;

public class PersonalAreaPatientCli {

        private final UserBean user;

        public PersonalAreaPatientCli(UserBean user) {
            this.user = user;
        }

        public void show() {
            System.out.println("\n===== AREA PERSONALE PAZIENTE =====");

            System.out.println("Nome: " + user.getName());
            System.out.println("Cognome: " + user.getSurname());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Telefono: " + user.getPhoneNumber());
            System.out.println("Ruolo: " + user.getRole());

            System.out.println("\nPremi Invio per tornare al menu principale...");
            new java.util.Scanner(System.in).nextLine(); // Aspetta input
        }
    }

