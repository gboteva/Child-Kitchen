package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.binding.AddRecipeBindingModel;
import bg.softuni.childrenkitchen.model.entity.FoodEntity;
import bg.softuni.childrenkitchen.model.view.FoodViewModel;

import java.util.Optional;

public interface FoodService {
    void initDB();

    Optional<FoodEntity> getByName(String foodName);

    FoodViewModel addFood(AddRecipeBindingModel addRecipeBindingModel);
}
