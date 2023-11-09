package bg.softuni.childrenkitchen.model.binding;

import bg.softuni.childrenkitchen.validation.annotation.ExistFile;
import bg.softuni.childrenkitchen.validation.annotation.IsChecked;
import bg.softuni.childrenkitchen.validation.annotation.OutOfAgeKid;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class ChildRegisterBindingModel {
    @Size(min = 10, max = 50)
    @NotBlank
    private String fullName;

    @PastOrPresent
    @OutOfAgeKid
    private LocalDate birthDate;
    @NotBlank
    private String allergy;

    @NotNull
    @ExistFile
    private MultipartFile medicalList;

    @NotNull
    @ExistFile
    private MultipartFile birthCertificate;

    @IsChecked
    private Boolean isChecked;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public MultipartFile getMedicalList() {
        return medicalList;
    }

    public void setMedicalList(MultipartFile medicalList) {
        this.medicalList = medicalList;
    }

    public MultipartFile getBirthCertificate() {
        return birthCertificate;
    }

    public void setBirthCertificate(MultipartFile birthCertificate) {
        this.birthCertificate = birthCertificate;
    }

}
