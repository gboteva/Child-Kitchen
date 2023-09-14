package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.service.ChildService;
import bg.softuni.childrenkitchen.service.PointService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/users/profile")
public class ProfileController {

    private final PointService pointService;
    private final ChildService childService;

    public ProfileController(PointService pointService, ChildService childService) {
        this.pointService = pointService;
        this.childService = childService;
    }

    @GetMapping()
    public String getUserProfile(
                                 @AuthenticationPrincipal CustomUserDetails loggedInUser,
                                 Model model){
        if (loggedInUser!=null){
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("points", pointService.getAll());
        }
        return "profile";
    }


    @PostMapping()
    public String editProfile(){
        // TODO: 13.9.2023 г.
        System.out.println("Am here");
        return null;
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


        return "redirect:/e-kitchen";
    }
}
