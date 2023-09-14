package bg.softuni.childrenkitchen.model.entity;

import bg.softuni.childrenkitchen.model.entity.enums.AllergensEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "allergens")
public class AllergenEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private AllergensEnum name;

    public AllergensEnum getName() {
        return name;
    }

    public void setName(AllergensEnum name) {
        this.name = name;
    }
}