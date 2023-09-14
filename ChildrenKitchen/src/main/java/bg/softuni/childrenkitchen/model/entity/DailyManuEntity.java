package bg.softuni.childrenkitchen.model.entity;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "menus")
public class DailyManuEntity extends BaseEntity {

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "age-group", nullable = false)
    private AgeGroupEnum ageGroup;

    @ManyToOne
    private FoodEntity soup;

    @ManyToOne
    private FoodEntity main;

    @ManyToOne
    private FoodEntity dessert;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
    }

    public FoodEntity getSoup() {
        return soup;
    }

    public void setSoup(FoodEntity soup) {
        this.soup = soup;
    }

    public FoodEntity getMain() {
        return main;
    }

    public void setMain(FoodEntity main) {
        this.main = main;
    }

    public FoodEntity getDessert() {
        return dessert;
    }

    public void setDessert(FoodEntity dessert) {
        this.dessert = dessert;
    }
}
