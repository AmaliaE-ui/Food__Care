package ispw.foodcare.model;

import ispw.foodcare.Role;

public class Patient extends User {

     private String birthDate;
     private String gender;

     public Patient(String username, String password, String name, String surname, String email,
                    String phoneNumber, Role role, String birthDate, String gender) {
         super(username, password, name, surname, email, phoneNumber, role);
         this.birthDate = birthDate;
         this.gender = gender;
     }

     //Getter e Setter
     public String getBirthDate() { return birthDate; }
     public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

     public String getGender() { return gender; }
     public void setGender(String gender) { this.gender = gender; }
 }

