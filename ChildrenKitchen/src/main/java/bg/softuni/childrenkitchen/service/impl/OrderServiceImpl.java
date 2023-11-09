package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.binding.AdminSearchBindingModel;
import bg.softuni.childrenkitchen.model.entity.*;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.view.*;
import bg.softuni.childrenkitchen.repository.OrderRepository;
import bg.softuni.childrenkitchen.service.interfaces.*;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final MenuService menuService;
    private final CouponService couponService;
    private final PointService pointService;
    private final ModelMapper modelMapper;


    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, MenuService menuService, CouponService couponService, PointService pointService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.menuService = menuService;
        this.couponService = couponService;
        this.pointService = pointService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initDB() {

        if (orderRepository.count() > 0) {
            return;
        }

        OrderEntity first = new OrderEntity();
        first.setUser(userService.getByEmail("mama@test.com")
                                 .orElseThrow(ObjectNotFoundException::new));
        fillOrderInfo(first);

        OrderEntity second = new OrderEntity();
        second.setUser(userService.getByEmail("mama2@test.com")
                                  .orElseThrow(ObjectNotFoundException::new));
        fillOrderInfo(second);

        OrderEntity admin = new OrderEntity();
        admin.setUser(userService.getByEmail("admin@test.com")
                                 .orElseThrow(ObjectNotFoundException::new));
        fillOrderInfo(admin);



        orderRepository.saveAll(List.of(first, second, admin));
    }

    private void fillOrderInfo(OrderEntity order) {
        if (order.getChild() == null) {
            order.setChild(order.getUser()
                                .getChildren()
                                .stream()
                                .findFirst()
                                .orElseThrow(ObjectNotFoundException::new));
        }


        order.setCoupon(order.getChild()
                             .getCoupons()
                             .stream()
                             .filter(c -> c.getVerifiedDate() !=null)
                             .findFirst()
                             .orElseThrow(ObjectNotFoundException::new));


        order.setServicePoint(order.getUser()
                                   .getServicePoint());
        order.setDate(order.getCoupon()
                           .getVerifiedDate());

    }

    @Override
    public List<ReferenceAllPointsViewModel> getReferenceForAllPoints(AdminSearchBindingModel adminSearchBindingModel) {
        LocalDate fromDate = adminSearchBindingModel.getFromDate();
        LocalDate toDate = adminSearchBindingModel.getToDate();

        List<OrderEntity> orders;

        if (adminSearchBindingModel.getAgeGroup()
                                   .equals("All")) {
            orders = orderRepository.findAllByDateBetween(fromDate, toDate);
        } else {
            AgeGroupEnum ageGroup = AgeGroupEnum.valueOf(adminSearchBindingModel.getAgeGroup());
            orders = orderRepository.findAllByDateBetweenAndChildAgeGroup(fromDate, toDate, ageGroup);
        }

        Map<String, ReferenceAllPointsViewModel> allPointsMap = getMapForAllOrders(orders);


        return allPointsMap.values()
                           .stream()
                           .toList();


    }

    private Map<String, ReferenceAllPointsViewModel> getMapForAllOrders(List<OrderEntity> orders) {
        Map<String, ReferenceAllPointsViewModel> allPointsMap = new HashMap<>();

        for (OrderEntity order : orders) {
            if (allPointsMap.get(order.getServicePoint()
                                      .getName()) == null) {
                ReferenceAllPointsViewModel model = new ReferenceAllPointsViewModel();

                model.setAgeGroup(order.getChild()
                                       .getAgeGroup());
                model.setServicePoint(order.getServicePoint()
                                           .getName());
                model.setSmallOrderCount(order.getChild()
                                              .getAgeGroup()
                                              .equals(AgeGroupEnum.МАЛКИ) ? 1 : 0);
                model.setBigOrderCount(order.getChild()
                                            .getAgeGroup()
                                            .equals(AgeGroupEnum.ГОЛЕМИ) ? 1 : 0);

                if (order.getChild().isAllergic()) {

                    AllergicChildViewModel allergicChildViewModel = mapToAllergicChildViewModel(order.getChild());

                    if (model.getAllergicChildren()
                             .isEmpty() || model.getAllergicChildren() == null) {
                        model.setAllergicChildren(List.of(allergicChildViewModel));
                    } else {
                        List<AllergicChildViewModel> allergicChildren = model.getAllergicChildren();
                        model.setAllergicChildren(allergicChildren);
                    }

                }

                allPointsMap.put(order.getServicePoint()
                                      .getName(), model);

            } else {
                ReferenceAllPointsViewModel saved = allPointsMap.get(order.getServicePoint()
                                                                          .getName());
                saved.setSmallOrderCount(order.getChild()
                                              .getAgeGroup()
                                              .equals(AgeGroupEnum.МАЛКИ) ? saved.getSmallOrderCount() + 1 : saved.getSmallOrderCount());
                saved.setBigOrderCount(order.getChild()
                                            .getAgeGroup()
                                            .equals(AgeGroupEnum.ГОЛЕМИ) ? saved.getBigOrderCount() + 1 : saved.getBigOrderCount());

                if (order.getChild().isAllergic()) {

                    AllergicChildViewModel allergicChildViewModel = mapToAllergicChildViewModel(order.getChild());

                    List<AllergicChildViewModel> allergicChildren = saved.getAllergicChildren();

                    List<AllergicChildViewModel> newAllergicList = new ArrayList<>(allergicChildren);

                    if (allergicChildren.isEmpty() || allergicChildren == null) {
                        saved.setAllergicChildren(List.of(allergicChildViewModel));
                    } else {
                        newAllergicList.add(allergicChildViewModel);
                        saved.setAllergicChildren(newAllergicList);
                    }
                }

                allPointsMap.put(saved.getServicePoint(), saved);

            }
        }

        return allPointsMap;
    }


    private AllergicChildViewModel mapToAllergicChildViewModel(ChildEntity allergicChild) {
        AllergicChildViewModel allergicChildViewModel =  modelMapper.map(allergicChild, AllergicChildViewModel.class);
        allergicChildViewModel.setServicePoint(allergicChild.getParent().getServicePoint().getName());

        return allergicChildViewModel;
    }

    @Override
    public List<ReferenceByPointsViewModel> getReferenceForPoint(AdminSearchBindingModel adminSearchBindingModel) {
        LocalDate fromDate = adminSearchBindingModel.getFromDate();

        LocalDate toDate = adminSearchBindingModel.getToDate();

        PointEntity pointEntity = pointService.getByName(adminSearchBindingModel.getServicePoint())
                                              .orElseThrow(ObjectNotFoundException::new);

        AgeGroupEnum ageGroup = null;

        if (!adminSearchBindingModel.getAgeGroup()
                                    .equals("All")) {
            ageGroup = AgeGroupEnum.valueOf(adminSearchBindingModel.getAgeGroup());
        }


        List<OrderEntity> allOrdersPerPointAndAge = new ArrayList<>();


        if (ageGroup == null) {
            List<OrderEntity> allOrdersPerPoint = orderRepository.findAllByDateBetweenAndServicePoint(fromDate, toDate, pointEntity);

            Map<String, ReferenceByPointsViewModel> infoForOnePointAllGroups = getInfoFromOnePointsAllGroups(fromDate, toDate, pointEntity, allOrdersPerPoint);

            return infoForOnePointAllGroups.values()
                                           .stream()
                                           .toList();

        } else {
            allOrdersPerPointAndAge = orderRepository.findAllByDateBetweenAndServicePointAndChildAgeGroup(fromDate, toDate, pointEntity, ageGroup);
            Map<String, ReferenceByPointsViewModel> infoForOnePointAndOneGroup = new LinkedHashMap<>();
            ReferenceByPointsViewModel byPoint = new ReferenceByPointsViewModel();

            byPoint.setFromDate(fromDate);
            byPoint.setToDate(toDate);
            byPoint.setAgeGroup(ageGroup.name());
            byPoint.setPoint(pointEntity.getName());
            byPoint.setCountOrders(allOrdersPerPointAndAge.size());
            byPoint.setAllergicChild(getListOfAllergicChild(allOrdersPerPointAndAge));
            byPoint.setCountAllergicOrders(byPoint.getAllergicChild()
                                                  .size());
            infoForOnePointAndOneGroup.put(ageGroup.name(), byPoint);

            return infoForOnePointAndOneGroup.values()
                                             .stream()
                                             .toList();
        }


    }

    private Map<String, ReferenceByPointsViewModel> getInfoFromOnePointsAllGroups(LocalDate fromDate, LocalDate toDate, PointEntity pointEntity, List<OrderEntity> allOrdersPerPoint) {
        Map<String, ReferenceByPointsViewModel> infoForOnePointAllGroups = new LinkedHashMap<>();

        List<OrderEntity> smallOrders = allOrdersPerPoint.stream()
                                                         .filter(o -> o.getChild()
                                                                       .getAgeGroup()
                                                                       .name()
                                                                       .equals("МАЛКИ"))
                                                         .collect(Collectors.toList());

        ReferenceByPointsViewModel byPointSmall = new ReferenceByPointsViewModel();
        byPointSmall.setFromDate(fromDate);
        byPointSmall.setToDate(toDate);
        byPointSmall.setAgeGroup(AgeGroupEnum.МАЛКИ.name());
        byPointSmall.setPoint(pointEntity.getName());
        byPointSmall.setCountOrders(smallOrders.size());


        byPointSmall.setAllergicChild(getListOfAllergicChild(smallOrders.stream()
                                                                        .filter(o -> o.getChild().isAllergic())
                                                                        .collect(Collectors.toList())));

        byPointSmall.setCountAllergicOrders(byPointSmall.getAllergicChild()
                                                        .size());

        infoForOnePointAllGroups.put("МАЛКИ", byPointSmall);

        //_________________________________BIG__________________________________________

        infoForOnePointAllGroups.put(AgeGroupEnum.ГОЛЕМИ.name(), null);

        List<OrderEntity> bigOrders = allOrdersPerPoint.stream()
                                                       .filter(o -> o.getChild()
                                                                     .getAgeGroup()
                                                                     .name()
                                                                     .equals("ГОЛЕМИ"))
                                                       .collect(Collectors.toList());

        ReferenceByPointsViewModel byPointBig = new ReferenceByPointsViewModel();
        byPointBig.setFromDate(fromDate);
        byPointBig.setToDate(toDate);
        byPointBig.setAgeGroup(AgeGroupEnum.ГОЛЕМИ.name());
        byPointBig.setPoint(pointEntity.getName());
        byPointBig.setCountOrders(bigOrders.size());


        byPointBig.setAllergicChild(getListOfAllergicChild(bigOrders.stream()
                                                                    .filter(o ->o.getChild().isAllergic())
                                                                    .collect(Collectors.toList())));

        byPointBig.setCountAllergicOrders(byPointBig.getAllergicChild()
                                                    .size());

        infoForOnePointAllGroups.put("ГОЛЕМИ", byPointBig);

        return infoForOnePointAllGroups;
    }


    @Override
    public OrderViewModel makeOrder(LocalDate date, String servicePointName, String userEmail, String childFullName, String loggedInUserEmail) {

        UserEntity loggedInUser = userService.getByEmail(loggedInUserEmail).orElseThrow(ObjectNotFoundException::new);

        UserEntity userToAddOrder = loggedInUser;

        if(!userEmail.equals(loggedInUserEmail)){
            userToAddOrder = userService.getByEmail(userEmail).orElseThrow(ObjectNotFoundException::new);
        }

        //only admin may add more than one order per day
        if (!userService.isAdmin(loggedInUser)){
            List<OrderEntity> allByDate = orderRepository.findAllByDateAndChildFullName(date, childFullName);
            if (!allByDate.isEmpty()){
                return null;
            }
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setDate(date);
        orderEntity.setServicePoint(pointService.getByName(servicePointName)
                                                .orElseThrow(ObjectNotFoundException::new));

        orderEntity.setUser(userToAddOrder);

        orderEntity.setCoupon(couponService.getAndVerifyCoupon(userEmail, childFullName, date));

        orderEntity.setChild(userService.getChildByNames(childFullName, userEmail));

        OrderEntity saved = orderRepository.save(orderEntity);

        OrderViewModel orderViewModel = new OrderViewModel();
        orderViewModel.setChildNames(saved.getChild()
                                          .getFullName());
        orderViewModel.setServicePointName(saved.getServicePoint()
                                                .getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        orderViewModel.setDate(saved.getDate()
                                    .format(formatter));

       MenuViewModel menuViewModel = menuService.getMenuViewModelByDateAndAgeGroup(orderEntity.getDate(), orderEntity.getChild().getAgeGroup());
       orderViewModel.setMenuViewModel(menuViewModel);

       orderViewModel.setRemainingCouponsCount(saved
               .getChild().getCoupons().stream()
               .filter(c -> c.getVerifiedDate()==null).toList().size());

        return orderViewModel;
    }

    @Override
    public List<LocalDate> getOrdersDateOfChild(String childName, String userEmail) {

        List<OrderEntity> allByChildFullName = orderRepository.findAllByChildFullName(childName);

        return allByChildFullName.stream()
                                 .map(OrderEntity::getDate)
                                 .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(LocalDate deleteOrderDate, String childName) {
        List<OrderEntity> ordersToDelete = orderRepository.findAllByChildFullNameAndDate(childName, deleteOrderDate);

        if (ordersToDelete.isEmpty()) {
            throw new ObjectNotFoundException();
        }

        if (ordersToDelete.size() == 1){
            couponService.unverifiedCoupon(ordersToDelete.get(0).getCoupon().getId());
            orderRepository.delete(ordersToDelete.get(0));
        }else {
            OrderEntity toDelete = ordersToDelete.get(ordersToDelete.size()-1);
            couponService.unverifiedCoupon(toDelete.getCoupon().getId());
            orderRepository.delete(toDelete);
        }

    }

    @Override
    public List<OrderViewModel> getActiveOrders(String userEmail) {
        Optional<List<OrderEntity>> allByUserEmail = orderRepository.findAllByUserEmail(userEmail);

        if (allByUserEmail.isEmpty()) {
            return new ArrayList<>();
        }


        List<OrderEntity> sorted = allByUserEmail.get()
                                                 .stream()
                                                 .filter(o -> o.getCoupon()
                                                               .getVerifiedDate() != null
                                                         &&
                                                         o.getCoupon()
                                                          .getVerifiedDate()
                                                          .isAfter(LocalDate.now()
                                                                            .minusDays(1L)))
                                                 .sorted((a, b) -> b.getCoupon()
                                                                    .getVerifiedDate()
                                                                    .compareTo(a.getCoupon()
                                                                                .getVerifiedDate()))
                                                 .toList();

        return sorted.stream()
                     .map(this::mapToOrderViewModel)
                     .collect(Collectors.toList());
    }

    @Override
    public void deleteAllOrderByUserId(Long userId) {
        List<OrderEntity> allByUserId = orderRepository.findAllByUserId(userId);

        orderRepository.deleteAll(allByUserId);
    }


    @Scheduled(cron = "00 00 00 20 02 *")
    public void yearlyDeleteOrdersOlderThanOneYear() {
        List<OrderEntity> olderThanOneYear = orderRepository.findAll()
                                                              .stream()
                                                              .filter(order -> order.getCoupon().getVerifiedDate()
                                                                            .isBefore(LocalDate.now()))
                                                              .collect(Collectors.toList());

        orderRepository.deleteAll(olderThanOneYear);
    }

    private OrderViewModel mapToOrderViewModel(OrderEntity order) {
        OrderViewModel orderViewModel = new OrderViewModel();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        orderViewModel.setDate(order.getDate()
                                    .format(formatter));
        orderViewModel.setChildNames(order.getChild()
                                          .getFullName());
        orderViewModel.setServicePointName(order.getServicePoint()
                                                .getName());
        orderViewModel.setRemainingCouponsCount(order.getChild()
                                                     .getCoupons()
                                                     .stream()
                                                     .filter(c -> c.getVerifiedDate() == null)
                                                     .toList()
                                                     .size());

        MenuViewModel menuViewModel = menuService.getMenuViewModelByDateAndAgeGroup(order.getDate(), order.getCoupon()
                                                                                                          .getAgeGroup());
        orderViewModel.setMenuViewModel(menuViewModel);

        return orderViewModel;
    }

    private List<AllergicChildViewModel> getListOfAllergicChild(List<OrderEntity> allOrdersPerServicePoint) {
        return allOrdersPerServicePoint.stream()
                                       .map(OrderEntity::getChild)
                                       .filter(ChildEntity::isAllergic)
                                       .map(this::mapToAllergicChildViewModel)
                                       .collect(Collectors.toList());
    }


}
