package bg.softuni.childrenkitchen.model.view;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class OrderViewModel {
    private String childNames;
    private String date;
    private MenuViewModel menuViewModel;
    private String servicePointName;
    private Integer remainingCouponsCount;

    public String getChildNames() {
        return childNames;
    }

    public void setChildNames(String childNames) {
        this.childNames = childNames;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MenuViewModel getMenuViewModel() {
        return menuViewModel;
    }

    public void setMenuViewModel(MenuViewModel menuViewModel) {
        this.menuViewModel = menuViewModel;
    }

    public String getServicePointName() {
        return servicePointName;
    }

    public void setServicePointName(String servicePointName) {
        this.servicePointName = servicePointName;
    }

    public Integer getRemainingCouponsCount() {
        return remainingCouponsCount;
    }

    public void setRemainingCouponsCount(Integer remainingCouponsCount) {
        this.remainingCouponsCount = remainingCouponsCount;
    }
}
