package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.binding.UserRegisterBindingModel;
import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.model.service.UserRegisterServiceModel;
import bg.softuni.childrenkitchen.service.ChildService;
import bg.softuni.childrenkitchen.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ChildService childService;

    public UserController(ModelMapper modelMapper, UserService userService, ChildService childService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.childService = childService;
    }

    @GetMapping("/users/login")
    public String getLogin(){
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

        UserRegisterServiceModel userRegisterServiceModel = modelMapper.map(userRegisterBindingModel, UserRegisterServiceModel.class);

        userService.registerUser(userRegisterServiceModel);


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
    public String editUser(@Valid UserUpdateBindingModel userUpdateBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes){



        if (bindingResult.hasErrors() || (userUpdateBindingModel.getMakeUser() && userUpdateBindingModel.getMakeAdmin())){
            redirectAttributes.addFlashAttribute("userUpdateBindingModel", userUpdateBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userUpdateBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("notPossible", true);
            return "redirect:/admin/edit-user";
        }


        UserUpdateBindingModel updated = userService.editUser(userUpdateBindingModel);
        redirectAttributes.addFlashAttribute("successUpdate", true);
        redirectAttributes.addFlashAttribute("updated", updated);

        return "redirect:/admin/edit-user";

    }


    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel(){
        return new UserRegisterBindingModel();
    }



}
