package bg.softuni.childrenkitchen.model.service;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;

import java.math.BigDecimal;

public class BuyCouponsServiceModel {
    private String childName;
    private Integer countCoupons;

    private AgeGroupEnum ageGroup;

    private BigDecimal price;

    private String parentEmail;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public Integer getCountCoupons() {
        return countCoupons;
    }

    public void setCountCoupons(Integer countCoupons) {
        this.countCoupons = countCoupons;
    }

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }
}
