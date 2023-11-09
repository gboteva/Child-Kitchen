package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.*;
import bg.softuni.childrenkitchen.model.entity.enums.*;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.model.view.FoodViewModel;
import bg.softuni.childrenkitchen.model.view.MenuViewModel;
import bg.softuni.childrenkitchen.model.view.OrderViewModel;
import bg.softuni.childrenkitchen.repository.OrderRepository;
import bg.softuni.childrenkitchen.service.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderService mockOrderServiceToTest;

    private static final String NOT_ADMIN_MAIL = "user@test.bg";
    @Mock
    private OrderRepository mockOrderRepo;
    @Mock
    private UserService mockUserService;
    @Mock
    private MenuService mockMenuService;

    @Mock
    private CouponService mockCouponService;

    @Mock
    private PointService mockPointService;
    @Mock
    private ModelMapper mockModelMapper;

    private UserEntity testUser;
    private OrderEntity order;

    @BeforeEach
    void setup(){
        mockOrderServiceToTest = new OrderServiceImpl(mockOrderRepo, mockUserService,mockMenuService,mockCouponService, mockPointService, mockModelMapper);
        PointEntity testPoint = new PointEntity();
        testPoint.setName("Kitchen");
        testPoint.setAddress("somewhere");

        RoleEntity userRole = new RoleEntity();
        userRole.setRole(UserRoleEnum.USER);

        AllergyEntity allergy = new AllergyEntity();
        allergy.setAllergenName(AllergyEnum.ПТИЧЕ_МЕСО);

        ChildEntity testChild = new ChildEntity();
        testChild.setFullName("TestChild");
        testChild.setBirthDate(LocalDate.of(2022, 11, 1));
        testChild.setAllergies(Set.of(allergy));
        testChild.setCoupons(new ArrayList<>());
        testChild.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        testChild.setBirthCertificateURL("testBirthCertUrl");
        testChild.setMedicalListURL("testMedicalURL");

        testUser = new UserEntity();
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

        CouponEntity coupon = new CouponEntity();
        coupon.setOwner(testChild);
        coupon.setPrice(BigDecimal.TEN);
        coupon.setAgeGroup(testChild.getAgeGroup());
        coupon.setVerifiedDate(LocalDate.now());

        CouponEntity coupon2 = new CouponEntity();
        coupon2.setOwner(testChild);
        coupon2.setPrice(BigDecimal.TEN);
        coupon2.setAgeGroup(testChild.getAgeGroup());
        coupon2.setVerifiedDate(null);

        CouponEntity coupon3 = new CouponEntity();
        coupon3.setOwner(testChild);
        coupon3.setPrice(BigDecimal.TEN);
        coupon3.setAgeGroup(testChild.getAgeGroup());
        coupon3.setVerifiedDate(null);

        testChild.getCoupons().add(coupon);
        testChild.getCoupons().add(coupon2);
        testChild.getCoupons().add(coupon3);

        order = new OrderEntity();
        order.setDate(coupon.getVerifiedDate());
        order.setChild(testChild);
        order.setCoupon(coupon);
        order.setServicePoint(testPoint);
        order.setUser(testUser);

    }

    @Test
    public void testGetActiveOrdersMustNewArrayListIfNouOrders(){
        when(mockOrderRepo.findAllByUserEmail(testUser.getEmail()))
                .thenReturn(Optional.of(List.of()));

        Assertions.assertEquals(0, mockOrderServiceToTest.getActiveOrders(NOT_ADMIN_MAIL).size());
    }

    @Test
    public void testGetActiveOrdersMustValidData(){
        when(mockOrderRepo.findAllByUserEmail(testUser.getEmail()))
                .thenReturn(Optional.of(List.of(order)));

        MenuViewModel viewModel = new MenuViewModel();
        viewModel.setDate(order.getDate().toString());
        viewModel.setDessert(new FoodViewModel().setName("Dessert").setAllergens(AllergensEnum.ГЛУТЕН.name()).setCategoryName(FoodCategoryEnum.ДЕСЕРТ.name()).setAgeGroupName(order.getChild().getAgeGroup().name()));
        viewModel.setMain(new FoodViewModel().setName("Main").setAllergens(AllergensEnum.ЦЕЛИНА.name()).setCategoryName(FoodCategoryEnum.ОСНОВНО.name()).setAgeGroupName(order.getChild().getAgeGroup().name()));
        viewModel.setSoup(new FoodViewModel().setName("Soup").setAllergens(AllergensEnum.ЯЙЦА.name()).setCategoryName(FoodCategoryEnum.СУПА.name()).setAgeGroupName(order.getChild().getAgeGroup().name()));
        viewModel.setLocalDate(order.getDate());
        viewModel.setDayOfWeek(order.getDate().getDayOfWeek().toString());
        viewModel.setAgeGroupName(order.getChild().getAgeGroup().name());

        when(mockMenuService.getMenuViewModelByDateAndAgeGroup(order.getDate(), order.getChild()
                                                                                     .getAgeGroup()))
                .thenReturn(viewModel);

        Assertions.assertEquals(1, mockOrderServiceToTest.getActiveOrders(NOT_ADMIN_MAIL).size());
        Assertions.assertEquals(testUser.getChildren().stream().findFirst().get().getFullName(), mockOrderServiceToTest.getActiveOrders(NOT_ADMIN_MAIL).get(0).getChildNames());
        Assertions.assertEquals(2, mockOrderServiceToTest.getActiveOrders(NOT_ADMIN_MAIL).get(0).getRemainingCouponsCount());

    }


    @Test
    public void testGetActiveOrdersMustValidSortedData(){
        CouponEntity coupon3 = new CouponEntity();
        coupon3.setOwner(testUser.getChildren().stream().findFirst().get());
        coupon3.setPrice(BigDecimal.TEN);
        coupon3.setAgeGroup(coupon3.getOwner().getAgeGroup());
        coupon3.setVerifiedDate(LocalDate.now().plusDays(7));

        OrderEntity order1 = new OrderEntity();
        order1.setDate(coupon3.getVerifiedDate());
        order1.setChild(testUser.getChildren().stream().findFirst().get());
        order1.setCoupon(coupon3);
        order1.setServicePoint(testUser.getServicePoint());
        order1.setUser(testUser);


        when(mockOrderRepo.findAllByUserEmail(testUser.getEmail()))
                .thenReturn(Optional.of(List.of(order, order1)));

        MenuViewModel viewModel = new MenuViewModel();
        viewModel.setDate(order.getDate().toString());
        viewModel.setDessert(new FoodViewModel().setName("Dessert").setAllergens(AllergensEnum.ГЛУТЕН.name()).setCategoryName(FoodCategoryEnum.ДЕСЕРТ.name()).setAgeGroupName(order.getChild().getAgeGroup().name()));
        viewModel.setMain(new FoodViewModel().setName("Main").setAllergens(AllergensEnum.ЦЕЛИНА.name()).setCategoryName(FoodCategoryEnum.ОСНОВНО.name()).setAgeGroupName(order.getChild().getAgeGroup().name()));
        viewModel.setSoup(new FoodViewModel().setName("Soup").setAllergens(AllergensEnum.ЯЙЦА.name()).setCategoryName(FoodCategoryEnum.СУПА.name()).setAgeGroupName(order.getChild().getAgeGroup().name()));
        viewModel.setLocalDate(order.getDate());
        viewModel.setDayOfWeek(order.getDate().getDayOfWeek().toString());
        viewModel.setAgeGroupName(order.getChild().getAgeGroup().name());

        when(mockMenuService.getMenuViewModelByDateAndAgeGroup(order.getDate(), order.getChild()
                                                                                     .getAgeGroup()))
                .thenReturn(viewModel);

        when(mockMenuService.getMenuViewModelByDateAndAgeGroup(order1.getDate(), order1.getChild()
                                                                                     .getAgeGroup()))
                .thenReturn(viewModel);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        Assertions.assertEquals(2, mockOrderServiceToTest.getActiveOrders(NOT_ADMIN_MAIL).size());

        Assertions.assertEquals(formatter.format(LocalDate.now().plusDays(7)), mockOrderServiceToTest.getActiveOrders(NOT_ADMIN_MAIL).get(0).getDate());

    }

}
