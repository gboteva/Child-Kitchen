package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.binding.AdminSearchBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
import bg.softuni.childrenkitchen.model.view.ReferenceAllPointsViewModel;
import bg.softuni.childrenkitchen.model.view.ReferenceByPointsViewModel;
import bg.softuni.childrenkitchen.service.ChildService;
import bg.softuni.childrenkitchen.service.OrderService;
import bg.softuni.childrenkitchen.service.PointService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AdminReferenceController {
    private final ChildService childService;
    private final PointService pointService;
    private final OrderService orderService;

    public AdminReferenceController(ChildService childService, PointService pointService, OrderService orderService) {
        this.childService = childService;
        this.pointService = pointService;
        this.orderService = orderService;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model){
        model.addAttribute("ageGroups", AgeGroupEnum.values());

        model.addAttribute("points", pointService.getAllNames());

        model.addAttribute("allergicKids", childService.getAllAllergicChildren());


        return "admin";
    }

    @PostMapping("/admin")
    public String getInfoFromDB(@Valid AdminSearchBindingModel adminSearchBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()
                || adminSearchBindingModel.getFromDate() == null
                || adminSearchBindingModel.getToDate() == null
                || adminSearchBindingModel.getFromDate().isAfter(adminSearchBindingModel.getToDate())
        ){
            redirectAttributes.addFlashAttribute("adminSearchBindingModel", adminSearchBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.adminSearchBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("errorMsg", "Грешка при въвеждане!");
            return "redirect:/admin";
        }

        if (!adminSearchBindingModel.getServicePoint().equals("All")){
            List<ReferenceByPointsViewModel> referenceForPoint = orderService.getReferenceForPoint(adminSearchBindingModel);

            Integer totalCountOrders = referenceForPoint.stream()
                                              .map(ReferenceByPointsViewModel::getCountOrders)
                                              .reduce(0, Integer::sum);

            Integer totalCountAllOrders = referenceForPoint.stream()
                                                        .map(ReferenceByPointsViewModel::getCountAllergicOrders)
                                                        .reduce(0, Integer::sum);

           List<AllergicChildViewModel> allergicList = new ArrayList<>();
            referenceForPoint.stream().map(ReferenceByPointsViewModel::getAllergicChild)
                                     .forEach(list -> {
                                         if (list.size() > 0){
                                             allergicList.addAll(list);
                                         }
                                     });

            redirectAttributes.addFlashAttribute("referenceByPointsViewModelList", referenceForPoint);
            redirectAttributes.addFlashAttribute("totalCountOrders", totalCountOrders);
            redirectAttributes.addFlashAttribute("totalCountAllOrders", totalCountAllOrders);
            redirectAttributes.addFlashAttribute("allergicChildren", allergicList);

        }else {
            List<ReferenceAllPointsViewModel> referenceForAllPoints = orderService.getReferenceForAllPoints(adminSearchBindingModel);

            redirectAttributes.addFlashAttribute("referenceAllPointsViewModelList", referenceForAllPoints);

            Set<AllergicChildViewModel> allergicList = new HashSet<>();


            for (ReferenceAllPointsViewModel order : referenceForAllPoints) {

                allergicList.addAll(order.getAllergicChildren());
            }


            Integer smallSum = referenceForAllPoints.stream()
                                                        .map(ReferenceAllPointsViewModel::getSmallOrderCount)
                                                        .reduce(0, Integer::sum);

            Integer bigSum = referenceForAllPoints.stream()
                                                    .map(ReferenceAllPointsViewModel::getBigOrderCount)
                                                    .reduce(0, Integer::sum);

            redirectAttributes.addFlashAttribute("allergicChildViewModelList",allergicList);
            redirectAttributes.addFlashAttribute("smallSum", smallSum);
            redirectAttributes.addFlashAttribute("bigSum", bigSum);
            redirectAttributes.addFlashAttribute("allergicSum", allergicList.size());
        }

        return "redirect:/admin";

    }

    @ModelAttribute
    public AdminSearchBindingModel adminSearchBindingModel(){
        return new AdminSearchBindingModel();
    }

    @ModelAttribute
    public List<ReferenceByPointsViewModel> referenceByPointsViewModelList(){
        return new ArrayList<>();
    }

    @ModelAttribute
    public List<ReferenceAllPointsViewModel> referenceForAllPoints(){
        return new ArrayList<>();
   }

    @ModelAttribute
    public List<AllergicChildViewModel> getAllergicChildren(){
       return new ArrayList<>();
   }


}
