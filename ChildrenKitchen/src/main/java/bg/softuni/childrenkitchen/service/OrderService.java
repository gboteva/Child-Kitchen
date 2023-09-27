package bg.softuni.childrenkitchen.service;

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

    OrderViewModel makeOrder(LocalDate date, String servicePointName, String userEmail, String childFullName);

    List<LocalDate> getOrdersOfChild(String childName, String userEmail);

    void deleteOrder(LocalDate deleteOrderDate, String childName);

    List<OrderViewModel> getOrdersFromToday(String username);

}
