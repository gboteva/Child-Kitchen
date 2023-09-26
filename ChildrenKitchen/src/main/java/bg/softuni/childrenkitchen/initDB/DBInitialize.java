package bg.softuni.childrenkitchen.initDB;

import bg.softuni.childrenkitchen.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInitialize implements CommandLineRunner {
    private final RoleService roleService;
    private final AllergenService allergenService;
    private final PointService pointService;
    private final FoodService foodService;
    private final MenuService menuService;
    private final UserService userService;
    private final AllergyService allergyService;
    private final ChildService childService;
    private final CouponService couponService;
    private final OrderService orderService;

    public DBInitialize(RoleService roleService, AllergenService allergenService, PointService pointService, FoodService foodService, MenuService menuService, UserService userService, AllergyService allergyService, ChildService childService, CouponService couponService, OrderService orderService) {
        this.roleService = roleService;
        this.allergenService = allergenService;
        this.pointService = pointService;
        this.foodService = foodService;
        this.menuService = menuService;
        this.userService = userService;
        this.allergyService = allergyService;
        this.childService = childService;
        this.couponService = couponService;
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.initDB();
        allergenService.initDB();
        pointService.initDB();
        foodService.initDB();
        menuService.initDB();
        userService.initDB();
        allergyService.initDB();
        childService.initDB();
        couponService.initDB();
        orderService.initDB();
    }
}
