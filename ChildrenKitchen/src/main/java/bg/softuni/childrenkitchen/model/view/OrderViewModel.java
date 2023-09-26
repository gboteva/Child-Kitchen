package bg.softuni.childrenkitchen.model.view;

import java.time.LocalDate;

public class OrderViewModel {
    private String childNames;
    private LocalDate verifiedDate;
    private MenuViewModel menuViewModel;
    private String servicePointName;
    private Integer remainingCouponsCount;

    public String getChildNames() {
        return childNames;
    }

    public void setChildNames(String childNames) {
        this.childNames = childNames;
    }

    public LocalDate getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(LocalDate verifiedDate) {
        this.verifiedDate = verifiedDate;
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
