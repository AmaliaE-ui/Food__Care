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
    private String patientSurname;

    public Appointment(String patientUsername, String nutritionistUsername, LocalDate date, LocalTime time, String note) {
        this.patientUsername = patientUsername;
        this.nutritionistUsername = nutritionistUsername;
        this.date = date;
        this.time = time;
        this.note = note;
        this.status = AppointmentStatus.CONFIRMED; //Appuntamento auto-confermato
    }
    public Appointment(){}

    //Getter e setter
    public String getPatientUsername() {return patientUsername;}
    public void setPatientUsername(String patientUsername) {this.patientUsername = patientUsername;}

    public String getNutritionistUsername() {return nutritionistUsername;}
    public void setNutritionistUsername(String nutritionistUsername) {this.nutritionistUsername = nutritionistUsername;}


    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public LocalTime getTime() {return time;}
    public void setTime(LocalTime time) {this.time = time;}

    public String getNotes() {return note;}
    public void setNotes(String note) {this.note = note;}

    public AppointmentStatus getStatus() {return status;}
    public void setStatus(AppointmentStatus status) {this.status = status;}

    public void setPatientName(String patientName) {this.patientName = patientName;}

    public void setPatientSurname(String surname) {this.patientSurname = surname;}
}
