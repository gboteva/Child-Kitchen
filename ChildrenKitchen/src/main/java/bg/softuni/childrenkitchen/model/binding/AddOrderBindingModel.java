package bg.softuni.childrenkitchen.model.binding;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AddOrderBindingModel {
    @FutureOrPresent
    @NotNull
    private LocalDate date;
    @NotBlank
    private String userEmail;
    @NotBlank
    private String childFullName;
    @NotBlank
    private String servicePoint;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getChildFullName() {
        return childFullName;
    }

    public void setChildFullName(String childFullName) {
        this.childFullName = childFullName;
    }

    public String getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(String servicePoint) {
        this.servicePoint = servicePoint;
    }
}
