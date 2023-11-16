package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.userDetail.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.UserRegisterBindingModel;
import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.service.interfaces.UserDeleteService;
import bg.softuni.childrenkitchen.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;
    private final UserDeleteService userDeleteService;

    public UserController(UserService userService, UserDeleteService userDeleteService) {
        this.userService = userService;
        this.userDeleteService = userDeleteService;
    }

    @GetMapping("/users/login")
    public String getLogin(){
        return "login";
    }

    @PostMapping("/users/login-error")
    public String loginError(Model model){
        model.addAttribute("badCredentials", true);
        return "login";
    }

    @GetMapping("/users/register")
    public String getRegister(Model model){
        model.addAttribute("cities", CityEnum.values());
        return "register";
    }

    @PostMapping("/users/register")
    public String registerConfirm(@Valid UserRegisterBindingModel userRegisterBindingModel,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors() ||
                !userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);

            return "redirect:/users/register";
        }

        userService.registerUser(userRegisterBindingModel);

        return "redirect:/users/login";
    }

    @GetMapping("/admin/edit-user")
    public String getEditUser(Model model){

        model.addAttribute("cities", CityEnum.values());

        if (!model.containsAttribute("userUpdateBindingModel")){
            model.addAttribute("userUpdateBindingModel", new UserUpdateBindingModel());
        }

        return "edit-user";
    }

    @PutMapping("/admin/edit-user")
    public String editUser(@Valid UserUpdateBindingModel userUpdateBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal CustomUserDetails loggedInUser){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userUpdateBindingModel", userUpdateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userUpdateBindingModel", bindingResult);

            if(userUpdateBindingModel.getMakeUser() && userUpdateBindingModel.getMakeAdmin()){
                redirectAttributes.addFlashAttribute("notPossible", true);
            }

            return "redirect:/admin/edit-user";
        }

        if(userUpdateBindingModel.getMakeUser() && userUpdateBindingModel.getMakeAdmin()){
            redirectAttributes.addFlashAttribute("notPossible", true);
            return "redirect:/admin/edit-user";
        }

        UserUpdateBindingModel updated = userService.editUser(userUpdateBindingModel, loggedInUser.getUsername());
        redirectAttributes.addFlashAttribute("successUpdate", true);
        redirectAttributes.addFlashAttribute("updated", updated);


        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedInUser, loggedInUser.getPassword(), loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/admin/edit-user";

    }

    @DeleteMapping("/users/delete")
    public String deleteUser(@RequestParam("email") String email, RedirectAttributes redirectAttributes){

       String deletedUserEmail = userDeleteService.deleteUser(email);

       if (deletedUserEmail == null){
           redirectAttributes.addFlashAttribute("notSuccessDelete", true);
       }else {
           redirectAttributes.addFlashAttribute("successfullyDeleted", deletedUserEmail);
       }

        return "redirect:/admin/edit-user";
    }


    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel(){
        return new UserRegisterBindingModel();
    }

}
