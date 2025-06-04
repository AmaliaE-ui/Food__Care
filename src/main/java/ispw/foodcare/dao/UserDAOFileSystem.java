package ispw.foodcare.dao;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.guicontroller.UserDAOInterface;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.Nutritionist;
import ispw.foodcare.model.Patient;
import ispw.foodcare.model.User;
import ispw.foodcare.model.Address;
import ispw.foodcare.Role;
import ispw.foodcare.utils.Converter;

import java.io.*;

public class UserDAOFileSystem implements UserDAOInterface {

    private static final String BASE_PATH = "src/main/java/ispw/foodcare/data/";
    private static final String USERS_FILE = BASE_PATH + "users.txt";
    private static final String PATIENTS_FILE = BASE_PATH + "patients.txt";
    private static final String NUTRITIONISTS_FILE = BASE_PATH + "nutritionist.txt";
    private static final String ADDRESSES_FILE = BASE_PATH + "address.txt";

    //Crea e salva file nella directory data
    public UserDAOFileSystem() {
        new File(BASE_PATH).mkdirs();
    }

    //Metodo salva User
    @Override
    public void saveUser(User user) throws AccountAlreadyExistsException {
        if (getUserByEmail(user.getUsername()) != null) {
            throw new AccountAlreadyExistsException("Utente già esistente con username: " + user.getUsername());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(String.join(",", user.getUsername(), user.getPassword(), user.getRole().toString(),
                    user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNumber()));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Errore nel salvataggio in users.txt", e);
        }

        if (user instanceof Patient patient) {
            savePatient(patient);
        } else if (user instanceof Nutritionist nutritionist) {
            saveNutritionist(nutritionist);
        }
    }

    //Metodo salva patient usato da saveUser
    private void savePatient(Patient patient) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENTS_FILE, true))) {
            writer.write(String.join(",", patient.getUsername(), patient.getBirthDate(), patient.getGender()));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Errore nel salvataggio in patients.txt", e);
        }
    }

    //Metodo salva nutritionist usato da saveUser
    private void saveNutritionist(Nutritionist nutritionist) {
        int addressId = saveAddress(nutritionist.getAddress());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NUTRITIONISTS_FILE, true))) {
            writer.write(String.join(",", nutritionist.getUsername(), nutritionist.getPiva(),
                    nutritionist.getTitoloStudio(), nutritionist.getSpecializzazione(), String.valueOf(addressId)));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Errore nel salvataggio in nutritionists.txt", e);
        }
    }

    private int saveAddress(Address address) {
        int id = getNextAddressId();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADDRESSES_FILE, true))) {
            writer.write(id + "," + String.join(",", address.getVia(), address.getCivico(), address.getCap(),
                    address.getCitta(), address.getProvincia(), address.getRegione()));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Errore nel salvataggio in addresses.txt", e);
        }
        return id;
    }

    private int getNextAddressId() {
        int maxId = 0;
        File file = new File(ADDRESSES_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens.length > 0) {
                        try {
                            int currentId = Integer.parseInt(tokens[0]); //il metodo Integer.parseInt() genera eccezione se la stringa non è un numero valido
                            if (currentId > maxId) {
                                maxId = currentId;
                            }
                        } catch (NumberFormatException ignored) {//Non è obbligotario usare ingnored, posso anche mettere 'e'
                            // Ignora le righe errate. Nel momento in cui la conversione da stringa a intero è andata male
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Errore nella lettura di addresses.txt", e);
            }
        }
        return maxId + 1;
    }


    //Metodo per restituire uno user dopo una ricerca tra quelli esistenti
    @Override
    public User getUserByEmail(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length != 7) {
                    System.err.println("Riga malformata in users.txt: " + line);
                    continue;
                }
                if (tokens[0].equals(username)) {
                    String password = tokens[1];
                    Role role = Role.valueOf(tokens[2]);
                    String name = tokens[3];
                    String surname = tokens[4];
                    String email = tokens[5];
                    String phone = tokens[6];

                    return switch (role) {
                        case PATIENT -> loadPatient(username, password, name, surname, email, phone);
                        case NUTRITIONIST -> loadNutritionist(username, password, name, surname, email, phone);
                        default -> new User(username, password, name, surname, email, phone, role);
                    };
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore nella lettura di users.txt", e);
        }
        return null;
    }

    //Metodo di getUserByEmail
    private Patient loadPatient(String username, String password, String name, String surname, String email, String phone) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length != 3) {
                    System.err.println("Riga malformata in patients.txt: " + line);
                    continue;
                }
                if (tokens[0].equals(username)) {
                    return new Patient(username, password, name, surname, email, phone, Role.PATIENT, tokens[1], tokens[2]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore nella lettura di patients.txt", e);
        }
        return null;
    }

    //Metodo di getUserByEmail
    private Nutritionist loadNutritionist(String username, String password, String name, String surname, String email, String phone) {
        try (BufferedReader reader = new BufferedReader(new FileReader(NUTRITIONISTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length != 5) {
                    System.err.println("Riga malformata in nutritionist.txt: " + line);
                    continue;
                }
                if (tokens[0].equals(username)) {
                    String piva = tokens[1];
                    String titoloStudio = tokens[2];
                    String specializzazione = tokens[3];
                    int addressId;
                    try {
                        addressId = Integer.parseInt(tokens[4]);
                    } catch (NumberFormatException e) {
                        System.err.println("AddressId non valido per username " + username + ": " + tokens[4]);
                        return null;
                    }
                    Address address = loadAddressById(addressId);
                    return new Nutritionist(username, password, name, surname, email, phone,
                            Role.NUTRITIONIST, piva, titoloStudio, specializzazione, address);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore nella lettura di nutritionists.txt", e);
        }
        return null;
    }

    //Metodo di LoadNutritionist
    private Address loadAddressById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADDRESSES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length != 7) {
                    System.err.println("Riga malformata in address.txt: " + line);
                    continue;
                }
                try {
                    if (Integer.parseInt(tokens[0]) == id) {
                        return new Address(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6]);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("ID non numerico in address.txt: " + tokens[0]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore nella lettura di addresses.txt", e);
        }
        return null;
    }

    @Override
    public UserBean checkLogin(String username, String password) {
        try(BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] tokens = line.split(",");
                if (tokens[0].equals(username)) {
                    String storedPassword = tokens[1];
                    if(storedPassword.equals(password)) {
                        User user = getUserByEmail(username);
                        return Converter.userToBean(user);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il login da file system", e);
        }
        return null;
    }

    @Override
    public void updateUserModelData(User user) {
        // Da implementare se servono modifiche nei file (in futuro)
    }

    @Override
    public User loadUserData(User user) {
        return getUserByEmail(user.getUsername());
    }
}
