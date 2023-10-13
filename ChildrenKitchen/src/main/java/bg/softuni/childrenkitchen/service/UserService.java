package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.service.UserRegisterServiceModel;
import bg.softuni.childrenkitchen.model.view.UserAndChildViewModel;
import bg.softuni.childrenkitchen.model.view.UserViewModel;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void initDB();

    Optional<UserEntity> getByEmail(String mail);

    UserViewModel registerUser(UserRegisterServiceModel userRegisterServiceModel);

    boolean isEmailFree(String email);

    UserUpdateBindingModel editUser(UserUpdateBindingModel userUpdateBindingModel);

    List<UserAndChildViewModel> getUserByKeyWord(String keyWord);

    ChildEntity getChildByNames(String childFullName, String userEmail);

    boolean isAdmin(UserEntity user);

}
