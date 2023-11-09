package bg.softuni.childrenkitchen.util;

import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.entity.*;
import bg.softuni.childrenkitchen.model.entity.enums.*;
import bg.softuni.childrenkitchen.repository.*;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TestDataUtil {
    private static final String ADMIN_MAIL = "admin@test.com";
    private static final String NOT_ADMIN_MAIL = "user@test.bg";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PointRepository pointRepository;
    private final ChildRepository childRepository;
    private final AllergyRepository allergyRepository;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;

    private final AllergenRepository allergenRepository;
    private final FoodRepository foodRepository;
    private final MenusRepository menusRepository;

    public TestDataUtil(UserRepository userRepository, RoleRepository roleRepository, PointRepository pointRepository, ChildRepository childRepository, AllergyRepository allergyRepository, OrderRepository orderRepository, CouponRepository couponRepository, AllergenRepository allergenRepository, FoodRepository foodRepository, MenusRepository menusRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.pointRepository = pointRepository;
        this.childRepository = childRepository;
        this.allergyRepository = allergyRepository;
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.allergenRepository = allergenRepository;
        this.foodRepository = foodRepository;
        this.menusRepository = menusRepository;
    }

    public void initAllergy(){
        AllergyEntity none = new AllergyEntity();
        none.setId(1L);
        none.setAllergenName(AllergyEnum.НЯМА);

        AllergyEntity other = new AllergyEntity();
        other.setId(2L);
        other.setAllergenName(AllergyEnum.ДРУГО);

        allergyRepository.save(none);
        allergyRepository.save(other);
    }

    public void initRoles(){
        if (roleRepository.count() > 0){
            return;
        }
        RoleEntity admin = new RoleEntity();
        admin.setRole(UserRoleEnum.ADMIN);
        RoleEntity user = new RoleEntity();
        user.setRole(UserRoleEnum.USER);
        roleRepository.save(admin);
        roleRepository.save(user);
    }

    public void initPoints(){
        if (pointRepository.count() > 0){
            return;
        }

        PointEntity peace = new PointEntity();
        peace.setName("ДЯ Мир");
        peace.setAddress("ж.к. Сторгозия");
        peace.setWorkingTime("11:30ч. - 12:00ч");
        peace.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D1%8F_%D0%BC%D0%B8%D1%80_yijqe3.jpg");


        PointEntity ninthDistrict = new PointEntity();
        ninthDistrict.setName("9-ти квартал");
        ninthDistrict.setAddress("ул. Гурко №10");
        ninthDistrict.setWorkingTime("11:30ч. - 12:15ч");
        ninthDistrict.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/gurko_tugnfe.jpg");

        PointEntity centralPoint = new PointEntity();
        centralPoint.setName("Детска кухня");
        centralPoint.setAddress("ул. Георги Кочев");
        centralPoint.setWorkingTime("11:30ч. - 12:30ч");
        centralPoint.setPictureUrl("https://res.cloudinary.com/galkab/image/upload/v1694089119/DK_PROJECT/POINTS/%D0%B4%D0%BA_w8lrse.jpg");

        pointRepository.save(peace);
        pointRepository.save(ninthDistrict);
        pointRepository.save(centralPoint);

    }

    public void initChild(){
        ChildEntity testChild = new ChildEntity();
        testChild.setFullName("TestChild");
        testChild.setBirthDate(LocalDate.of(2022, 11, 1));
        testChild.setAllergies(Set.of(allergyRepository.findByAllergenName(AllergyEnum.НЯМА).orElseThrow()));
        testChild.setCoupons(new ArrayList<>());
        testChild.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        testChild.setBirthCertificateURL("testBirthCertUrl");
        testChild.setMedicalListURL("testMedicalURL");
        testChild.setParent(userRepository.findByEmail(ADMIN_MAIL).orElseThrow());
        childRepository.save(testChild);

        ChildEntity testChild2 = new ChildEntity();
        testChild2.setFullName("TestChild2");
        testChild2.setBirthDate(LocalDate.of(2022, 10, 3));
        testChild2.setAllergies(Set.of(allergyRepository.findByAllergenName(AllergyEnum.НЯМА).orElseThrow()));
        testChild2.setCoupons(new ArrayList<>());
        testChild2.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        testChild2.setBirthCertificateURL("testBirthCertUrl");
        testChild2.setMedicalListURL("testMedicalURL");
        testChild2.setParent(userRepository.findByEmail(NOT_ADMIN_MAIL).orElseThrow());
        childRepository.save(testChild2);
    }

    public void initUsers(){
        UserEntity testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setEmail(ADMIN_MAIL);
        testUser.setPassword("test");
        testUser.setFullName("Admin Adminov");
        testUser.setCity(CityEnum.ПЛЕВЕН);
        testUser.setPhoneNumber("0888888887");
        testUser.setServicePoint(pointRepository.findByName("Детска кухня").get());
        testUser.setRoles(new HashSet<>());
        testUser.getRoles().add(roleRepository.findByRole(UserRoleEnum.USER).get());
        testUser.getRoles().add(roleRepository.findByRole(UserRoleEnum.ADMIN).get());
//        testUser.setChildren(new HashSet<>());
        userRepository.save(testUser);

        UserEntity testUser2 = new UserEntity();
        testUser2.setId(2L);
        testUser2.setEmail("user@test.bg");
        testUser2.setPassword("test");
        testUser2.setFullName("User Userov");
        testUser2.setCity(CityEnum.ВЪРБИЦА);
        testUser2.setPhoneNumber("5464564645645654");
        testUser2.setServicePoint(pointRepository.findByName("9-ти квартал").get());
        testUser2.setRoles(new HashSet<>());
        testUser2.getRoles().add(roleRepository.findByRole(UserRoleEnum.USER).get());
//        testUser2.setChildren(new HashSet<>());
        userRepository.save(testUser2);
    }

    public void initCoupons(){
        CouponEntity adminChildCoupon = new CouponEntity();
        ChildEntity childEntity = childRepository.findAll().stream().limit(1).toList()
                                                 .get(0);
        adminChildCoupon.setOwner(childEntity);
        adminChildCoupon.setVerifiedDate(null);
        adminChildCoupon.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        adminChildCoupon.setPrice(BigDecimal.valueOf(0.80));


        couponRepository.save(adminChildCoupon);

        CouponEntity userChildCoupon = new CouponEntity();
        ChildEntity userChild = childRepository.findAll().stream().limit(2).toList()
                                              .get(1);
        userChildCoupon.setOwner(userChild);
        userChildCoupon.setVerifiedDate(null);
        userChildCoupon.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        userChildCoupon.setPrice(BigDecimal.valueOf(0.80));


        couponRepository.save(userChildCoupon);
    }

    public void initOrders(){
        LocalDate date = LocalDate.now();
        LocalDate with = date.with(DayOfWeek.FRIDAY).plusDays(5);

        OrderEntity userOrder = new OrderEntity();
        userOrder.setDate(with);
        userOrder.setCoupon(couponRepository.findAll().stream().limit(2).toList()
                                            .get(1));
        userOrder.setChild(userOrder.getCoupon().getOwner());
        userOrder.setServicePoint(pointRepository.findByName("9-ти квартал").orElseThrow(ObjectNotFoundException::new));
        userOrder.setUser(userRepository.findByEmail(NOT_ADMIN_MAIL).orElseThrow());
        userOrder.setId(2L);

        CouponEntity coupon = userOrder.getCoupon();
        coupon.setVerifiedDate(userOrder.getDate());
        couponRepository.save(coupon);

        orderRepository.save(userOrder);
    }

    public void initAllergens(){
        AllergenEntity al = new AllergenEntity();
        al.setName(AllergensEnum.ПРЯСНО_МЛЯКО.name());

        allergenRepository.save(al);
    }

    public void initFood(){

        FoodEntity soup = new FoodEntity();
        soup.setName("Supichka");
        soup.setAllergens(new HashSet<>());
        soup.setCategory(FoodCategoryEnum.СУПА);
        soup.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        foodRepository.save(soup);

        FoodEntity main = new FoodEntity();
        main.setName("Prase");
        main.setAllergens(new HashSet<>());
        main.setCategory(FoodCategoryEnum.ОСНОВНО);
        main.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        foodRepository.save(main);

        FoodEntity dessert = new FoodEntity();
        dessert.setName("Sladko");
        dessert.setAllergens(new HashSet<>());
        dessert.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        dessert.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        foodRepository.save(dessert);

        FoodEntity soup2 = new FoodEntity();
        soup2.setName("SOUP");
        soup2.setAllergens(new HashSet<>());
        soup2.setCategory(FoodCategoryEnum.СУПА);
        soup2.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        foodRepository.save(soup2);

        FoodEntity main2 = new FoodEntity();
        main2.setName("MAIN");
        main2.setAllergens(new HashSet<>());
        main2.setCategory(FoodCategoryEnum.ОСНОВНО);
        main2.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        foodRepository.save(main2);

        FoodEntity dessert2 = new FoodEntity();
        dessert2.setName("SWEET");
        dessert2.setAllergens(new HashSet<>());
        dessert2.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        dessert2.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        foodRepository.save(dessert2);
    }

    public void initMenus(){
        DailyManuEntity menu = new DailyManuEntity();
        menu.setDate(LocalDate.now().with(DayOfWeek.MONDAY).plusDays(2));
        menu.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);

        menu.setSoup(foodRepository.findByName("Supichka").orElseThrow());
        menu.setMain(foodRepository.findByName("Prase").orElseThrow());
        menu.setDessert(foodRepository.findByName("Sladko").orElseThrow());

        menusRepository.save(menu);
    }


    @Cascade(value = CascadeType.ALL)
    @Transactional
    public void cleanUpDateBase() {
        orderRepository.deleteAll();
        allergyRepository.deleteAll();
        couponRepository.deleteAll();
        childRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        pointRepository.deleteAll();
        allergenRepository.deleteAll();
        foodRepository.deleteAll();
        menusRepository.deleteAll();
    }

    public List<String> getAllEmailInDB(){
      return userRepository.findAll().stream().map(UserEntity::getEmail).collect(Collectors.toList());
    }

    public UserEntity getEntityByEmail(String email){
      return userRepository.findByEmail(email).orElse(null);
    }


    public void buyCouponsByUser(){
        UserEntity userEntity = userRepository.findByEmail(NOT_ADMIN_MAIL).get();
        ChildEntity childEntity = userEntity.getChildren()
                                            .stream()
                                            .findFirst()
                                            .get();

        CouponEntity coupon = new CouponEntity();
        coupon.setOwner(childEntity);
        coupon.setPrice(BigDecimal.valueOf(0.8));
        coupon.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        coupon.setVerifiedDate(null);

        couponRepository.save(coupon);

        childEntity.getCoupons().add(coupon);
        childRepository.save(childEntity);
    }

    public void populateOrdersToTestAdminStatistic(){
        UserEntity user2 = new UserEntity();
        user2.setRoles(Set.of(roleRepository.findByRole(UserRoleEnum.USER).orElseThrow()));
        user2.setFullName("Petko Petkov");
        user2.setCity(CityEnum.КОИЛОВЦИ);
        user2.setEmail("petko@abv.bg");
        user2.setServicePoint(pointRepository.findByName("ДЯ Мир").orElseThrow());
        user2.setPhoneNumber("58349859348535");
        user2.setPassword("testpassword");
        userRepository.save(user2);

        ChildEntity child = new ChildEntity();
        child.setFullName("Pesho");
        child.setParent(userRepository.findByEmail("petko@abv.bg").orElseThrow());
        child.setAllergies(Set.of(allergyRepository.findByAllergenName(AllergyEnum.ДРУГО).orElseThrow()));
        child.setAgeGroup(AgeGroupEnum.МАЛКИ);
        child.setBirthDate(LocalDate.of(2023, 1, 2));
        child.setCoupons(new ArrayList<>());
        child.setMedicalListURL("medicalURL");
        child.setBirthCertificateURL("certURL");
        childRepository.save(child);


        CouponEntity coupon = new CouponEntity();
        coupon.setAgeGroup(AgeGroupEnum.МАЛКИ);
        coupon.setPrice(BigDecimal.valueOf(0.8));
        coupon.setOwner(child);
        coupon.setVerifiedDate(LocalDate.now().with(DayOfWeek.FRIDAY).plusDays(4));
        couponRepository.save(coupon);

        OrderEntity order = new OrderEntity();
        order.setCoupon(coupon);
        order.setDate(order.getCoupon().getVerifiedDate());
        order.setUser(userRepository.findByEmail("petko@abv.bg").orElseThrow());
        order.setServicePoint(order.getUser().getServicePoint());
        order.setChild(child);

        orderRepository.save(order);

        CouponEntity freeAdminCoupon = couponRepository.findAll()
                                                    .stream()
                                                    .filter(c -> c.getOwner()
                                                                  .getFullName()
                                                                  .equals("TestChild"))
                                                    .findFirst()
                                                    .get();
        freeAdminCoupon.setVerifiedDate(LocalDate.now().with(DayOfWeek.FRIDAY).plusDays(10));
        couponRepository.save(freeAdminCoupon);

        OrderEntity adminOrder = new OrderEntity();
        adminOrder.setCoupon(freeAdminCoupon);
        adminOrder.setDate(adminOrder.getCoupon().getVerifiedDate());
        adminOrder.setUser(userRepository.findByEmail(ADMIN_MAIL).orElseThrow());
        adminOrder.setServicePoint(adminOrder.getUser().getServicePoint());
        adminOrder.setChild(adminOrder.getUser().getChildren().stream().findFirst().get());

        orderRepository.save(adminOrder);
    }

    public void populateMoreAllergicChildToTestAdminStatistic(){
        ChildEntity child = new ChildEntity();
        child.setFullName("Gosho");
        child.setParent(userRepository.findByEmail("petko@abv.bg").orElseThrow());
        child.setAllergies(Set.of(allergyRepository.findByAllergenName(AllergyEnum.ДРУГО).orElseThrow()));
        child.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        child.setBirthDate(LocalDate.of(2022, 11, 7));
        child.setCoupons(new ArrayList<>());
        child.setMedicalListURL("medicalURL");
        child.setBirthCertificateURL("certURL");
        childRepository.save(child);

        CouponEntity coupon = new CouponEntity();
        coupon.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        coupon.setPrice(BigDecimal.valueOf(0.8));
        coupon.setOwner(child);
        coupon.setVerifiedDate(LocalDate.now().plusDays(14));
        couponRepository.save(coupon);

        OrderEntity order = new OrderEntity();
        order.setDate(LocalDate.now().plusDays(14));
        order.setUser(userRepository.findByEmail("petko@abv.bg").orElseThrow());
        order.setCoupon(coupon);
        order.setServicePoint(order.getUser().getServicePoint());
        order.setChild(child);

        orderRepository.save(order);
    }

    public void populateOneMoreChild(){
        ChildEntity child = new ChildEntity();
        child.setFullName("Gosho");
        child.setParent(userRepository.findByEmail("petko@abv.bg").orElseThrow());
        child.setAllergies(Set.of(allergyRepository.findByAllergenName(AllergyEnum.ДРУГО).orElseThrow()));
        child.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        child.setBirthDate(LocalDate.of(2022, 11, 7));
        child.setCoupons(new ArrayList<>());
        child.setMedicalListURL("medicalURL");
        child.setBirthCertificateURL("certURL");
        childRepository.save(child);

    }
}
