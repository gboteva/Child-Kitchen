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
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<AllergenEntity> allergens;

    public String getName() {
        return name;
    }

    public FoodEntity setName(String name) {
        this.name = name;
        return this;
    }

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public FoodEntity setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
        return this;
    }

    public FoodCategoryEnum getCategory() {
        return category;
    }

    public FoodEntity setCategory(FoodCategoryEnum category) {
        this.category = category;
        return this;
    }

    public Set<AllergenEntity> getAllergens() {
        return allergens;
    }

    public FoodEntity setAllergens(Set<AllergenEntity> allergens) {
        this.allergens = allergens;
        return this;
    }
}
