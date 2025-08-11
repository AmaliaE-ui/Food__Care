package ispw.foodcare.bean;

import java.time.LocalDate;
import java.time.LocalTime;
import ispw.foodcare.utils.ShowAlert;

public class AvailabilityBean {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String nutritionistUsername;

    private LocalDate today = LocalDate.now();

    // Costruttore vuoto
    public AvailabilityBean() {}


    // Getter e Setter
    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) {
        if(date == null){
            throw new IllegalArgumentException("La data non può essere nulla.");
        }
        if(date.isAfter(today)) {
            this.date = date;

        }else{
            throw new IllegalArgumentException("Non puoi prenotare in una data passata.");}
    }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("L'orario di inizio non può essere nullo.");
        }
        this.startTime = startTime;
    }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("L'orario di fine non può essere nullo.");
        }
        if (this.startTime != null && endTime.isBefore(this.startTime)) {
            throw new IllegalArgumentException("L'orario di fine non può essere precedente all'inizio.");
        }
        this.endTime = endTime;
    }

    public String getNutritionistUsername() { return nutritionistUsername; }
    public void setNutritionistUsername(String nutritionistUsername) {
        if (nutritionistUsername == null || nutritionistUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Lo username del nutrizionista non può essere vuoto.");
        }
        this.nutritionistUsername = nutritionistUsername.trim();
    }

}
