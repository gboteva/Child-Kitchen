package bg.softuni.childrenkitchen.model.view;

import java.io.Serializable;
import java.time.LocalDate;

public class ChildViewModel implements Serializable {
    private String fullName;
    private String age;
    private String allergies;
    private int countCoupons;
    private String ageGroupName;

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

    public String getAgeGroupName() {
        return ageGroupName;
    }

    public void setAgeGroupName(String ageGroupName) {
        this.ageGroupName = ageGroupName;
    }

    public void defineAge(LocalDate birthDate){
        String age = "";
        if (birthDate.isBefore(LocalDate.now().minusMonths(12))){
            int years = LocalDate.now().getYear() - birthDate.getYear();
            int months = LocalDate.now().getMonth().getValue() - birthDate.getMonth().getValue();
            age = years + "год. и " + months + " мес";

        }else {
            int months = LocalDate.now().getMonth().getValue() - birthDate.getMonth().getValue();

            if (months == 0){
                age = "1 год.";
            }else if (months < 0){
                int lastYearMonthsCount = 12 - birthDate.getMonth().getValue();
                int thisYearMonthsCount = LocalDate.now().getMonth().getValue();
                months = lastYearMonthsCount + thisYearMonthsCount;
                age = months + " мес.";
            }
        }

        this.setAge(age);
    }
}
