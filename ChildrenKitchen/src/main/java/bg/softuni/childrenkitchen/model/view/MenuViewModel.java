package bg.softuni.childrenkitchen.model.view;

import java.time.LocalDate;

public class MenuViewModel {
    private String date;
    private LocalDate localDate;
    private String dayOfWeek;
    private String ageGroupName;

    private FoodViewModel soup;

    private FoodViewModel main;

    private FoodViewModel dessert;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAgeGroupName() {
        return ageGroupName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setAgeGroupName(String ageGroupName) {
        this.ageGroupName = ageGroupName;
    }

    public FoodViewModel getSoup() {
        return soup;
    }

    public void setSoup(FoodViewModel soup) {
        this.soup = soup;
    }

    public FoodViewModel getMain() {
        return main;
    }

    public void setMain(FoodViewModel main) {
        this.main = main;
    }

    public FoodViewModel getDessert() {
        return dessert;
    }

    public void setDessert(FoodViewModel dessert) {
        this.dessert = dessert;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
