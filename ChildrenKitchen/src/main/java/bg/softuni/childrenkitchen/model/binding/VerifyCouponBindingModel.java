package bg.softuni.childrenkitchen.model.binding;

import bg.softuni.childrenkitchen.model.validation.DateVerifyCoupon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VerifyCouponBindingModel {
    @NotBlank
    private String childName;

    @DateVerifyCoupon
    private LocalDate verifyDate;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public LocalDate getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(LocalDate verifyDate) {
        this.verifyDate = verifyDate;
    }
}
