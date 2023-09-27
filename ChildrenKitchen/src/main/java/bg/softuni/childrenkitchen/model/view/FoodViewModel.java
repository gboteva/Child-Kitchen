package bg.softuni.childrenkitchen.model.view;

public class FoodViewModel {
    private String name;

    private String ageGroupName;

    private String categoryName;

    private String allergens;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgeGroupName() {
        return ageGroupName;
    }

    public void setAgeGroupName(String ageGroupName) {
        this.ageGroupName = ageGroupName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
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
