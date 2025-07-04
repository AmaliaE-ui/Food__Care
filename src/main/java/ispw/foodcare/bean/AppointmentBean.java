package ispw.foodcare.bean;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentBean {

    private LocalDate date;
    private LocalTime time;
    private String notes;
    private String nutritionistUsername;

    // Costruttore vuoto
    public AppointmentBean() {}

    // Getter e Setter
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getNutritionistUsername() { return nutritionistUsername; }
    public void setNutritionistUsername(String nutritionistUsername) { this.nutritionistUsername = nutritionistUsername;}

}
