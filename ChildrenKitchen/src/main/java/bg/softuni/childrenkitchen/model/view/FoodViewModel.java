package bg.softuni.childrenkitchen.model.view;

public class FoodViewModel {
    private String name;

    private String ageGroupName;

    private String categoryName;

    private String allergens;

    public String getName() {
        return name;
    }

    public FoodViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getAgeGroupName() {
        return ageGroupName;
    }

    public FoodViewModel setAgeGroupName(String ageGroupName) {
        this.ageGroupName = ageGroupName;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public FoodViewModel setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getAllergens() {
        return allergens;
    }

    public FoodViewModel setAllergens(String allergens) {
        this.allergens = allergens;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(System.lineSeparator());
        sb.append("Възрастова група: ").append(ageGroupName).append(System.lineSeparator());
        sb.append("Категория: ").append(categoryName).append(System.lineSeparator());
        sb.append("Алергени: ").append(allergens).append(System.lineSeparator());

        return sb.toString();
    }
}
