package ispw.foodcare.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Availability {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String nutritionistUsername;

    // Getter e Setter
    public LocalDate getDate() { return date; }
    public void setData(LocalDate date) {this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) {this.endTime = endTime; }

    public String getNutritionistUsername() { return nutritionistUsername; }
    public void setNutritionistUsername(String nutritionistUsername) {this.nutritionistUsername = nutritionistUsername; }


}
