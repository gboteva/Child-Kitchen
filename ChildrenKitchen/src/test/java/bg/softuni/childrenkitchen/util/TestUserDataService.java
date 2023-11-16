package bg.softuni.childrenkitchen.util;

import bg.softuni.childrenkitchen.model.userDetail.CustomUserDetails;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.repository.UserRepository;
import bg.softuni.childrenkitchen.service.impl.UserDetailService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
public class TestUserDataService extends UserDetailService {


    public TestUserDataService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails userDetails;

        if (username.equalsIgnoreCase("admin@test.com")) {
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));

            userDetails = new CustomUserDetails("admin@test.com", "test", authorities);
            userDetails.setChildren(new HashSet<>());
        } else {
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));

            ChildViewModel child = new ChildViewModel();
            child.setFullName("TestChild2");
            child.setAgeGroupName(AgeGroupEnum.ГОЛЕМИ.name());
            child.setAllergies("Няма");
            child.setCountCoupons(0);
            child.setAge("1 год.");

            userDetails = new CustomUserDetails("user@test.bg", "test", authorities);
            userDetails.setChildren(new HashSet<>());
            userDetails.getChildren().add(child);
            userDetails.setServicePointName("9-ти квартал");
        }

        return userDetails;
    }


}

