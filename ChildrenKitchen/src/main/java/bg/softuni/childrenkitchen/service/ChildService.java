package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.CouponEntity;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ChildService {
    void initDB() throws FileNotFoundException;

    ChildViewModel saveChild(ChildRegisterBindingModel childRegisterBindingModel, CustomUserDetails loggedInUser) throws IOException;

    List<AllergicChildViewModel> getAllAllergicChildren();

}
