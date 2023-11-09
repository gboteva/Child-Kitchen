package bg.softuni.childrenkitchen.service.interfaces;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.UserRegisterBindingModel;
import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.model.view.UserAndChildViewModel;
import bg.softuni.childrenkitchen.model.view.UserViewModel;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void initDB();

    Optional<UserEntity> getByEmail(String mail);

    UserViewModel registerUser(UserRegisterBindingModel userRegisterBindingModel);

    boolean isEmailFree(String email);

    UserUpdateBindingModel editUser(UserUpdateBindingModel userUpdateBindingModel,  String loggedInUserEmail);

    List<UserAndChildViewModel> getUserByKeyWord(String keyWord);

    ChildEntity getChildByNames(String childFullName, String userEmail);

    boolean isAdmin(UserEntity user);

    ChildViewModel getPrincipalChildByName(CustomUserDetails loggedInUser, String childName);


}
