package ispw.foodcare.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Availability {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String nutritionistUsername;

    // Getter e Setter
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) {this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) {this.endTime = endTime; }

    public String getNutritionistUsername() { return nutritionistUsername; }
    public void setNutritionistUsername(String nutritionistUsername) {this.nutritionistUsername = nutritionistUsername; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability that)) return false;
        return Objects.equals(nutritionistUsername, that.nutritionistUsername)
                && Objects.equals(date, that.date)
                && Objects.equals(startTime, that.startTime)
                && Objects.equals(endTime, that.endTime);
    }

    @Override public int hashCode() {
        return Objects.hash(nutritionistUsername, date, startTime, endTime);
    }
}
