package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.entity.AllergenEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergensEnum;

import java.util.Optional;

public interface AllergenService {
    void initDB();
    Optional<AllergenEntity> findById(Long id);

    Optional<AllergenEntity> findByName(String allergenName);

    AllergenEntity createAllergenEntity(String allergenName);
}
