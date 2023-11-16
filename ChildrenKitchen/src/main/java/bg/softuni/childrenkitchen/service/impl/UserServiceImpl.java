package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.userDetail.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.UserRegisterBindingModel;
import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.RoleEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.model.view.UserAndChildViewModel;
import bg.softuni.childrenkitchen.model.view.UserViewModel;
import bg.softuni.childrenkitchen.repository.UserRepository;
import bg.softuni.childrenkitchen.service.interfaces.PointService;
import bg.softuni.childrenkitchen.service.interfaces.RoleService;
import bg.softuni.childrenkitchen.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PointService pointService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PointService pointService, RoleService roleService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.pointService = pointService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initDB() {
        if (userRepository.count() > 0) {
            return;
        }

        UserEntity admin = new UserEntity();
        admin.setEmail("admin@test.com");
        admin.setFullName("АДМИН АДМИНОВ");
        admin.setPhoneNumber("0888 888 888");
        admin.setServicePoint(pointService.getByName("Детска кухня")
                                          .orElse(null));
        admin.setRoles(Set.of(roleService.getRole("ADMIN")
                                         .orElse(null)));
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setCity(CityEnum.ПЛЕВЕН);


        UserEntity user = new UserEntity();
        user.setEmail("mama@test.com");
        user.setFullName("МАМА ГОШКА");
        user.setPhoneNumber("0999 999 888");
        user.setServicePoint(pointService.getByName("9-ти квартал")
                                         .orElse(null));
        user.setRoles(Set.of(roleService.getRole("USER")
                                        .orElseThrow(ObjectNotFoundException::new)));
        user.setPassword(passwordEncoder.encode("test"));
        user.setCity(CityEnum.ПЛЕВЕН);

        UserEntity user2 = new UserEntity();
        user2.setEmail("mama2@test.com");
        user2.setFullName("МАМА ТОШКА");
        user2.setPhoneNumber("0999 777 666");
        user2.setServicePoint(pointService.getByName("ДГ Славейче")
                                          .orElse(null));
        user2.setRoles(Set.of(roleService.getRole("USER")
                                         .orElseThrow(ObjectNotFoundException::new)));
        user2.setPassword(passwordEncoder.encode("test"));
        user2.setCity(CityEnum.ПЛЕВЕН);

        userRepository.saveAll(List.of(admin, user, user2));
    }

    @Override
    public Optional<UserEntity> getByEmail(String mail) {
        return userRepository.findByEmail(mail);
    }

    @Override
    public boolean isEmailFree(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public UserUpdateBindingModel editUser(UserUpdateBindingModel userUpdateBindingModel, String loggedInUserEmail) {

        UserEntity loggedInUser = userRepository.findByEmail(loggedInUserEmail)
                                                .orElseThrow(ObjectNotFoundException::new);

        UserEntity userToEdit;

        if (isAdmin(loggedInUser)){
            userToEdit = userRepository.findByEmail(userUpdateBindingModel.getEmail())
                                       .orElseThrow(ObjectNotFoundException::new);

            if (userUpdateBindingModel.getNewEmail() !=null &&
                    !userUpdateBindingModel.getNewEmail().equals(userToEdit.getEmail())){
                userToEdit.setEmail(userUpdateBindingModel.getNewEmail());
            }


            if (userUpdateBindingModel.getMakeAdmin() != null){
                if (userUpdateBindingModel.getMakeAdmin()){
                    userToEdit.getRoles().add(roleService.getRole("ADMIN").orElseThrow(ObjectNotFoundException::new));
                }
            }

            if (userUpdateBindingModel.getMakeUser() != null){
                if (userUpdateBindingModel.getMakeUser()){
                    userToEdit.getRoles().clear();
                    userToEdit.getRoles().add(roleService.getRole("USER").orElseThrow(ObjectNotFoundException::new));
                }
            }

        }else {
            userToEdit = loggedInUser;
        }


        if (!userToEdit.getFullName().equals(userUpdateBindingModel.getFullName())){
            userToEdit.setFullName(userUpdateBindingModel.getFullName()
                                                         .toUpperCase());
        }

        if (!userToEdit.getPhoneNumber().equals(userUpdateBindingModel.getPhoneNumber())){
            userToEdit.setPhoneNumber(userUpdateBindingModel.getPhoneNumber());
        }

        if (!userToEdit.getCity().name().equals(userUpdateBindingModel.getCityName())){
            userToEdit.setCity(CityEnum.valueOf(userUpdateBindingModel.getCityName()));
        }

        if (!userToEdit.getServicePoint().getName().equals(userUpdateBindingModel.getServicePointName())){
            userToEdit.setServicePoint(pointService.getByName(userUpdateBindingModel.getServicePointName())
                                                   .orElseThrow(ObjectNotFoundException::new));
        }

        if (!userUpdateBindingModel.getPassword().equals("") && userUpdateBindingModel.getPassword()!=null){
            if (!passwordEncoder.matches(userUpdateBindingModel.getPassword(), userToEdit.getPassword())){
                userToEdit.setPassword(passwordEncoder.encode(userUpdateBindingModel.getPassword()));
            }
        }


        UserEntity saved = userRepository.save(userToEdit);
        UserUpdateBindingModel mapped = modelMapper.map(saved, UserUpdateBindingModel.class);
        mapped.setCityName(saved.getCity().name());
        mapped.setMakeAdmin(isAdmin(saved));

        return mapped;
    }

    @Override
    public boolean isAdmin(UserEntity userEntity) {
        for (RoleEntity r: userEntity.getRoles()){
            if (r.getRole().name().equals("ADMIN")){
                return true;
            }
        }

        return false;
    }

    @Override
    public ChildViewModel getPrincipalChildByName(CustomUserDetails loggedInUser, String childName) {

       return loggedInUser.getChildren()
                    .stream()
                    .filter(c -> c.getFullName()
                                  .equals(childName))
                    .findFirst()
                    .orElseThrow(ObjectNotFoundException::new);
    }



    @Override
    public List<UserAndChildViewModel> getUserByKeyWord(String keyWord) {
        List<UserEntity> matchedUser = userRepository.findAllByEmailContaining(keyWord);

        List<UserAndChildViewModel> list = new ArrayList<>();

        matchedUser.forEach(u -> {
            UserAndChildViewModel model = new UserAndChildViewModel();
            model.setUserEmail(u.getEmail());
            model.setUserNames(u.getFullName());
            model.setServicePointName(u.getServicePoint()
                                       .getName());
            model.setChildrenNames(u.getChildren()
                                    .stream()
                                    .map(ChildEntity::getFullName)
                                    .collect(Collectors.toSet()));
            model.setPhoneNumber(u.getPhoneNumber());
            model.setCityName(u.getCity().name());
            list.add(model);
        });

        return list;
    }

    @Override
    public ChildEntity getChildByNames(String childFullName, String userEmail) {
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                                              .orElseThrow(ObjectNotFoundException::new);

        return userEntity.getChildren()
                                      .stream()
                                      .filter(childEntity -> childEntity.getFullName()
                                                                        .equals(childFullName))
                                      .findFirst()
                                      .orElseThrow(ObjectNotFoundException::new);
    }


    @Override
    public UserViewModel registerUser(UserRegisterBindingModel userRegisterBindingModel) {

        UserEntity user = modelMapper.map(userRegisterBindingModel, UserEntity.class);
        user.setFullName(userRegisterBindingModel.getFullName()
                                                 .toUpperCase());
        user.setPassword(passwordEncoder.encode(userRegisterBindingModel.getPassword()));

        user.setRoles(Set.of(roleService.getRole("USER")
                                        .orElseThrow(ObjectNotFoundException::new)));
        user.setServicePoint(pointService.getByName("Детска кухня")
                                         .orElseThrow(ObjectNotFoundException::new));

        UserEntity saved = userRepository.save(user);

        UserViewModel userViewModel = modelMapper.map(saved, UserViewModel.class);
        userViewModel.setCityName(user.getCity()
                                      .name());


        return userViewModel;

    }


}
