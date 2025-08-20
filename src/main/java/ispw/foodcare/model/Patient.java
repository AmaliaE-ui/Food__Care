package ispw.foodcare.model;

import ispw.foodcare.Role;
import java.time.LocalDate;

public class Patient extends User {

    private LocalDate birthDate;
    private String gender;

    public record Credentials(String username, String password) {}
    public record Anagraphic(String name, String surname, String email, String phone) {}
    public record PatientProfile(LocalDate birthDate, String gender) {}

    public Patient(Credentials c, Anagraphic a, Role role,PatientProfile p) {
        super(c.username, c.password, a.name, a.surname, a.email, a.phone, role);
        this.birthDate = p.birthDate;
        this.gender = p.gender;
    }

     //Getter e Setter
     public LocalDate getBirthDate() { return birthDate; }

     public String getGender() { return gender; }
 }

