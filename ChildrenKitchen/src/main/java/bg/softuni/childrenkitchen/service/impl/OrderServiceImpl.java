package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.binding.AdminSearchBindingModel;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.OrderEntity;
import bg.softuni.childrenkitchen.model.entity.PointEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import bg.softuni.childrenkitchen.model.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
import bg.softuni.childrenkitchen.model.view.OrderViewModel;
import bg.softuni.childrenkitchen.model.view.ReferenceByPointsViewModel;
import bg.softuni.childrenkitchen.model.view.ReferenceAllPointsViewModel;
import bg.softuni.childrenkitchen.repository.OrderRepository;
import bg.softuni.childrenkitchen.service.*;
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
    private final AllergyService allergyService;


    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, MenuService menuService, CouponService couponService, PointService pointService, AllergyService allergyService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.menuService = menuService;
        this.couponService = couponService;
        this.pointService = pointService;
        this.allergyService = allergyService;
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

        OrderEntity mishoOrder = new OrderEntity();
        UserEntity mamaUser = userService.getByEmail("mama@test.com")
                                         .orElseThrow(ObjectNotFoundException::new);
        mishoOrder.setUser(mamaUser);
        mishoOrder.setChild(userService.getChildByNames("МИХАИЛ ИВАНОВ ИВАНОВ", mamaUser.getEmail()));
        fillOrderInfo(mishoOrder);

        orderRepository.saveAll(List.of(first, second, admin, mishoOrder));
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
                             .findFirst()
                             .orElseThrow(ObjectNotFoundException::new));

        order.setServicePoint(order.getUser()
                                   .getServicePoint());
        order.setDate(order.getCoupon()
                           .getVerifiedDate());

        order.setMenu(menuService.getMenuByDateAndAgeGroup(order.getCoupon()
                                                                .getVerifiedDate(), order.getChild()
                                                                                         .getAgeGroup()));
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


        return allPointsMap.values().stream().toList();


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

                if (!order.getChild().getAllergies().contains(allergyService.findByName(AllergyEnum.НЯМА).orElseThrow(ObjectNotFoundException::new))) {

                    AllergicChildViewModel allergicChildViewModel = mapToAllergicChildViewModel(order.getChild());

                    if(model.getAllergicChildren().isEmpty() || model.getAllergicChildren() == null){
                        model.setAllergicChildren(List.of(allergicChildViewModel));
                    }else{
                        List<AllergicChildViewModel> allergicChildren = model.getAllergicChildren();
                        model.setAllergicChildren(allergicChildren);
                    }

                }

                allPointsMap.put(order.getServicePoint()
                                      .getName(), model);

            }else{
                ReferenceAllPointsViewModel saved = allPointsMap.get(order.getServicePoint()
                                                                          .getName());
                saved.setSmallOrderCount(order.getChild()
                                              .getAgeGroup()
                                              .equals(AgeGroupEnum.МАЛКИ) ? saved.getSmallOrderCount() + 1 : saved.getSmallOrderCount());
                saved.setBigOrderCount(order.getChild()
                                            .getAgeGroup()
                                            .equals(AgeGroupEnum.ГОЛЕМИ) ? saved.getBigOrderCount() + 1 : saved.getBigOrderCount());
                if (!order.getChild()
                          .getAllergies()
                          .contains(allergyService.findByName(AllergyEnum.НЯМА).orElseThrow(ObjectNotFoundException::new))) {

                    AllergicChildViewModel allergicChildViewModel = mapToAllergicChildViewModel(order.getChild());


                    List<AllergicChildViewModel> allergicChildren = saved.getAllergicChildren();

                    List<AllergicChildViewModel> newAllergicList = new ArrayList<>(allergicChildren);

                    if(allergicChildren.isEmpty() || allergicChildren == null){
                        saved.setAllergicChildren(List.of(allergicChildViewModel));
                    }else{
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
        AllergicChildViewModel allergicChildViewModel = new AllergicChildViewModel();

        allergicChildViewModel.setFullName(allergicChild.getFullName());

        allergicChildViewModel.setServicePoint(allergicChild.getParent()
                                                            .getServicePoint()
                                                            .getName());

        allergicChildViewModel.setAgeGroup(allergicChild.getAgeGroup());

        allergicChildViewModel.setAllergies(allergicChild
                .getAllergies()
                .stream()
                .map(allergyEntity -> allergyEntity.getAllergenName()
                                                   .name())
                .collect(Collectors.joining(", ")));
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
                                                                        .filter(o ->
                                                                                !o.getChild()
                                                                                  .getAllergies()
                                                                                  .contains(allergyService.findByName(AllergyEnum.НЯМА)
                                                                                                          .orElseThrow(ObjectNotFoundException::new))

                                                                        )
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
                                                                    .filter(o ->
                                                                            !o.getChild()
                                                                              .getAllergies()
                                                                              .contains(allergyService.findByName(AllergyEnum.НЯМА)
                                                                                                      .orElseThrow(ObjectNotFoundException::new))

                                                                    )
                                                                    .collect(Collectors.toList())));

        byPointBig.setCountAllergicOrders(byPointBig.getAllergicChild()
                                                    .size());

        infoForOnePointAllGroups.put("ГОЛЕМИ", byPointBig);

        return infoForOnePointAllGroups;
    }


    @Override
    public OrderViewModel makeOrder(LocalDate date, String servicePointName, String userEmail, String childFullName) {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setDate(date);

        orderEntity.setServicePoint(pointService.getByName(servicePointName)
                                                .orElseThrow(ObjectNotFoundException::new));

        orderEntity.setUser(userService.getByEmail(userEmail)
                                       .orElseThrow(ObjectNotFoundException::new));

        orderEntity.setChild(userService.getChildByNames(childFullName, userEmail));

        orderEntity.setCoupon(couponService.getAndVerifyCoupon(userEmail, childFullName, date));

        //todo Ако няма добавено меню за датата -> грешка
        //Защо заявката трябва да знае за менюто?

        orderEntity.setMenu(menuService.getMenuByDateAndAgeGroup(date, orderEntity.getCoupon().getAgeGroup()));

        OrderEntity saved = orderRepository.save(orderEntity);

        OrderViewModel orderViewModel = new OrderViewModel();
        orderViewModel.setChildNames(saved.getChild()
                                          .getFullName());
        orderViewModel.setServicePointName(saved.getServicePoint()
                                                .getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        orderViewModel.setDate(saved.getDate().format(formatter));
        orderViewModel.setMenuViewModel(menuService.mapToViewModel(saved.getMenu()));

        return orderViewModel;
    }

    @Override
    public List<LocalDate> getOrdersOfChild(String childName, String userEmail) {
        ChildEntity childByNames = userService.getChildByNames(childName, userEmail);

        List<OrderEntity> allByChildFullName = orderRepository.findAllByChildFullName(childName);

        return allByChildFullName.stream()
                                 .map(OrderEntity::getDate)
                                 .collect(Collectors.toList());

    }

    @Override
    public void deleteOrder(LocalDate deleteOrderDate, String childName) {
        Optional<OrderEntity> orderToDelete = orderRepository.findByChildFullNameAndDate(childName, deleteOrderDate);

        if (orderToDelete.isEmpty()) {
            throw new ObjectNotFoundException();
        }

        orderRepository.delete(orderToDelete.get());
    }

    @Override
    public List<OrderViewModel> getOrdersFromToday(String userEmail) {
        Optional<List<OrderEntity>> allByUserEmail = orderRepository.findAllByUserEmail(userEmail);

        if(allByUserEmail.isEmpty()){
            throw new ObjectNotFoundException();
        }

       List<OrderEntity> sorted = allByUserEmail.get().stream()
                      .filter(o -> o.getCoupon()
                                    .getVerifiedDate() != null
                                    &&
                              o.getCoupon().getVerifiedDate().isAfter(LocalDate.now().minusDays(1L)))
                      .sorted((a, b) -> b.getCoupon()
                                      .getVerifiedDate()
                                      .compareTo(a.getCoupon()
                                                  .getVerifiedDate()))
               .collect(Collectors.toList());

        return    sorted.stream().map(this::mapToOrderViewModel)
                .collect(Collectors.toList());
    }

    private OrderViewModel mapToOrderViewModel(OrderEntity order) {
        OrderViewModel orderViewModel = new OrderViewModel();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        orderViewModel.setDate(order.getDate().format(formatter));
        orderViewModel.setChildNames(order.getChild().getFullName());
        orderViewModel.setServicePointName(order.getServicePoint().getName());
        orderViewModel.setRemainingCouponsCount(order.getChild().getCoupons().stream().filter(c -> c.getVerifiedDate()==null).collect(Collectors.toList()).size());

        orderViewModel.setMenuViewModel(menuService.mapToViewModel(menuService.getMenuByDateAndAgeGroup(order.getDate(), order.getCoupon()
                                                                                                                              .getAgeGroup())));
        return orderViewModel;
    }

    private List<AllergicChildViewModel> getListOfAllergicChild(List<OrderEntity> allOrdersPerServicePoint) {
        return allOrdersPerServicePoint.stream()
                                       .filter(order -> !order.getChild()
                                                              .getAllergies()
                                                              .contains(allergyService.findByName(AllergyEnum.НЯМА)
                                                                                      .orElseThrow(ObjectNotFoundException::new))
                                       )
                                       .map(OrderEntity::getChild)
                                       .map(child -> {
                                           AllergicChildViewModel allergicChildViewModel = new AllergicChildViewModel();
                                           allergicChildViewModel.setFullName(child.getFullName());
                                           allergicChildViewModel.setServicePoint(child.getParent()
                                                                                       .getServicePoint()
                                                                                       .getName());
                                           allergicChildViewModel.setAgeGroup(child.getAgeGroup());
                                           allergicChildViewModel.setAllergies(child.getAllergies()
                                                                                    .stream()
                                                                                    .map(a -> a.getAllergenName()
                                                                                               .name())
                                                                                    .collect(Collectors.joining(", ")));

                                           return allergicChildViewModel;
                                       })
                                       .collect(Collectors.toList());
    }


}
