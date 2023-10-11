package bg.softuni.childrenkitchen.model.view;

import java.util.List;

public class UserViewModel {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String cityName;
    private String servicePointName;
    private List<ChildViewModel> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getServicePointName() {
        return servicePointName;
    }

    public void setServicePointName(String servicePointName) {
        this.servicePointName = servicePointName;
    }

    public List<ChildViewModel> getChildren() {
        return children;
    }

    public void setChildren(List<ChildViewModel> children) {
        this.children = children;
    }
}
