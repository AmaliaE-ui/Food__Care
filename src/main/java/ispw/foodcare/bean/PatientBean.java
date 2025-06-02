package ispw.foodcare.bean;

public class PatientBean extends UserBean{

    private String birthDate;
    private String gender;

    public PatientBean() {
        // Costruttore vuoto
    }

    // Getter & Setter
    public String getBirthDate() {return birthDate;}
    public String getGender() {return gender;}

    public void setBirthDate(String birthDate) {this.birthDate = birthDate;}
    public void setGender(String gender) {this.gender = gender;}
}

