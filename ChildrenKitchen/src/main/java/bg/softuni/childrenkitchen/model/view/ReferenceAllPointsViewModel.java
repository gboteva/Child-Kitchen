package bg.softuni.childrenkitchen.model.view;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;

import java.util.ArrayList;
import java.util.List;

public class ReferenceAllPointsViewModel {
    private String servicePoint;
    private Integer smallOrderCount;
    private Integer bigOrderCount;

    private AgeGroupEnum ageGroup;
    List<AllergicChildViewModel> allergicChildren = new ArrayList<>();

    public String getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(String servicePoint) {
        this.servicePoint = servicePoint;
    }

    public Integer getSmallOrderCount() {
        return smallOrderCount;
    }

    public void setSmallOrderCount(Integer smallOrderCount) {
        this.smallOrderCount = smallOrderCount;
    }

    public Integer getBigOrderCount() {
        return bigOrderCount;
    }

    public void setBigOrderCount(Integer bigOrderCount) {
        this.bigOrderCount = bigOrderCount;
    }

    public List<AllergicChildViewModel> getAllergicChildren() {
        return allergicChildren;
    }

    public void setAllergicChildren(List<AllergicChildViewModel> allergicChildren) {
        this.allergicChildren = allergicChildren;
    }

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
    }
}
