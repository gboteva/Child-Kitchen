package bg.softuni.childrenkitchen.model.view;

import bg.softuni.childrenkitchen.model.entity.ChildEntity;

import java.time.LocalDate;

public class CouponViewModel {
    private LocalDate verifiedDate;
    private String ownerName;

    public LocalDate getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(LocalDate verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
