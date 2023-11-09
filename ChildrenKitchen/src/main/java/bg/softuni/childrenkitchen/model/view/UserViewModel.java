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

    public UserViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserViewModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserViewModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserViewModel setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public UserViewModel setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getServicePointName() {
        return servicePointName;
    }

    public UserViewModel setServicePointName(String servicePointName) {
        this.servicePointName = servicePointName;
        return this;
    }

    public List<ChildViewModel> getChildren() {
        return children;
    }

    public UserViewModel setChildren(List<ChildViewModel> children) {
        this.children = children;
        return this;
    }
}
