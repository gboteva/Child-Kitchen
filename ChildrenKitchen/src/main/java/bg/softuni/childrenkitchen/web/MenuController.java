package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.view.MenuViewModel;
import bg.softuni.childrenkitchen.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    @GetMapping("/menus")
    public String getWeeklyMenu(Model model){
        List<MenuViewModel> weeklyMenu =  menuService.getWeeklyMenu();

        model.addAttribute("smallWeeklyMenu", weeklyMenu.stream()
                                                        .filter(m -> m.getAgeGroupName().equals("МАЛКИ"))
                                                        .collect(Collectors.toList()));


        model.addAttribute("bigWeeklyMenu",weeklyMenu.stream()
                                                     .filter(m -> m.getAgeGroupName().equals("ГОЛЕМИ"))
                                                     .collect(Collectors.toList()));

        return "/menus";
    }


}
