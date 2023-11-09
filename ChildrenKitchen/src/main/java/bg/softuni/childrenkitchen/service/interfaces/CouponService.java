package bg.softuni.childrenkitchen.service.interfaces;

import bg.softuni.childrenkitchen.model.binding.BuyCouponsBindingModel;
import bg.softuni.childrenkitchen.model.entity.CouponEntity;
import java.time.LocalDate;

public interface CouponService {

    void initDB();
    int buyCoupons(BuyCouponsBindingModel buyCouponsBindingModel, String parentEmail);

    CouponEntity getAndVerifyCoupon(String userEmail, String childName, LocalDate forDate);

    Long unverifiedCoupon(Long couponId);

    void deleteCouponsByOwnerId(Long ownerId);

}
