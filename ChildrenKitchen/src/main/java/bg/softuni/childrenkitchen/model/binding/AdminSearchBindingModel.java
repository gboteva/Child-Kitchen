package bg.softuni.childrenkitchen.model.binding;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class AdminSearchBindingModel {
    private LocalDate fromDate;

    private LocalDate toDate;
    @NotBlank
    private String servicePoint;
    @NotBlank
    private String ageGroup;

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

    public String getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(String servicePoint) {
        this.servicePoint = servicePoint;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }
}
