package bg.softuni.childrenkitchen.model.entity;

import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "full_name")
    private String fullName;
    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CityEnum city;

    @ManyToOne(optional = false)
    private PointEntity servicePoint;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private Set<ChildEntity> children = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RoleEntity> roles;

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserEntity setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CityEnum getCity() {
        return city;
    }

    public UserEntity setCity(CityEnum city) {
        this.city = city;
        return this;
    }

    public PointEntity getServicePoint() {
        return servicePoint;
    }

    public UserEntity setServicePoint(PointEntity servicePoint) {
        this.servicePoint = servicePoint;
        return this;
    }

    public Set<ChildEntity> getChildren() {
        return children;
    }

    public UserEntity setChildren(Set<ChildEntity> children) {
        this.children = children;
        return this;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }
}
