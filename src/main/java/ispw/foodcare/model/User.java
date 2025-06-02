package ispw.foodcare.model;

import ispw.foodcare.Role;

public class User {
    private final String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private Role role;

    //Costruttore per assegnare il ruolo
    public User(String username, String password, String name, String surname, String email, String phoneNumber, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;   //Patient || Nutritionist
    }

    // Getter e Setter
    public String getUsername() { return username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
