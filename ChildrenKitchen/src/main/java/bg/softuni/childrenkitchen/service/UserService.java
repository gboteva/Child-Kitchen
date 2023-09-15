package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.service.UserRegisterServiceModel;
import bg.softuni.childrenkitchen.model.view.UserViewModel;

import java.util.Optional;

public interface UserService {
    void initDB();

    Optional<UserEntity> getByEmail(String mail);

    UserViewModel registerUser(UserRegisterServiceModel userRegisterServiceModel);

    boolean isEmailFree(String email);

    void editUser(UserUpdateBindingModel userUpdateBindingModel);
}
