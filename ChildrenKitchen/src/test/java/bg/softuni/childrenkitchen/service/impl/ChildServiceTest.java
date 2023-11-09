package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.*;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import bg.softuni.childrenkitchen.model.entity.enums.CityEnum;
import bg.softuni.childrenkitchen.model.entity.enums.UserRoleEnum;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.repository.ChildRepository;
import bg.softuni.childrenkitchen.service.interfaces.AllergyService;
import bg.softuni.childrenkitchen.service.interfaces.ChildService;
import bg.softuni.childrenkitchen.service.interfaces.CloudinaryService;
import bg.softuni.childrenkitchen.service.interfaces.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChildServiceTest {
    private static final String NOT_ADMIN_MAIL = "user@test.bg";
    @Mock
    private ChildService toTest;
    @Mock
    private ChildRepository mockChildRepo;
    @Mock
    private UserService mockUserService;
    @Mock
    private AllergyService mockAllergyService;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private CloudinaryService mockCloudinaryService;

    @Mock
    private ApplicationEventPublisher mockEvenPublisher;

    private ChildEntity testChild;

    @BeforeEach
    void setup() {
        toTest = new ChildServiceImpl(mockChildRepo, mockUserService, mockAllergyService, mockModelMapper, mockCloudinaryService, mockEvenPublisher );

        PointEntity testPoint = new PointEntity();
        testPoint.setName("Kitchen");
        testPoint.setAddress("somewhere");

        RoleEntity userRole = new RoleEntity();
        userRole.setRole(UserRoleEnum.USER);

        AllergyEntity allergy = new AllergyEntity();
        allergy.setAllergenName(AllergyEnum.ПТИЧЕ_МЕСО);

        testChild = new ChildEntity();
        testChild.setFullName("TestChild");
        testChild.setBirthDate(LocalDate.of(2022, 11, 1));
        testChild.setAllergies(Set.of(allergy));
        testChild.setCoupons(new ArrayList<>());
        testChild.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        testChild.setBirthCertificateURL("testBirthCertUrl");
        testChild.setMedicalListURL("testMedicalURL");


        UserEntity testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setEmail(NOT_ADMIN_MAIL);
        testUser.setPassword("test");
        testUser.setFullName("User Userov");
        testUser.setCity(CityEnum.ПЛЕВЕН);
        testUser.setPhoneNumber("0888888887");
        testUser.setServicePoint(testPoint);
        testUser.setRoles(Set.of(userRole));
        testUser.getChildren()
                .add(testChild);

        testChild.setParent(testUser);
    }

    @Test
    void testAllergicChildrenFromDB() {

        when(mockChildRepo.findAll())
                .thenReturn(List.of(testChild));

        AllergicChildViewModel viewModel = new AllergicChildViewModel();
        viewModel.setFullName(testChild.getFullName());
        viewModel.setServicePoint(testChild.getParent().getServicePoint().getName());
        viewModel.setAllergies("Птиче месо");
        viewModel.setAgeGroup(testChild.getAgeGroup());

      when(mockModelMapper.map(testChild, AllergicChildViewModel.class))
              .thenReturn(viewModel);

        Assertions.assertEquals("TestChild", toTest.getAllAllergicChildrenFromDB().get(0).getFullName());
    }

}