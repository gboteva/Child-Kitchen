package bg.softuni.childrenkitchen.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @Column(nullable = false)
    private LocalDate date;
    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ChildEntity child;

    @ManyToOne
    private CouponEntity coupon;

    @ManyToOne
    private PointEntity servicePoint;


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ChildEntity getChild() {
        return child;
    }

    public void setChild(ChildEntity child) {
        this.child = child;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public PointEntity getServicePoint() {
        return servicePoint;
    }

    public void setServicePoint(PointEntity servicePoint) {
        this.servicePoint = servicePoint;
    }

}
