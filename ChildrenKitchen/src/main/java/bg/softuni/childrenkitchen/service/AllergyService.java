package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.entity.AllergyEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;

import java.util.Optional;

public interface AllergyService {
    void initDB();

    Optional<AllergyEntity> findByName(AllergyEnum allergyName);
}
