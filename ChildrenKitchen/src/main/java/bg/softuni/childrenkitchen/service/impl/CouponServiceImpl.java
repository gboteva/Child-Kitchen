package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.CouponEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.exception.NoAvailableCouponsException;
import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.service.BuyCouponsServiceModel;
import bg.softuni.childrenkitchen.repository.CouponRepository;
import bg.softuni.childrenkitchen.service.CouponService;
import bg.softuni.childrenkitchen.service.MenuService;
import bg.softuni.childrenkitchen.service.UserService;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public CouponServiceImpl(CouponRepository couponRepository, UserService userService, MenuService menuService, ModelMapper modelMapper) {
        this.couponRepository = couponRepository;
        this.userService = userService;
        this.menuService = menuService;
        this.modelMapper = modelMapper;
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
    public int buyCoupons(BuyCouponsServiceModel serviceModel) {
        List<CouponEntity> couponsToBuy = createCoupons(serviceModel);

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
    public Long unverifyCoupon(Long couponId) {
        Optional<CouponEntity> couponToUnverify = couponRepository.findById(couponId);


        if (couponToUnverify.isEmpty()){
            throw new ObjectNotFoundException();
        }

        CouponEntity couponEntity = couponToUnverify.get();
        couponEntity.setVerifiedDate(null);

        couponRepository.save(couponEntity);

        return couponEntity.getId();
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


    private List<CouponEntity> createCoupons(BuyCouponsServiceModel buyCouponServiceModel) {
        int countCoupons = buyCouponServiceModel.getCountCoupons();

        Optional<UserEntity> userEntity = userService.getByEmail(buyCouponServiceModel.getParentEmail());

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
                                                                 .equals(buyCouponServiceModel.getChildName()))
                                              .limit(1).toList()
                                                .get(0);

        List<CouponEntity> coupons = new ArrayList<>();

        for (int i = 0; i < countCoupons; i++) {
            CouponEntity couponEntity = new CouponEntity();

            couponEntity.setOwner(child);
            couponEntity.setPrice(buyCouponServiceModel.getPrice());
            couponEntity.setAgeGroup(buyCouponServiceModel.getAgeGroup());

            coupons.add(couponEntity);
        }

        return coupons;
    }


    @Scheduled(cron = "00 00 11 20 02 *")
    public void YearlyDeleteCoupons(){
        //delete all coupons with verify date older than 1 year before 20.01 yearly

        List<CouponEntity> olderThanOneYear = couponRepository.findAll()
                                                     .stream()
                                                     .filter(coupon -> coupon.getVerifiedDate()
                                                                             .isBefore(LocalDate.now()))
                                                     .collect(Collectors.toList());

        couponRepository.deleteAll(olderThanOneYear);
    }



}
