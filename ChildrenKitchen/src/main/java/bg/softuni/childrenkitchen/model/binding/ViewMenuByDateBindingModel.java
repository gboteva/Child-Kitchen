package bg.softuni.childrenkitchen.model.binding;

import java.time.LocalDate;

public class ViewMenuByDateBindingModel {
    private LocalDate date;
    private String ageGroup;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }
}
