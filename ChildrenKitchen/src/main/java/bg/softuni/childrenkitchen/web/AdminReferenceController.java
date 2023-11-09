package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.binding.AdminSearchBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
import bg.softuni.childrenkitchen.model.view.ReferenceAllPointsViewModel;
import bg.softuni.childrenkitchen.model.view.ReferenceByPointsViewModel;
import bg.softuni.childrenkitchen.service.interfaces.ChildService;
import bg.softuni.childrenkitchen.service.interfaces.OrderService;
import bg.softuni.childrenkitchen.service.interfaces.PointService;
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

        List<AllergicChildViewModel> allAllergicChildren = childService.getAllAllergicChildrenFromDB();

        model.addAttribute("allergicKids", allAllergicChildren);


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

            Integer totalCountOrders = getTotalCountOrders(referenceForPoint);

            Integer totalCountAllergicOrders = getTotalCountAllergicOrders(referenceForPoint);

            Set<AllergicChildViewModel> allergicList = getAllergicList(referenceForPoint);

            redirectAttributes.addFlashAttribute("referenceByPointsViewModelList", referenceForPoint);
            redirectAttributes.addFlashAttribute("totalCountOrders", totalCountOrders);
            redirectAttributes.addFlashAttribute("totalCountAllOrders", totalCountAllergicOrders);
            redirectAttributes.addFlashAttribute("allergicChildren", allergicList);

        }else {
            List<ReferenceAllPointsViewModel> referenceForAllPoints = orderService.getReferenceForAllPoints(adminSearchBindingModel);

            redirectAttributes.addFlashAttribute("referenceAllPointsViewModelList", referenceForAllPoints);

            Set<AllergicChildViewModel> allergicList = new HashSet<>();
            Set<AllergicChildViewModel> allAllergicOrders = new HashSet<>();

            for (ReferenceAllPointsViewModel order : referenceForAllPoints) {
                allAllergicOrders.addAll(order.getAllergicChildren());

                order.getAllergicChildren().forEach(c -> {
                    if (!alreadyAdded(allergicList, c.getFullName())){
                        allergicList.add(c);
                    }
                });
            }


            Integer smallSum = getSmallOrderCount(referenceForAllPoints);

            Integer bigSum = getBigOrderCount(referenceForAllPoints);

            redirectAttributes.addFlashAttribute("allergicChildViewModelList",allergicList);
            redirectAttributes.addFlashAttribute("smallSum", smallSum);
            redirectAttributes.addFlashAttribute("bigSum", bigSum);
            redirectAttributes.addFlashAttribute("allergicSum", allAllergicOrders.size());
        }

        return "redirect:/admin";

    }

    private static Integer getBigOrderCount(List<ReferenceAllPointsViewModel> referenceForAllPoints) {
        return referenceForAllPoints.stream()
                                    .map(ReferenceAllPointsViewModel::getBigOrderCount)
                                    .reduce(0, Integer::sum);
    }

    private static Integer getSmallOrderCount(List<ReferenceAllPointsViewModel> referenceForAllPoints) {
        return referenceForAllPoints.stream()
                                    .map(ReferenceAllPointsViewModel::getSmallOrderCount)
                                    .reduce(0, Integer::sum);
    }

    private Set<AllergicChildViewModel> getAllergicList(List<ReferenceByPointsViewModel> referenceForPoint) {
        Set<AllergicChildViewModel> allergicList = new HashSet<>();

        referenceForPoint.stream().map(ReferenceByPointsViewModel::getAllergicChild)
                         .forEach(list -> {
                                     if (list.size() > 0){
                                         list.forEach(c -> {
                                             if(!alreadyAdded(allergicList, c.getFullName())){
                                                 allergicList.add(c);
                                             }
                                         });
                                     }

                                 });

        return allergicList;
    }

    private static Integer getTotalCountAllergicOrders(List<ReferenceByPointsViewModel> referenceForPoint) {
        return referenceForPoint.stream()
                                .map(ReferenceByPointsViewModel::getCountAllergicOrders)
                                .reduce(0, Integer::sum);
    }

    private static Integer getTotalCountOrders(List<ReferenceByPointsViewModel> referenceForPoint) {
        return referenceForPoint.stream()
                                .map(ReferenceByPointsViewModel::getCountOrders)
                                .reduce(0, Integer::sum);
    }

     private boolean alreadyAdded(Set<AllergicChildViewModel> allergicList, String fullName) {
        for(AllergicChildViewModel c : allergicList){
            if (c.getFullName().equals(fullName)){
                return true;
            }
        }

        return false;
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
