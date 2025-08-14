package ispw.foodcare.bean;

import ispw.foodcare.Role;

public class UserBean {

    private String name;
    private String surname;
    private String phoneNumber;
    private String username;
    private String email;
    private String password;
    private Role role;

    public UserBean() { /* Costruttore vuoto */ }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto.");
        }
        if (!name.matches("[a-zA-ZÀ-ž\\s]+")) {
            throw new IllegalArgumentException("Il nome può contenere solo lettere.");
        }
        this.name = name.trim();
    }

    public String getSurname() { return surname; }
    public void setSurname(String surname) {
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("Il cognome non può essere vuoto.");
        }
        if (!surname.matches("[a-zA-ZÀ-ž\\s]+")) {
            throw new IllegalArgumentException("Il cognome può contenere solo lettere.");
        }
        this.surname = surname.trim();
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{1,10}")) {
            throw new IllegalArgumentException("Il numero di telefono deve contenere solo cifre (max 10).");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("L'username non può essere vuoto.");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("L'username deve avere almeno 3 caratteri.");
        }
        this.username = username.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !email.matches("[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email non valida.");
        }
        this.email = email.trim();
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.length() < 3) {
            throw new IllegalArgumentException("La password deve avere almeno 3 caratteri.");
        }
        this.password = password;
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role;}
}
