package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.view.UserAndChildViewModel;
import bg.softuni.childrenkitchen.service.OrderService;
import bg.softuni.childrenkitchen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
public class AdminSearchRestController {

    private final UserService userService;
    private final OrderService orderService;

    public AdminSearchRestController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
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
}
