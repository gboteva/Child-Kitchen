package bg.softuni.childrenkitchen.service.interfaces;

import bg.softuni.childrenkitchen.model.entity.AllergyEntity;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;

import java.util.Optional;
import java.util.Set;

public interface AllergyService {
    void initDB();

    Optional<AllergyEntity> findByName(AllergyEnum allergyName);

    void deleteAllAllergiesByOwner(Set<ChildEntity> children);
}
