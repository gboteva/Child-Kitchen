package bg.softuni.childrenkitchen.model.view;

import java.time.LocalDate;
import java.util.List;

public class ReferenceViewModel {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String point;
    private String ageGroup;
    private Integer countSmallOrders;
    private Integer countBigOrders;
    private Integer totalCountOrders;
    private Integer countAllergicOrders;
    private List<AllergicChildViewModel> allAllergicChildren;

    public LocalDate getFromDate() {
        return fromDate;
    }

    public ReferenceViewModel setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public ReferenceViewModel setToDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public String getPoint() {
        return point;
    }

    public ReferenceViewModel setPoint(String point) {
        this.point = point;
        return this;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public ReferenceViewModel setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
        return this;
    }

    public Integer getCountSmallOrders() {
        return countSmallOrders;
    }

    public ReferenceViewModel setCountSmallOrders(Integer countSmallOrders) {
        this.countSmallOrders = countSmallOrders;
        return this;
    }

    public Integer getCountBigOrders() {
        return countBigOrders;
    }

    public ReferenceViewModel setCountBigOrders(Integer countBigOrders) {
        this.countBigOrders = countBigOrders;
        return this;
    }

    public Integer getTotalCountOrders() {
        return totalCountOrders;
    }

    public ReferenceViewModel setTotalCountOrders(Integer totalCountOrders) {
        this.totalCountOrders = totalCountOrders;
        return this;
    }

    public Integer getCountAllergicOrders() {
        return countAllergicOrders;
    }

    public ReferenceViewModel setCountAllergicOrders(Integer countAllergicOrders) {
        this.countAllergicOrders = countAllergicOrders;
        return this;
    }

    public List<AllergicChildViewModel> getAllAllergicChildren() {
        return allAllergicChildren;
    }

    public ReferenceViewModel setAllAllergicChildren(List<AllergicChildViewModel> allAllergicChildren) {
        this.allAllergicChildren = allAllergicChildren;
        return this;
    }
}
