package bg.softuni.childrenkitchen.service.interfaces;

import bg.softuni.childrenkitchen.model.binding.AdminSearchBindingModel;
import bg.softuni.childrenkitchen.model.view.OrderViewModel;
import bg.softuni.childrenkitchen.model.view.ReferenceByPointsViewModel;
import bg.softuni.childrenkitchen.model.view.ReferenceAllPointsViewModel;

import java.time.LocalDate;
import java.util.List;


public interface OrderService {
    void initDB();
    List<ReferenceAllPointsViewModel> getReferenceForAllPoints(AdminSearchBindingModel adminSearchBindingModel);

    List<ReferenceByPointsViewModel> getReferenceForPoint(AdminSearchBindingModel adminSearchBindingModel);

    OrderViewModel makeOrder(LocalDate date, String servicePoint, String userEmail, String childFullName, String loggedInUserEmail);

    List<LocalDate> getOrdersDateOfChild(String childName, String userEmail);

    void deleteOrder(LocalDate deleteOrderDate, String childName);

    List<OrderViewModel> getActiveOrders(String username);

    void deleteAllOrderByUserId(Long userId);

}
