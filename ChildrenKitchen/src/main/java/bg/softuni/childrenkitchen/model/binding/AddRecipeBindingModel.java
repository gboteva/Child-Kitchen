package bg.softuni.childrenkitchen.model.binding;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergensEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class AddRecipeBindingModel {

    private AgeGroupEnum ageGroup;
    private FoodCategoryEnum foodCategory;
    @NotBlank
    private String foodName;

    private List<AllergensEnum> allergens;

    private String otherAllergen;

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
    }

    public FoodCategoryEnum getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(FoodCategoryEnum foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public List<AllergensEnum> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<AllergensEnum> allergens) {
        this.allergens = allergens;
    }

    public String getOtherAllergen() {
        return otherAllergen;
    }

    public void setOtherAllergen(String otherAllergen) {
        this.otherAllergen = otherAllergen;
    }
}
