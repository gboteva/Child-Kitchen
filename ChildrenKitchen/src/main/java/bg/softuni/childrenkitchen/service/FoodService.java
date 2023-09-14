package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.entity.FoodEntity;

import java.util.Optional;

public interface FoodService {
    void initDB();

    Optional<FoodEntity> getByName(String foodName);
}
