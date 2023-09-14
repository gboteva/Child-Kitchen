package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.model.service.UserRegisterServiceModel;
import bg.softuni.childrenkitchen.model.view.UserViewModel;
import bg.softuni.childrenkitchen.repository.UserRepository;
import bg.softuni.childrenkitchen.service.PointService;
import bg.softuni.childrenkitchen.service.RoleService;
import bg.softuni.childrenkitchen.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

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
        if(userRepository.count() > 0){
            return;
        }

        UserEntity admin = new UserEntity();
        admin.setEmail("admin@test.com");
        admin.setFullName("Админ Админов");
        admin.setPhoneNumber("0888 888 888");
        admin.setServicePoint(pointService.getByName("Детска кухня").orElse(null));
        admin.setRoles(Set.of(roleService.getRole("ADMIN").orElse(null)));
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setCity(CityEnum.ПЛЕВЕН);


        UserEntity user = new UserEntity();
        user.setEmail("mama@test.com");
        user.setFullName("Мама Гошка");
        user.setPhoneNumber("0999 999 888");
        user.setServicePoint(pointService.getByName("9-ти квартал").orElse(null));
        user.setRoles(Set.of(roleService.getRole("USER").orElse(null)));
        user.setPassword(passwordEncoder.encode("test"));
        user.setCity(CityEnum.ПЛЕВЕН);

        UserEntity user2 = new UserEntity();
        user2.setEmail("mama2@test.com");
        user2.setFullName("Мама Тошка");
        user2.setPhoneNumber("0999 777 666");
        user2.setServicePoint(pointService.getByName("ДГ Славейче").orElse(null));
        user2.setRoles(Set.of(roleService.getRole("USER").orElse(null)));
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
    public UserViewModel registerUser(UserRegisterServiceModel userRegisterServiceModel) {

        UserEntity user = modelMapper.map(userRegisterServiceModel, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userRegisterServiceModel.getPassword()));

        user.setRoles(Set.of(roleService.getRole("USER").orElseThrow()));
        user.setServicePoint(pointService.getByName("Детска кухня").orElseThrow());

        UserViewModel userViewModel = modelMapper.map(userRepository.save(user), UserViewModel.class);
        userViewModel.setCityName(user.getCity().name());


        return  userViewModel;

    }


}
