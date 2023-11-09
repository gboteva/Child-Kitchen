package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.PointEntity;
import bg.softuni.childrenkitchen.model.entity.RoleEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.model.entity.enums.UserRoleEnum;
import bg.softuni.childrenkitchen.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {
    private UserDetailService toTest;
    private static final String EXISTING_EMAIL = "admin@example.com";
    private static final String NOT_EXISTING_EMAIL = "pesho@example.com";

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp(){
        toTest = new UserDetailService(mockUserRepository);
    }

    @Test
    void testUserFound(){
        PointEntity testPoint = new PointEntity();
        testPoint.setName("Kitchen");
        testPoint.setAddress("somewhere");

        RoleEntity adminRole = new RoleEntity();
        adminRole.setRole(UserRoleEnum.ADMIN);

        RoleEntity userRole = new RoleEntity();
        userRole.setRole(UserRoleEnum.USER);

        ChildEntity testChild = new ChildEntity();
        testChild.setFullName("TestChild");
        testChild.setBirthDate(LocalDate.of(2023, 11, 1));
        testChild.setAllergies(new HashSet<>());
        testChild.setCoupons(new ArrayList<>());
        testChild.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        testChild.setBirthCertificateURL("testBirthCertUrl");
        testChild.setMedicalListURL("testMedicalURL");

        UserEntity testUser = new UserEntity();
            testUser.setEmail(EXISTING_EMAIL);
            testUser.setPassword("test");
            testUser.setCity(CityEnum.ПЛЕВЕН);
            testUser.setPhoneNumber("0888888887");
            testUser.setServicePoint(testPoint);
            testUser.setRoles(Set.of(adminRole, userRole));
            testUser.setChildren(Set.of(testChild));


        when(mockUserRepository.findByEmail(EXISTING_EMAIL))
                .thenReturn(Optional.of(testUser));


        CustomUserDetails adminDetails = (CustomUserDetails) toTest.loadUserByUsername(EXISTING_EMAIL);

        Assertions.assertNotNull(adminDetails);
        Assertions.assertEquals("test", adminDetails.getPassword());
        Assertions.assertEquals(EXISTING_EMAIL, adminDetails.getUsername());
        Assertions.assertEquals(2, adminDetails.getAuthorities().size());
        Assertions.assertEquals(1, adminDetails.getChildren().size());
        Assertions.assertNotEquals("", adminDetails.getChildren().stream().findFirst().get().getAge());


    }

    @Test
    void testUserNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            toTest.loadUserByUsername(NOT_EXISTING_EMAIL);
        });
    }
}
