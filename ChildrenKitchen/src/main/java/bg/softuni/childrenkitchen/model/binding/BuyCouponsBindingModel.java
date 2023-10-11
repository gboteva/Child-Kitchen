package bg.softuni.childrenkitchen.model.binding;


public class BuyCouponsBindingModel {
    private String childName;
    private Integer countCoupons;

    private String price;

    private String ageGroupName;


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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAgeGroupName() {
        return ageGroupName;
    }

    public void setAgeGroupName(String ageGroupName) {
        this.ageGroupName = ageGroupName;
    }
}
