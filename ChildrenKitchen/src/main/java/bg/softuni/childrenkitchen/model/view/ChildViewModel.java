package bg.softuni.childrenkitchen.model.view;

import java.util.HashSet;
import java.util.Set;

public class ChildViewModel {
    private String fullName;
    private String age;
    private String allergies;
    private int countCoupons;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public int getCountCoupons() {
        return countCoupons;
    }

    public void setCountCoupons(int countCoupons) {
        this.countCoupons = countCoupons;
    }
}
