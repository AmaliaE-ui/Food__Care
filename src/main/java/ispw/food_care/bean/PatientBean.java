package ispw.food_care.bean;

public class PatientBean extends UserBean{
    private String birthDate;
    private String gender;

    public PatientBean() {
        // Costruttore vuoto
    }

    // BirthDate
    public String getBirthDate() {return birthDate;}
    public void setBirthDate(String birthDate) {this.birthDate = birthDate;}

    // Gender
    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}
}

