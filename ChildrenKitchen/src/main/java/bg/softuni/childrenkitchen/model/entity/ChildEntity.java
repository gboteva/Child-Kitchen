package bg.softuni.childrenkitchen.model.entity;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "children")
public class ChildEntity extends BaseEntity {
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private AgeGroupEnum ageGroup;

    @Column(name = "medical_list_url")
    private String medicalListURL;

    @Column(name = "birth_cert_url")
    private String birthCertificateURL;

    @ManyToMany(targetEntity = AllergyEntity.class,
            fetch = FetchType.EAGER)
    private Set<AllergyEntity> allergies = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<CouponEntity> coupons = new ArrayList<>();

    @ManyToOne()
    private UserEntity parent;

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

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getMedicalListURL() {
        return medicalListURL;
    }

    public void setMedicalListURL(String medicalListURL) {
        this.medicalListURL = medicalListURL;
    }

    public String getBirthCertificateURL() {
        return birthCertificateURL;
    }

    public void setBirthCertificateURL(String birthCertificateURL) {
        this.birthCertificateURL = birthCertificateURL;
    }

    public Set<AllergyEntity> getAllergies() {
        return allergies;
    }

    public void setAllergies(Set<AllergyEntity> allergies) {
        this.allergies = allergies;
    }

    public List<CouponEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponEntity> coupons) {
        this.coupons = coupons;
    }

    public UserEntity getParent() {
        return parent;
    }

    public void setParent(UserEntity parent) {
        this.parent = parent;
    }

    public boolean isAllergic(){
      return getAllergies().stream().filter(a -> a.getAllergenName()!= AllergyEnum.НЯМА)
                .collect(Collectors.toSet()).size() > 0;
    }
}
