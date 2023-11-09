package bg.softuni.childrenkitchen.model.binding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateBindingModel {
    private Long id;

    private String email;
    @NotBlank
    @Size(min = 7, max = 50)
    private String fullName;
    @NotBlank
    @Size(min = 10)
    private String phoneNumber;
    @NotBlank
    private String cityName;
    @NotBlank
    private String servicePointName;
    private String password;
    private Boolean makeAdmin;
    private Boolean makeUser;

    private String newEmail;

    public Long getId() {
        return id;
    }

    public UserUpdateBindingModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserUpdateBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserUpdateBindingModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserUpdateBindingModel setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public UserUpdateBindingModel setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getServicePointName() {
        return servicePointName;
    }

    public UserUpdateBindingModel setServicePointName(String servicePointName) {
        this.servicePointName = servicePointName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserUpdateBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public Boolean getMakeAdmin() {
        return makeAdmin;
    }

    public UserUpdateBindingModel setMakeAdmin(Boolean makeAdmin) {
        this.makeAdmin = makeAdmin;
        return this;
    }

    public Boolean getMakeUser() {
        return makeUser;
    }

    public UserUpdateBindingModel setMakeUser(Boolean makeUser) {
        this.makeUser = makeUser;
        return this;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public UserUpdateBindingModel setNewEmail(String newEmail) {
        this.newEmail = newEmail;
        return this;
    }
}
