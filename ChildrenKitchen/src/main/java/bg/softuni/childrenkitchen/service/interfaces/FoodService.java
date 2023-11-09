package bg.softuni.childrenkitchen.service.interfaces;

import bg.softuni.childrenkitchen.model.binding.AddRecipeBindingModel;
import bg.softuni.childrenkitchen.model.entity.FoodEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
import bg.softuni.childrenkitchen.model.view.FoodViewModel;

import java.util.Optional;
import java.util.Set;

public interface FoodService {
    void initDB();

    Optional<FoodEntity> getByName(String foodName);
    boolean existByName(String foodName);

    FoodViewModel addFood(AddRecipeBindingModel addRecipeBindingModel);

    Set<String> findAllFoodsNameByCategoryAndAgeGroup(FoodCategoryEnum foodCategoryEnum, AgeGroupEnum ageGroup);

    Set<String> getAllSoups(FoodCategoryEnum foodCategoryEnum);
}
