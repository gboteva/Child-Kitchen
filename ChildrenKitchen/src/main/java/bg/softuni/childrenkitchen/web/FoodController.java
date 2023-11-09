package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.binding.AddRecipeBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
import bg.softuni.childrenkitchen.model.view.FoodViewModel;
import bg.softuni.childrenkitchen.service.interfaces.FoodService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }


    @GetMapping("/healthy-food")
    public String getHealthyFood(){
        return "healthy";
    }

    @GetMapping("/admin/add-recipe")
    public String addRecipe(Model model){
        model.addAttribute("ageGroups", AgeGroupEnum.values());
        model.addAttribute("foodCategories", FoodCategoryEnum.values());
        return "add-recipe";
    }

    @PostMapping("/admin/add-recipe")
    public String postRecipe(@Valid AddRecipeBindingModel addRecipeBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes
                             ){


        if (foodService.existByName(addRecipeBindingModel.getFoodName())){
            redirectAttributes.addFlashAttribute("existFood", "Храна с това име вече съществува в базата!");
            return "redirect:/admin/add-recipe";
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addRecipeBindingModel", addRecipeBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addRecipeBindingModel", bindingResult);
            return "redirect:/admin/add-recipe";
        }

        FoodViewModel foodViewModel = foodService.addFood(addRecipeBindingModel);

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("foodViewModel", foodViewModel.toString());

        return "redirect:/admin/add-recipe";
    }


    @ModelAttribute
    public AddRecipeBindingModel addRecipeBindingModel(){
        return new  AddRecipeBindingModel();
    }

    @ModelAttribute
    public FoodViewModel foodViewModel(){
        return new FoodViewModel();
    }
}
