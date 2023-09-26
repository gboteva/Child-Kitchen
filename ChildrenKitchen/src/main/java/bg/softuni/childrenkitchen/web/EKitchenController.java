package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.BuyCouponsBindingModel;
import bg.softuni.childrenkitchen.model.binding.VerifyCouponBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.exception.NoAvailableCouponsError;
import bg.softuni.childrenkitchen.model.service.BuyCouponsServiceModel;
import bg.softuni.childrenkitchen.service.CouponService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class EKitchenController {
    private final CouponService couponService;
    private final ModelMapper modelMapper;

    public EKitchenController(CouponService couponService, ModelMapper modelMapper) {
        this.couponService = couponService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/e-kitchen")
    public String getEKitchen(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                              Model model,
                              BuyCouponsBindingModel buyCouponsBindingModel ){

        model.addAttribute("children", customUserDetails.getChildren());
        model.addAttribute("buyCouponsBindingModel", buyCouponsBindingModel);
        model.addAttribute("ageGroups", AgeGroupEnum.values());

        return "e-kitchen";
    }

    @PostMapping("/e-kitchen")
    public String buyCoupons(BuyCouponsBindingModel buyCouponsBindingModel,
                             @AuthenticationPrincipal CustomUserDetails loggedInUser){

        BuyCouponsServiceModel serviceModel = modelMapper.map(buyCouponsBindingModel, BuyCouponsServiceModel.class);

        serviceModel.setParentEmail(loggedInUser.getUsername());

        couponService.buyCoupons(serviceModel);

        return "redirect:/e-kitchen";
    }

    @PatchMapping("/e-kitchen")
    public String verifyCoupon(@Valid VerifyCouponBindingModel verifyCouponBindingModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("verifyCouponBindingModel", verifyCouponBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.verifyCouponBindingModel", bindingResult);
            return "redirect:/e-kitchen";
        }


        couponService.getAndVerifyCoupon(customUserDetails.getUsername(),
                                    verifyCouponBindingModel.getChildName(),
                                    verifyCouponBindingModel.getVerifyDate());

        return "redirect:/profile";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoAvailableCouponsError.class)
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
