package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.userDetail.CustomUserDetails;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email)
                                        .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found!"));

       return mapToUserDetail(userEntity);

    }

    private static UserDetails mapToUserDetail(UserEntity userEntity) {
        List<GrantedAuthority> authorities = userEntity.getRoles().stream()
                                                       .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                                                       .collect(Collectors.toList());

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity.getEmail(), userEntity.getPassword(), authorities);
        customUserDetails.setId(userEntity.getId());
        customUserDetails.setFullName(userEntity.getFullName());
        customUserDetails.setPhoneNumber(userEntity.getPhoneNumber());
        customUserDetails.setCityName(userEntity.getCity().name());
        customUserDetails.setServicePointName(userEntity.getServicePoint().getName());
        customUserDetails.setChildren(userEntity.getChildren().stream().map(UserDetailService::mapToChildrenView).collect(Collectors.toSet()));

        return customUserDetails;
    }

    private static ChildViewModel mapToChildrenView(ChildEntity childEntity) {
        ChildViewModel childViewModel = new ChildViewModel();
        childViewModel.setFullName(childEntity.getFullName());
        childViewModel.setCountCoupons(childEntity.getCoupons().stream().filter(c->c.getVerifiedDate()==null).toList()
                                                  .size());
        childViewModel.setAllergies(childEntity.getAllergies().stream()
                                    .map(allergyEntity -> allergyEntity.getAllergenName().name())
                                               .collect(Collectors.joining(", ")));

        LocalDate childBirthDate = childEntity.getBirthDate();
        String age = "";
        if (childBirthDate.isBefore(LocalDate.now().minusMonths(12))){
           int years = LocalDate.now().getYear() - childBirthDate.getYear();
           int months = LocalDate.now().getMonth().getValue() - childBirthDate.getMonth().getValue();
           age = years + "год. и " + months + " мес";

        }else {
            int months = LocalDate.now().getMonth().getValue() - childBirthDate.getMonth().getValue();

            if (months == 0){
                age = "1 год.";
            }else if (months < 0){
                int lastYearMonthsCount = 12 - childBirthDate.getMonth().getValue();
                int thisYearMonthsCount = LocalDate.now().getMonth().getValue();
                months = lastYearMonthsCount + thisYearMonthsCount;
                age = months + " мес.";
            }else {
                age = months + " мес.";
            }
        }

        childViewModel.setAge(age);

        childViewModel.setAgeGroupName(childEntity.getAgeGroup().name());

        return childViewModel;
    }
}
