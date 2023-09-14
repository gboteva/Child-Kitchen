package bg.softuni.childrenkitchen.model.entity;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "foods")
public class FoodEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private AgeGroupEnum ageGroup;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FoodCategoryEnum category;
    @ManyToMany
    private Set<AllergenEntity> allergens;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(FoodCategoryEnum category) {
        this.category = category;
    }

    public Set<AllergenEntity> getAllergens() {
        return allergens;
    }

    public void setAllergens(Set<AllergenEntity> allergens) {
        this.allergens = allergens;
    }

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
    }
}
