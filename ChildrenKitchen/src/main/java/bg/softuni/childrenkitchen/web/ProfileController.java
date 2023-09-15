package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.service.ChildService;
import bg.softuni.childrenkitchen.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/users/profile")
public class ProfileController {

    private final ChildService childService;
    private final UserService userService;

    public ProfileController(ChildService childService, UserService userService) {
        this.childService = childService;
        this.userService = userService;
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

        userUpdateBindingModel.setId(loggedInUser.getId());

        userService.editUser(userUpdateBindingModel);

        return "redirect:/e-kitchen";
    }


    @PostMapping("/add-kid")
    public String addKid(@Valid ChildRegisterBindingModel childRegisterBindingModel,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal CustomUserDetails loggedInUser
                         ) throws IOException {

        boolean empty = childRegisterBindingModel.getBirthCertificate()
                                                 .isEmpty();
        String originalFilename = childRegisterBindingModel.getBirthCertificate()
                                                           .getOriginalFilename();
        String name = childRegisterBindingModel.getBirthCertificate()
                                               .getName();

        if(bindingResult.hasErrors() || !childRegisterBindingModel.getIsChecked()){
            redirectAttributes.addFlashAttribute("childRegisterBindingModel", childRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.childRegisterBindingModel", bindingResult);
            return "redirect:add-kid";
        }

        ChildViewModel childViewModel = childService.saveChild(childRegisterBindingModel, loggedInUser);


        return "redirect:/users/profile";
    }
}
