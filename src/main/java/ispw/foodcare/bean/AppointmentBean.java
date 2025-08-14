package ispw.foodcare.bean;

import ispw.foodcare.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentBean {

    private LocalDate date;
    private LocalTime time;
    private String notes;
    private String nutritionistUsername;
    private String patientUsername;
    private String patientName;
    private String patientSurname;
    private AppointmentStatus status;

    public AppointmentBean() { /* Costruttore vuoto */ }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La data è obbligatoria.");
        }
        this.date = date;
    }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("L'orario è obbligatorio.");
        }
        this.time = time;
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) {
        if (notes != null && notes.length() > 1000) {
            throw new IllegalArgumentException("Le note non possono superare 1000 caratteri.");
        }
        this.notes = (notes == null ? "" : notes.trim());
    }

    public String getNutritionistUsername() { return nutritionistUsername; }
    public void setNutritionistUsername(String nutritionistUsername) {
        if (nutritionistUsername == null || nutritionistUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Username nutrizionista obbligatorio.");
        }
        // Controllo che sia solo lettere/numeri
        for (char c : nutritionistUsername.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                throw new IllegalArgumentException("Username nutrizionista non valido: solo lettere e numeri.");
            }
        }
        this.nutritionistUsername = nutritionistUsername.trim();
    }

    public String getPatientUsername() { return patientUsername; }
    public void setPatientUsername(String patientUsername) {
        if (patientUsername == null || patientUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Username paziente obbligatorio.");
        }
        for (char c : patientUsername.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                throw new IllegalArgumentException("Username paziente non valido: solo lettere e numeri.");
            }
        }
        this.patientUsername = patientUsername.trim();
    }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) {
        if (patientName == null || patientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome paziente obbligatorio.");
        }
        for (char c : patientName.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                throw new IllegalArgumentException("Nome paziente non valido: solo lettere e spazi.");
            }
        }
        this.patientName = patientName.trim();
    }

    public String getPatientSurname() { return patientSurname; }
    public void setPatientSurname(String patientSurname) {
        if (patientSurname == null || patientSurname.trim().isEmpty()) {
            throw new IllegalArgumentException("Cognome paziente obbligatorio.");
        }
        for (char c : patientSurname.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                throw new IllegalArgumentException("Cognome paziente non valido: solo lettere e spazi.");
            }
        }
        this.patientSurname = patientSurname.trim();
    }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Lo stato dell'appuntamento è obbligatorio.");
        }
        this.status = status;
    }
}
