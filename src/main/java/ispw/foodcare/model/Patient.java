package ispw.foodcare.model;

import ispw.foodcare.Role;

import java.time.LocalDate;

public class Patient extends User {

     private LocalDate birthDate;
     private String gender;

     public Patient(String username, String password, String name, String surname, String email,
                    String phoneNumber, Role role, LocalDate birthDate, String gender) {
         super(username, password, name, surname, email, phoneNumber, role);
         this.birthDate = birthDate;
         this.gender = gender;
     }

     //Getter e Setter
     public LocalDate getBirthDate() { return birthDate; }
     public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

     public String getGender() { return gender; }
     public void setGender(String gender) { this.gender = gender; }
 }

