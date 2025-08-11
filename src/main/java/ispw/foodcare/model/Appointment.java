package ispw.foodcare.model;

import ispw.foodcare.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private String patientUsername;
    private String nutritionistUsername;
    private LocalDate date;
    private LocalTime time;
    private String note;
    private AppointmentStatus status;//Valori CONFIRMED/CANCELLED
    private String patientName;

    public Appointment(String patientUsername, String nutritionistUsername, LocalDate date, LocalTime time, String note) {
        this.patientUsername = patientUsername;
        this.nutritionistUsername = nutritionistUsername;
        this.date = date;
        this.time = time;
        this.note = note;
        this.status = AppointmentStatus.CONFIRMED; //Appuntamento auto-confermato
    }

    //Getter e setter
    public String getPatientUsername() {return patientUsername;}

    public String getNutritionistUsername() {return nutritionistUsername;}

    public LocalDate getDate() {return date;}

    public LocalTime getTime() {return time;}
    public void setTime(LocalTime time) {this.time = time;}

    public String getNotes() {return note;}

    public AppointmentStatus getStatus() {return status;}
    public void setStatus(AppointmentStatus status) {this.status = status;}

    public void setPatientName(String patientName) {this.patientName = patientName;};
}
