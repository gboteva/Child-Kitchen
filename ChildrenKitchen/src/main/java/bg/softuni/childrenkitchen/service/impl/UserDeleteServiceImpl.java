package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.repository.UserRepository;
import bg.softuni.childrenkitchen.service.interfaces.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserDeleteServiceImpl implements UserDeleteService {
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ChildService childService;
    private final CouponService couponService;
    private final AllergyService allergyService;

    public UserDeleteServiceImpl(UserRepository userRepository, OrderService orderService, ChildService childService, CouponService couponService, AllergyService allergyService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.childService = childService;
        this.couponService = couponService;
        this.allergyService = allergyService;
    }

    @Override
    @Transactional
    public String deleteUser(String email) {

        UserEntity userToDelete = userRepository.findByEmail(email)
                                                .orElse(null);

        if (userToDelete == null){
            return null;
        }

        userToDelete.getChildren().forEach(childEntity -> {
            couponService.deleteCouponsByOwnerId(childEntity.getId());
        });

        orderService.deleteAllOrderByUserId(userToDelete.getId());

        allergyService.deleteAllAllergiesByOwner(userToDelete.getChildren());

        childService.deleteAllChildByUserId(userToDelete.getId());

        userRepository.delete(userToDelete);

        return userToDelete.getEmail();

    }
}
