package ispw.foodcare.bean;

import java.time.LocalDate;

public class PatientBean extends UserBean {

    private LocalDate birthDate;
    private String gender;

    // Costruttore vuoto
    public PatientBean() {}

    // Getter e Setter
    public LocalDate getBirthDate() { return birthDate; }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La data di nascita non può essere nulla.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La data di nascita non può essere nel futuro.");
        }
        this.birthDate = birthDate;
    }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }
}
