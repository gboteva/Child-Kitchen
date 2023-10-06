package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.binding.AddMenuBindingModel;
import bg.softuni.childrenkitchen.model.binding.ViewMenuByDateBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.view.FoodViewModel;
import bg.softuni.childrenkitchen.model.view.MenuViewModel;
import bg.softuni.childrenkitchen.service.FoodService;
import bg.softuni.childrenkitchen.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MenuController {
    private final MenuService menuService;
    private final FoodService foodService;

    public MenuController(MenuService menuService, FoodService foodService) {
        this.menuService = menuService;
        this.foodService = foodService;
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

    @GetMapping("/admin/view-menu-by-date")
    public String getMenuByDate() {
        return "date-menu";
    }

    @GetMapping("/admin/add-menu")
    public String getAddMenu(Model model) {
        return "add-menu";
    }

    @PostMapping("/admin/view-menu-by-date")
    public String viewMenuByDate(ViewMenuByDateBindingModel viewMenuByDateBindingModel, RedirectAttributes redirectAttributes) {

        MenuViewModel dailyMenu = menuService.getMenuViewModelByDateAndAgeGroup(viewMenuByDateBindingModel.getDate(), AgeGroupEnum.valueOf(viewMenuByDateBindingModel.getAgeGroup()));

        if (dailyMenu.getSoup()
                     .getName()
                     .contains("Няма")) {
            redirectAttributes.addFlashAttribute("notFound", true);
        } else {
            redirectAttributes.addFlashAttribute("menuViewModel", dailyMenu);
        }

        return "redirect:/admin/view-menu-by-date";

    }

    @PostMapping("/admin/add-menu")
    public String addMenu(@Valid AddMenuBindingModel addMenuBindingModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addMenuBindingModel", addMenuBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addMenuBindingModel", bindingResult);
            return "redirect:/admin/add-menu";
        }

        if (menuService.existByDateAndAgeGroup(addMenuBindingModel.getDate(), AgeGroupEnum.valueOf(addMenuBindingModel.getAgeGroup()))) {
            redirectAttributes.addFlashAttribute("isExistingMenu", true);
            redirectAttributes.addFlashAttribute("existingMenu", menuService.getMenuViewModelByDateAndAgeGroup(addMenuBindingModel.getDate(), AgeGroupEnum.valueOf(addMenuBindingModel.getAgeGroup())));
            redirectAttributes.addFlashAttribute("date", addMenuBindingModel.getDate());
            return "redirect:/admin/add-menu";
        }


        MenuViewModel newMenu = menuService.createMenu(addMenuBindingModel);

        redirectAttributes.addFlashAttribute("addedNewMenu", true);
        redirectAttributes.addFlashAttribute("newMenu", newMenu);

        return "redirect:/admin/add-menu";
    }

    @PatchMapping("/admin/add-menu")
    public String editMenu(AddMenuBindingModel addMenuBindingModel, RedirectAttributes redirectAttributes) {

        MenuViewModel edited = menuService.editMenu(addMenuBindingModel.getDate(), AgeGroupEnum.valueOf(addMenuBindingModel.getAgeGroup()), addMenuBindingModel);

        redirectAttributes.addFlashAttribute("addedNewMenu", true);
        redirectAttributes.addFlashAttribute("newMenu", edited);

        return "redirect:/admin/add-menu";
    }


    @ModelAttribute
    public MenuViewModel model() {
        MenuViewModel model = new MenuViewModel();
        FoodViewModel food = new FoodViewModel();
        food.setName("не е избрано");
        model.setSoup(food);
        model.setMain(food);
        model.setDessert(food);
        return model;
    }

    @ModelAttribute
    public AddMenuBindingModel addMenuBindingModel() {
        return new AddMenuBindingModel();
    }
}
