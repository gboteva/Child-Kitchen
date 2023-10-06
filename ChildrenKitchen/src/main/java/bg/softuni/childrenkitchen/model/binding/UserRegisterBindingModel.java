package bg.softuni.childrenkitchen.model.binding;

import bg.softuni.childrenkitchen.validation.annotation.UniqueEmail;
import jakarta.validation.constraints.*;

public class UserRegisterBindingModel {

    @Email
    @NotBlank
    @UniqueEmail
    private String email;
    @NotBlank
    @Size(min = 7, max = 50)
    private String fullName;
    @NotBlank
    private String city;
    @NotBlank
    @Size(min = 10)
    private String phoneNumber;
    @NotBlank
    @Size(min = 4)
    private String password;
    @NotBlank
    @Size(min = 4)
    private String confirmPassword;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


}
