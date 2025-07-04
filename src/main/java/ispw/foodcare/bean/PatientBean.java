package ispw.foodcare.bean;

public class PatientBean extends UserBean{

    private String birthDate;
    private String gender;

    // Costruttore vuoto
    public PatientBean() {}

    // Getter e Setter
    public String getBirthDate() {return birthDate;}
    public String getGender() {return gender;}

    public void setBirthDate(String birthDate) {this.birthDate = birthDate;}
    public void setGender(String gender) {this.gender = gender;}
}

