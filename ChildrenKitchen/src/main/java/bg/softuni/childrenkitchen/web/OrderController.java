package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.binding.AddOrderBindingModel;
import bg.softuni.childrenkitchen.model.binding.DeleteOrderBindingModel;
import bg.softuni.childrenkitchen.model.exception.NoAvailableCouponsError;
import bg.softuni.childrenkitchen.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/add-delete-order")
    public String getAddDeleteOrder(){
        return "add-delete-order";
    }


    @PostMapping("/admin/add-delete-order")
    public String addOrder(@Valid AddOrderBindingModel addOrderBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addOrderBindingModel", addOrderBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addOrderBindingModel", bindingResult);
            return "redirect:/admin/add-delete-order";
        }

        orderService.makeOrder(addOrderBindingModel);

        return "redirect:/admin/add-delete-order";
    }

    @DeleteMapping("/admin/add-delete-order")
    public String deleteOrder(DeleteOrderBindingModel deleteOrderBindingModel){

        if(deleteOrderBindingModel.getDeleteOrderDate() == null ||
                deleteOrderBindingModel.getChildName().equals("")){
            return "redirect:/admin/add-delete-order";
        }

        orderService.deleteOrder(deleteOrderBindingModel.getDeleteOrderDate(), deleteOrderBindingModel.getChildName());

        return "redirect:/admin/add-delete-order";
    }


    @ModelAttribute
    public AddOrderBindingModel addOrderBindingModel(){
        return new AddOrderBindingModel();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoAvailableCouponsError.class)
    public ModelAndView onNoAvailableCoupons() {
        return new ModelAndView("no-available-coupons");
    }

}
