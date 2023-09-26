package bg.softuni.childrenkitchen.model.view;

import java.time.LocalDate;
import java.util.List;

public class ReferenceByPointsViewModel {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String ageGroup;

    private String point;
    private Integer countOrders;
    private Integer countAllergicOrders;

    private List<AllergicChildViewModel> allergicChild;

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Integer getCountOrders() {
        return countOrders;
    }

    public void setCountOrders(Integer countOrders) {
        this.countOrders = countOrders;
    }

    public Integer getCountAllergicOrders() {
        return countAllergicOrders;
    }

    public void setCountAllergicOrders(Integer countAllergicOrders) {
        this.countAllergicOrders = countAllergicOrders;
    }

    public List<AllergicChildViewModel> getAllergicChild() {
        return allergicChild;
    }

    public void setAllergicChild(List<AllergicChildViewModel> allergicChild) {
        this.allergicChild = allergicChild;
    }
}
