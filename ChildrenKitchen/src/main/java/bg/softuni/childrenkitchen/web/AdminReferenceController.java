package bg.softuni.childrenkitchen.web;

import bg.softuni.childrenkitchen.model.view.ReferenceViewModel;
import bg.softuni.childrenkitchen.model.binding.AdminSearchBindingModel;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
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
import java.util.List;

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
    public String getAdminPage(Model model) {
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

        if (bindingResult.hasErrors()
                || adminSearchBindingModel.getFromDate() == null
                || adminSearchBindingModel.getToDate() == null
                || adminSearchBindingModel.getFromDate().isAfter(adminSearchBindingModel.getToDate())) {
            redirectAttributes.addFlashAttribute("adminSearchBindingModel", adminSearchBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.adminSearchBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("errorMsg", "Грешка при въвеждане!");
            return "redirect:/admin";
        }

        List<ReferenceViewModel> adminReference = orderService.getAdminReference(adminSearchBindingModel);

        if (!adminSearchBindingModel.getServicePoint().equals("All")) {
            Integer totalCountOrders = getTotalCountOrders(adminReference);
            Integer totalCountAllergicOrders = getTotalCountAllergicOrders(adminReference);
            List<AllergicChildViewModel> allergicList = getListWithAllergicChildren(adminReference);

            redirectAttributes.addFlashAttribute("referenceByPointsViewModelList", adminReference);
            redirectAttributes.addFlashAttribute("totalCountOrders", totalCountOrders);
            redirectAttributes.addFlashAttribute("totalCountAllOrders", totalCountAllergicOrders);
            redirectAttributes.addFlashAttribute("allergicChildren", allergicList);
        } else {
            redirectAttributes.addFlashAttribute("referenceAllPointsViewModelList", adminReference);
            Integer smallSum = getTotalCountOfSmallOrders(adminReference);
            Integer bigSum = getTotalCountOfBigOrders(adminReference);

            if (!adminReference.isEmpty()) {
                List<AllergicChildViewModel> allergicList = adminReference.get(0).getAllAllergicChildren();
                redirectAttributes.addFlashAttribute("allergicChildViewModelList", allergicList);
            }

            Integer allergicSum = getTotalCountAllergicOrders(adminReference);

            redirectAttributes.addFlashAttribute("smallSum", smallSum);
            redirectAttributes.addFlashAttribute("bigSum", bigSum);
            redirectAttributes.addFlashAttribute("allergicSum", allergicSum);
        }

        return "redirect:/admin";

    }

    private static Integer getTotalCountOfBigOrders(List<ReferenceViewModel> adminReference) {
        return adminReference.stream()
                             .map(ReferenceViewModel::getCountBigOrders)
                             .reduce(Integer::sum)
                             .get();
    }

    private static Integer getTotalCountOfSmallOrders(List<ReferenceViewModel> adminReference) {
        return adminReference.stream()
                             .map(ReferenceViewModel::getCountSmallOrders)
                             .reduce(Integer::sum)
                             .get();
    }

    private static List<AllergicChildViewModel> getListWithAllergicChildren(List<ReferenceViewModel> adminReference) {
        return adminReference.stream()
                             .map(ReferenceViewModel::getAllAllergicChildren)
                             .reduce(new ArrayList<>(), (l1, l2) -> {
                                 l1.addAll(l2);
                                 return l1;
                             });
    }

    private static Integer getTotalCountAllergicOrders(List<ReferenceViewModel> adminReference) {
        return adminReference.stream()
                             .map(ReferenceViewModel::getCountAllergicOrders)
                             .reduce(Integer::sum)
                             .get();
    }

    private static Integer getTotalCountOrders(List<ReferenceViewModel> adminReference) {
        return adminReference.stream()
                             .map(ReferenceViewModel::getTotalCountOrders)
                             .reduce(Integer::sum)
                             .get();
    }


    @ModelAttribute
    public AdminSearchBindingModel adminSearchBindingModel() {
        return new AdminSearchBindingModel();
    }


    @ModelAttribute
    public List<AllergicChildViewModel> getAllergicChildren() {
        return new ArrayList<>();
    }


}
