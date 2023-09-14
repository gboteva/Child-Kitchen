package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ChildService {
    void initDB() throws FileNotFoundException;

    ChildViewModel saveChild(ChildRegisterBindingModel childRegisterBindingModel, CustomUserDetails loggedInUser) throws IOException;
}
