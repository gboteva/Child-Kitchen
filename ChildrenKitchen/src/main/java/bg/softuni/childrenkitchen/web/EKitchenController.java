package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.userDetail.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.BuyCouponsBindingModel;
import bg.softuni.childrenkitchen.model.binding.VerifyCouponBindingModel;
import bg.softuni.childrenkitchen.exception.NoAvailableCouponsException;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.model.view.OrderViewModel;
import bg.softuni.childrenkitchen.service.interfaces.CouponService;
import bg.softuni.childrenkitchen.service.interfaces.OrderService;
import bg.softuni.childrenkitchen.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class EKitchenController {
    private final CouponService couponService;
    private final OrderService orderService;
    private final UserService userService;

    public EKitchenController(CouponService couponService, OrderService orderService, UserService userService) {
        this.couponService = couponService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/e-kitchen")
    public String getEKitchen(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              Model model,
                              BuyCouponsBindingModel buyCouponsBindingModel ){

        model.addAttribute("children", customUserDetails.getChildren());
        model.addAttribute("buyCouponsBindingModel", buyCouponsBindingModel);

        return "e-kitchen";
    }

    @PostMapping("/e-kitchen")
    public String buyCoupons(BuyCouponsBindingModel buyCouponsBindingModel,
                             @AuthenticationPrincipal CustomUserDetails loggedInUser,
                             RedirectAttributes redirectAttributes){

        ChildViewModel couponsOwner = userService.getPrincipalChildByName(loggedInUser, buyCouponsBindingModel.getChildName());

        String ageGroupName = couponsOwner.getAgeGroupName();

        if (!buyCouponsBindingModel.getAgeGroupName().equals(ageGroupName)){
            redirectAttributes.addFlashAttribute("noMatchAgeGroup", true);
            return "redirect:/e-kitchen";
        }


        int countCoupons = couponService.buyCoupons(buyCouponsBindingModel, loggedInUser.getUsername());

        couponsOwner.setCountCoupons(couponsOwner.getCountCoupons() + countCoupons);

        redirectAttributes.addFlashAttribute("countCoupons", countCoupons);
        redirectAttributes.addFlashAttribute("kidName", buyCouponsBindingModel.getChildName());
        redirectAttributes.addFlashAttribute("success",true);


        loggedInUser.getChildren().stream()
                    .filter(c -> c.getFullName().equals(buyCouponsBindingModel.getChildName()))
                .findFirst()
                    .get()
                    .setCountCoupons(couponsOwner.getCountCoupons());

        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedInUser, loggedInUser.getPassword(), loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/e-kitchen";
    }

    @PatchMapping("/e-kitchen")
    public String verifyCouponAndMakeOrder(@Valid VerifyCouponBindingModel verifyCouponBindingModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("verifyCouponBindingModel", verifyCouponBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.verifyCouponBindingModel", bindingResult);
            return "redirect:/e-kitchen";
        }

        OrderViewModel orderViewModel = orderService.makeOrder(verifyCouponBindingModel.getVerifyDate(),
                customUserDetails.getServicePointName(),
                customUserDetails.getUsername(),
                verifyCouponBindingModel.getChildName(),
                customUserDetails.getUsername());

        if (orderViewModel == null){
            redirectAttributes.addFlashAttribute("noMoreOrdersPerDay", true);
            return "redirect:/e-kitchen";
        }

        customUserDetails.getChildren().stream()
                    .filter(c -> c.getFullName().equals(verifyCouponBindingModel.getChildName()))
                    .findFirst()
                    .get()
                    .setCountCoupons(orderViewModel.getRemainingCouponsCount());

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, customUserDetails.getPassword(), customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        redirectAttributes.addFlashAttribute("successVerify", true);
        redirectAttributes.addFlashAttribute("kidName", verifyCouponBindingModel.getChildName());
        redirectAttributes.addFlashAttribute("date", verifyCouponBindingModel.getVerifyDate());

        return "redirect:/e-kitchen";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoAvailableCouponsException.class)
    public ModelAndView onNoAvailableCoupons() {
        return new ModelAndView("no-available-coupons");
    }

    @ModelAttribute
    public BuyCouponsBindingModel buyCouponsBindingModel(){
        return new BuyCouponsBindingModel();
    }

    @ModelAttribute
    public VerifyCouponBindingModel verifyCouponBindingModel(){
        return new VerifyCouponBindingModel();
    }

}
