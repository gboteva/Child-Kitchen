package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.binding.UserRegisterBindingModel;
import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.service.PointService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PageController {

    @GetMapping("/users/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/about-us")
    public String aboutUs(){
        return "about-us";
    }

    @GetMapping("/healthy-food")
    public String getHealthyFood(){
        return "healthy";
    }



    @GetMapping("/admin/add-menu")
    public String getAddMenu(){
        return "add-menu";
    }

    @GetMapping("/admin/view-menu-by-date")
    public String getMenuByDate(){
        return "date-menu";
    }

    @GetMapping("/admin/add-recipe")
    public String addRecipe(){
        return "add-recipe";
    }














}
