package bg.softuni.childrenkitchen.model.entity;

import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import jakarta.persistence.*;


@Entity
@Table(name = "allergies")
public class AllergyEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private AllergyEnum allergenName;

    public AllergyEnum getAllergenName() {
        return allergenName;
    }

    public AllergyEntity setAllergenName(AllergyEnum allergenName) {
        this.allergenName = allergenName;
        return this;
    }
}
