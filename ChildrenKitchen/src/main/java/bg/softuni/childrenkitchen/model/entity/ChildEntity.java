package bg.softuni.childrenkitchen.model.entity;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import jakarta.persistence.*;

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

    @Column(name = "medical_list_public_id")
    private String medicalListPublicId;

    @Column(name = "birth_cert_public_id")
    private String birthCertificatePublic_id;

    @ManyToMany(targetEntity = AllergyEntity.class,
            fetch = FetchType.EAGER)
    private Set<AllergyEntity> allergies = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<CouponEntity> coupons = new ArrayList<>();

    @ManyToOne
    private UserEntity parent;

    public String getFullName() {
        return fullName;
    }

    public ChildEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public ChildEntity setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public AgeGroupEnum getAgeGroup() {
        return ageGroup;
    }

    public ChildEntity setAgeGroup(AgeGroupEnum ageGroup) {
        this.ageGroup = ageGroup;
        return this;
    }

    public String getMedicalListURL() {
        return medicalListURL;
    }

    public ChildEntity setMedicalListURL(String medicalListURL) {
        this.medicalListURL = medicalListURL;
        return this;
    }

    public String getBirthCertificateURL() {
        return birthCertificateURL;
    }

    public ChildEntity setBirthCertificateURL(String birthCertificateURL) {
        this.birthCertificateURL = birthCertificateURL;
        return this;
    }

    public Set<AllergyEntity> getAllergies() {
        return allergies;
    }

    public ChildEntity setAllergies(Set<AllergyEntity> allergies) {
        this.allergies = allergies;
        return this;
    }

    public List<CouponEntity> getCoupons() {
        return coupons;
    }

    public ChildEntity setCoupons(List<CouponEntity> coupons) {
        this.coupons = coupons;
        return this;
    }

    public UserEntity getParent() {
        return parent;
    }

    public ChildEntity setParent(UserEntity parent) {
        this.parent = parent;
        return this;
    }

    public String getMedicalListPublicId() {
        return medicalListPublicId;
    }

    public ChildEntity setMedicalListPublicId(String medicalListPublicId) {
        this.medicalListPublicId = medicalListPublicId;
        return this;
    }

    public String getBirthCertificatePublic_id() {
        return birthCertificatePublic_id;
    }

    public ChildEntity setBirthCertificatePublic_id(String birthCertificatePublic_id) {
        this.birthCertificatePublic_id = birthCertificatePublic_id;
        return this;
    }

    public boolean isAllergic(){
      return getAllergies().stream().filter(a -> a.getAllergenName()!= AllergyEnum.НЯМА)
                .collect(Collectors.toSet()).size() > 0;
    }
}
