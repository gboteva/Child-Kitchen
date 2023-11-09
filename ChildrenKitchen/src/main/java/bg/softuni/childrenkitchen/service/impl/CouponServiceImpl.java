package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.binding.BuyCouponsBindingModel;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.CouponEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.exception.NoAvailableCouponsException;
import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.repository.CouponRepository;
import bg.softuni.childrenkitchen.service.interfaces.CouponService;
import bg.softuni.childrenkitchen.service.interfaces.MenuService;
import bg.softuni.childrenkitchen.service.interfaces.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final UserService userService;
    private final MenuService menuService;

    public CouponServiceImpl(CouponRepository couponRepository, UserService userService, MenuService menuService) {
        this.couponRepository = couponRepository;
        this.userService = userService;
        this.menuService = menuService;
    }


    @Override
    public void initDB() {

        List<LocalDate> mondayAndFriday = menuService.getDateOfCurrentMondayAndFriday(LocalDate.now());
        LocalDate monday = mondayAndFriday.get(0);

        if (couponRepository.count() > 0){
            return;
        }

        CouponEntity couponEntity1 = new CouponEntity();

        couponEntity1.setOwner(userService.getByEmail("mama@test.com")
                                         .orElseThrow(ObjectNotFoundException::new)
                                         .getChildren()
                                         .stream()
                                         .filter(child -> child.getFullName().contains("МИХАИЛ"))
                                          .findFirst().orElseThrow(ObjectNotFoundException::new));


        couponEntity1.setVerifiedDate(monday);
        couponEntity1.setAgeGroup(couponEntity1.getOwner()
                                             .getAgeGroup());
        couponEntity1.setPrice(BigDecimal.valueOf(0.80));


        CouponEntity goshoCoupon = new CouponEntity();
        goshoCoupon.setOwner(userService.getByEmail("mama@test.com")
                                          .orElseThrow(ObjectNotFoundException::new)
                                          .getChildren()
                                          .stream()
                                          .filter(child -> child.getFullName().contains("ГЕОРГИ"))
                                          .findFirst().orElseThrow(ObjectNotFoundException::new));

        goshoCoupon.setVerifiedDate(monday.plusDays(1));
        goshoCoupon.setAgeGroup(goshoCoupon.getOwner()
                                               .getAgeGroup());
        goshoCoupon.setPrice(BigDecimal.valueOf(0.80));


        CouponEntity couponEntity2 = new CouponEntity();

        couponEntity2.setOwner(userService.getByEmail("mama2@test.com")
                                          .orElseThrow(ObjectNotFoundException::new)
                                          .getChildren()
                                          .stream()
                                          .findFirst()
                                          .orElseThrow(ObjectNotFoundException::new));

        couponEntity2.setVerifiedDate(monday.plusDays(2));
        couponEntity2.setAgeGroup(couponEntity1.getOwner()
                                               .getAgeGroup());
        couponEntity2.setPrice(BigDecimal.valueOf(0.80));

        CouponEntity admin = new CouponEntity();

        admin.setOwner(userService.getByEmail("admin@test.com")
                                          .orElseThrow(ObjectNotFoundException::new)
                                          .getChildren()
                                          .stream()
                                          .findFirst()
                                          .orElseThrow(ObjectNotFoundException::new));

        admin.setVerifiedDate(monday.plusDays(4));
        admin.setAgeGroup(couponEntity1.getOwner()
                                               .getAgeGroup());
        admin.setPrice(BigDecimal.valueOf(0.80));

        couponRepository.saveAll(List.of(couponEntity1, couponEntity2, admin, goshoCoupon));
    }

    @Override
    public int buyCoupons(BuyCouponsBindingModel buyCouponsBindingModel, String parentEmail) {

        List<CouponEntity> couponsToBuy = createCoupons(buyCouponsBindingModel, parentEmail);

        return couponRepository.saveAll(couponsToBuy).size();
    }

    @Override
    public CouponEntity getAndVerifyCoupon(String userEmail, String childName, LocalDate forDate) {

        ChildEntity childEntity = userService.getChildByNames(childName, userEmail);

        CouponEntity nextFreeCoupon = getNextFreeCoupon(childEntity);

        nextFreeCoupon.setVerifiedDate(forDate);

        couponRepository.save(nextFreeCoupon);

        return nextFreeCoupon;
    }

    @Override
    public Long unverifiedCoupon(Long couponId) {
        Optional<CouponEntity> couponToUnverified = couponRepository.findById(couponId);


        if (couponToUnverified.isEmpty()){
            throw new ObjectNotFoundException();
        }

        CouponEntity couponEntity = couponToUnverified.get();
        couponEntity.setVerifiedDate(null);

        couponRepository.save(couponEntity);

        return couponEntity.getId();
    }

    @Override
    public void deleteCouponsByOwnerId(Long ownerId) {
        couponRepository.deleteAllByOwnerId(ownerId);
    }


    private CouponEntity getNextFreeCoupon(ChildEntity child) {

        Optional<CouponEntity> coupon = child.getCoupons()
                                            .stream()
                                            .filter(couponEntity -> couponEntity.getVerifiedDate() == null)
                                            .findFirst();
        if (coupon.isEmpty()){
            throw new NoAvailableCouponsException();
        }

        return coupon.get();
    }


    private List<CouponEntity> createCoupons(BuyCouponsBindingModel buyCouponsBindingModel, String parentEmail) {
        int countCoupons = buyCouponsBindingModel.getCountCoupons();

        Optional<UserEntity> userEntity = userService.getByEmail(parentEmail);

        if(userEntity.isEmpty()){
            throw new ObjectNotFoundException();
        }

        if(userEntity.get().getChildren().isEmpty()){
            throw new ObjectNotFoundException();
        }

        ChildEntity child = userEntity.get()
                                              .getChildren()
                                              .stream()
                                              .filter(childEntity ->
                                                      childEntity.getFullName()
                                                                 .equals(buyCouponsBindingModel.getChildName()))
                                              .limit(1).toList()
                                                .get(0);

        List<CouponEntity> coupons = new ArrayList<>();

        String priceString = buyCouponsBindingModel.getPrice();
        priceString = priceString.substring(0, priceString.indexOf(" "));
        priceString = priceString.replace(",", ".");

        double price = Double.parseDouble(priceString);
        AgeGroupEnum ageGroupEnum = AgeGroupEnum.valueOf(buyCouponsBindingModel.getAgeGroupName());

        for (int i = 0; i < countCoupons; i++) {
            CouponEntity couponEntity = new CouponEntity();

            couponEntity.setOwner(child);
            couponEntity.setPrice(BigDecimal.valueOf(price));
            couponEntity.setAgeGroup(ageGroupEnum);

            coupons.add(couponEntity);
        }

        return coupons;
    }


    @Scheduled(cron = "00 30 00 20 02 *")
    public void yearlyDeleteCoupons(){

        List<CouponEntity> olderThanOneYear = couponRepository.findAll()
                                                     .stream()
                                                     .filter(coupon -> coupon.getVerifiedDate()!=null &&
                                                             coupon.getVerifiedDate()
                                                                   .isBefore(LocalDate.now()))
                                                     .collect(Collectors.toList());

        couponRepository.deleteAll(olderThanOneYear);
    }



}
