package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.userDetail.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.service.interfaces.ChildService;
import bg.softuni.childrenkitchen.service.interfaces.OrderService;
import bg.softuni.childrenkitchen.service.interfaces.PointService;
import bg.softuni.childrenkitchen.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/users/profile")
public class ProfileController {

    private final ChildService childService;
    private final UserService userService;
    private final PointService pointService;
    private final OrderService orderService;


    public ProfileController(ChildService childService, UserService userService, PointService pointService, OrderService orderService) {
        this.childService = childService;
        this.userService = userService;
        this.pointService = pointService;
        this.orderService = orderService;
    }
    @GetMapping()
    public String getUserProfile(
            @AuthenticationPrincipal CustomUserDetails loggedInUser,
            Model model){

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("points", pointService.getAllNames());
        model.addAttribute("cities", CityEnum.values());
        model.addAttribute("lastOrders", orderService.getActiveOrders(loggedInUser.getUsername()));

        return "profile";
    }

    @PatchMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editProfile(@Valid UserUpdateBindingModel userUpdateBindingModel,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal CustomUserDetails loggedInUser,
                              RedirectAttributes redirectAttributes){


        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userUpdateBindingModel", userUpdateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userUpdateBindingModel", bindingResult);

            return "redirect:/users/profile";
        }



        UserUpdateBindingModel updated = userService.editUser(userUpdateBindingModel, loggedInUser.getUsername());

        loggedInUser.setCityName(updated.getCityName());
        loggedInUser.setPhoneNumber(updated.getPhoneNumber());
        loggedInUser.setFullName(updated.getFullName());
        loggedInUser.setServicePointName(updated.getServicePointName());

        redirectAttributes.addFlashAttribute("successEdit", true);

        return "redirect:/users/profile";
    }


    @GetMapping("/add-kid")
    public String addKidPage(){
        return "add-kid";
    }


    @PostMapping("/add-kid")
    public String addKid(@Valid ChildRegisterBindingModel childRegisterBindingModel,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal CustomUserDetails loggedInUser
                         ) throws IOException {


        if(bindingResult.hasErrors() || !childRegisterBindingModel.getIsChecked()){
            redirectAttributes.addFlashAttribute("childRegisterBindingModel", childRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.childRegisterBindingModel", bindingResult);
            return "redirect:add-kid";
        }

        ChildViewModel childViewModel = childService.saveChild(childRegisterBindingModel, loggedInUser.getUsername());
        loggedInUser.getChildren().add(childViewModel);

        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedInUser, loggedInUser.getPassword(), loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/users/profile";
    }


    @ModelAttribute
    public UserUpdateBindingModel userUpdateBindingModel(){
        return new UserUpdateBindingModel();
    }

    @ModelAttribute
    public ChildRegisterBindingModel childRegisterBindingModel(){
        return new ChildRegisterBindingModel();
    }

}
