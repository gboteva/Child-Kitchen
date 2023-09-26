package bg.softuni.childrenkitchen.model.view;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;

public class AllergicChildViewModel {
    private String fullName;
    private AgeGroupEnum ageGroup;
    private String servicePoint;
    private String allergies;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(String servicePoint) {
        this.servicePoint = servicePoint;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
}
