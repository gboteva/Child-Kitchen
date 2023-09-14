package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.entity.AllergenEntity;

import java.util.Optional;

public interface AllergenService {
    void initDB();
    Optional<AllergenEntity> findById(Long id);
}
