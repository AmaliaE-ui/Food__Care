package ispw.foodcare.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Availability {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String nutritionistUsername;

    public Availability(LocalDate date, LocalTime startTime, LocalTime endTime, String nutritionistUsername) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nutritionistUsername = nutritionistUsername;
    }

    // Getter e Setter
    public LocalDate getDate() { return date; }

    public LocalTime getStartTime() { return startTime; }

    public LocalTime getEndTime() { return endTime; }

    public String getNutritionistUsername() { return nutritionistUsername; }

}
