package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
import bg.softuni.childrenkitchen.model.view.FoodNameAndAgeGroup;
import bg.softuni.childrenkitchen.model.view.PointViewModel;
import bg.softuni.childrenkitchen.model.view.UserAndChildViewModel;
import bg.softuni.childrenkitchen.service.FoodService;
import bg.softuni.childrenkitchen.service.OrderService;
import bg.softuni.childrenkitchen.service.PointService;
import bg.softuni.childrenkitchen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
public class CommonRestController {

    private final UserService userService;
    private final OrderService orderService;
    private final PointService pointService;
    private final FoodService foodService;

    public CommonRestController(UserService userService, OrderService orderService, PointService pointService, FoodService foodService) {
        this.userService = userService;
        this.orderService = orderService;
        this.pointService = pointService;
        this.foodService = foodService;
    }

    @GetMapping("/api/points")
    public ResponseEntity<Set<PointViewModel>> getAboutUs(){
        return ResponseEntity.ok(pointService.getAll());
    }

    @GetMapping("/api/admin/add-delete-order/search")
    public ResponseEntity<List<UserAndChildViewModel>> searchUser(@RequestParam String addApplicantName){
        List<UserAndChildViewModel> userByKeyWord = userService.getUserByKeyWord(addApplicantName);
        return ResponseEntity.ok(userByKeyWord);
    }

    @GetMapping("/api/admin/add-delete-order/allOrders")
    public ResponseEntity<List<LocalDate>> childOrders(@RequestParam String childName, @RequestParam String userEmail){
        List<LocalDate> orderDate = orderService.getOrdersOfChild(childName, userEmail);
        return ResponseEntity.ok(orderDate);
    }

    @GetMapping("/api/get-foods")
    public ResponseEntity<Map<String, Set<String>>> getAllFoods(){
        Map<String, Set<String>> foods = new HashMap<>();

        foods.put("soups", foodService.findAllNameByCategoryAndAgeGroup(FoodCategoryEnum.СУПА, AgeGroupEnum.ГОЛЕМИ));
        foods.put("puree", foodService.findAllNameByCategoryAndAgeGroup(FoodCategoryEnum.ПЮРЕ, AgeGroupEnum.МАЛКИ));
        foods.put("smallMain", foodService.findAllNameByCategoryAndAgeGroup(FoodCategoryEnum.ОСНОВНО, AgeGroupEnum.МАЛКИ));
        foods.put("bigMain", foodService.findAllNameByCategoryAndAgeGroup(FoodCategoryEnum.ОСНОВНО, AgeGroupEnum.ГОЛЕМИ));
        foods.put("smallDessert", foodService.findAllNameByCategoryAndAgeGroup(FoodCategoryEnum.ДЕСЕРТ, AgeGroupEnum.МАЛКИ));
        foods.put("bigDessert", foodService.findAllNameByCategoryAndAgeGroup(FoodCategoryEnum.ДЕСЕРТ, AgeGroupEnum.ГОЛЕМИ));

        return ResponseEntity.ok(foods);
    }


}
