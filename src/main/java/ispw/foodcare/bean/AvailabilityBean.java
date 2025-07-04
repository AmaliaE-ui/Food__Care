package ispw.foodcare.bean;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilityBean {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String nutritionistUsername;

    // Costruttore vuoto
    public AvailabilityBean() {}


    // Getter e Setter
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getNutritionistUsername() { return nutritionistUsername; }
    public void setNutritionistUsername(String nutritionistUsername) { this.nutritionistUsername = nutritionistUsername; }
}
