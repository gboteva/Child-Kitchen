package bg.softuni.childrenkitchen.model.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateBindingModel {
    private Long id;
    @Email
    @NotBlank
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
