package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.UserRegisterBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.model.service.UserRegisterServiceModel;
import bg.softuni.childrenkitchen.service.ChildService;
import bg.softuni.childrenkitchen.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users/register")
public class RegisterController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ChildService childService;

    public RegisterController(ModelMapper modelMapper, UserService userService, ChildService childService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.childService = childService;
    }

    @GetMapping()
    public String getRegister(Model model){
        model.addAttribute("cities", CityEnum.values());
        return "register";
    }

    @PostMapping()
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


    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel(){
        return new UserRegisterBindingModel();
    }


}
