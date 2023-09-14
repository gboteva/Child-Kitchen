package bg.softuni.childrenkitchen.model.entity;

import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "allergies")
public class AllergyEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private AllergyEnum allergenName;

    @ManyToMany(targetEntity = ChildEntity.class, mappedBy = "allergies")
    private Set<ChildEntity> children;

    public AllergyEnum getAllergenName() {
        return allergenName;
    }

    public void setAllergenName(AllergyEnum allergenName) {
        this.allergenName = allergenName;
    }

    public Set<ChildEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildEntity> owners) {
        this.children = owners;
    }
}
