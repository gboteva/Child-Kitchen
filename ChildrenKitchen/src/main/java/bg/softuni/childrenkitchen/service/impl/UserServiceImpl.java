package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.binding.UserUpdateBindingModel;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.model.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.service.UserRegisterServiceModel;
import bg.softuni.childrenkitchen.model.view.UserAndChildViewModel;
import bg.softuni.childrenkitchen.model.view.UserViewModel;
import bg.softuni.childrenkitchen.repository.UserRepository;
import bg.softuni.childrenkitchen.service.PointService;
import bg.softuni.childrenkitchen.service.RoleService;
import bg.softuni.childrenkitchen.service.UserService;
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
    public UserUpdateBindingModel editUser(UserUpdateBindingModel userUpdateBindingModel) {
        UserEntity userEntity = userRepository.findById(userUpdateBindingModel.getId())
                                              .orElseThrow();

        userEntity.setEmail(userUpdateBindingModel.getEmail());
        userEntity.setFullName(userUpdateBindingModel.getFullName()
                                                     .toUpperCase());
        userEntity.setPhoneNumber(userUpdateBindingModel.getPhoneNumber());
        userEntity.setCity(CityEnum.valueOf(userUpdateBindingModel.getCityName()));
        userEntity.setServicePoint(pointService.getByName(userUpdateBindingModel.getServicePointName())
                                               .orElseThrow());


        UserEntity saved = userRepository.save(userEntity);
        return modelMapper.map(saved, UserUpdateBindingModel.class);
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
    public UserViewModel registerUser(UserRegisterServiceModel userRegisterServiceModel) {

        UserEntity user = modelMapper.map(userRegisterServiceModel, UserEntity.class);
        user.setFullName(userRegisterServiceModel.getFullName()
                                                 .toUpperCase());
        user.setPassword(passwordEncoder.encode(userRegisterServiceModel.getPassword()));

        user.setRoles(Set.of(roleService.getRole("USER")
                                        .orElseThrow(ObjectNotFoundException::new)));
        user.setServicePoint(pointService.getByName("Детска кухня")
                                         .orElseThrow(ObjectNotFoundException::new));

        UserViewModel userViewModel = modelMapper.map(userRepository.save(user), UserViewModel.class);
        userViewModel.setCityName(user.getCity()
                                      .name());


        return userViewModel;

    }


}
