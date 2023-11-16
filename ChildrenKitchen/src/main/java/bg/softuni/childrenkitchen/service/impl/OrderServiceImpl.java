package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.view.ReferenceViewModel;
import bg.softuni.childrenkitchen.model.binding.AdminSearchBindingModel;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.OrderEntity;
import bg.softuni.childrenkitchen.model.entity.UserEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
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
                             .filter(c -> c.getVerifiedDate() != null)
                             .findFirst()
                             .orElseThrow(ObjectNotFoundException::new));


        order.setServicePoint(order.getUser()
                                   .getServicePoint());
        order.setDate(order.getCoupon()
                           .getVerifiedDate());

    }

    @Override
    public List<ReferenceViewModel> getAdminReference(AdminSearchBindingModel adminSearchBindingModel) {
        LocalDate fromDate = adminSearchBindingModel.getFromDate();
        LocalDate toDate = adminSearchBindingModel.getToDate();
        List<OrderEntity> allOrderEntities = orderRepository.findAllByDateBetween(fromDate, toDate);

        allOrderEntities = filterByAssignment(allOrderEntities, adminSearchBindingModel.getServicePoint(), adminSearchBindingModel.getAgeGroup());

        List<ReferenceViewModel> viewModel;

        if (!adminSearchBindingModel.getServicePoint().equals("All")) {
            //sort by ageGroup
            viewModel = createListByAgeGroups(allOrderEntities, adminSearchBindingModel);

        } else {
            //sort by servicePoint
            viewModel = createListByPoints(allOrderEntities, adminSearchBindingModel);
        }

        return viewModel;
    }

    private List<ReferenceViewModel> createListByPoints(List<OrderEntity> allOrderEntities, AdminSearchBindingModel adminSearchBindingModel) {

        List<String> allPointsWithOrders = allOrderEntities.stream().map(o->o.getServicePoint().getName())
                .distinct().toList();

        List<ReferenceViewModel> toReturn = new ArrayList<>();

        allPointsWithOrders.forEach(point -> {
            ReferenceViewModel viewModel = new ReferenceViewModel();
            viewModel.setFromDate(adminSearchBindingModel.getFromDate());
            viewModel.setToDate(adminSearchBindingModel.getToDate());
            viewModel.setAgeGroup(null);
            viewModel.setPoint(point);
            viewModel.setCountSmallOrders(allOrderEntities.stream()
                                                          .filter(o->o.getChild().getAgeGroup().name()
                                                                      .equals(AgeGroupEnum.МАЛКИ.name()))
                                                          .filter(o->o.getServicePoint().getName().equals(point))
                                                            .toList().size());
            viewModel.setCountBigOrders(allOrderEntities.stream()
                                                        .filter(o->o.getChild().getAgeGroup().name()
                                                                    .equals(AgeGroupEnum.ГОЛЕМИ.name()))
                                                        .filter(o->o.getServicePoint().getName().equals(point))
                                                        .toList().size());
            viewModel.setTotalCountOrders(viewModel.getCountSmallOrders() + viewModel.getCountBigOrders());

            viewModel.setCountAllergicOrders(allOrderEntities.stream()
                                                             .filter(o->o.getServicePoint().getName().equals(point))
                                                             .map(OrderEntity::getChild)
                                                             .filter(ChildEntity::isAllergic)
                                                              .toList().size());

            toReturn.add(viewModel);
        });

        toReturn.get(0).setAllAllergicChildren(allOrderEntities.stream().map(OrderEntity::getChild)
                .filter(ChildEntity::isAllergic)
                        .distinct()
                .map(this::mapToAllergicChildViewModel)
                .collect(Collectors.toList())
        );

        return toReturn;
    }

    private List<ReferenceViewModel> createListByAgeGroups(List<OrderEntity> allOrderEntities, AdminSearchBindingModel adminSearchBindingModel) {
        String ageGroup = adminSearchBindingModel.getAgeGroup();

        List<ReferenceViewModel> list;

        if (ageGroup.equals("All")) {
            //2 models
            list = createListFromAllAgeGroups(allOrderEntities, adminSearchBindingModel);

        } else {
            //1 model
            list = createListFromOneAgeGroup(allOrderEntities, adminSearchBindingModel);
        }

        return list;
    }

    private List<ReferenceViewModel> createListFromOneAgeGroup(List<OrderEntity> allOrderEntities, AdminSearchBindingModel adminSearchBindingModel) {
        String ageGroup = adminSearchBindingModel.getAgeGroup();
        String servicePoint = adminSearchBindingModel.getServicePoint();
        LocalDate from = adminSearchBindingModel.getFromDate();
        LocalDate to = adminSearchBindingModel.getToDate();

        List<ReferenceViewModel> list = new ArrayList<>();

        ReferenceViewModel viewModel = new ReferenceViewModel();
        viewModel.setAgeGroup(ageGroup.equals("МАЛКИ") ? "Small" : "Big");
        viewModel.setPoint(servicePoint);
        viewModel.setFromDate(from);
        viewModel.setToDate(to);

        if (ageGroup.equals(AgeGroupEnum.МАЛКИ.name())) {
            viewModel.setCountBigOrders(0);
            viewModel.setCountSmallOrders(allOrderEntities.size());
        } else {
            viewModel.setCountSmallOrders(0);
            viewModel.setCountBigOrders(allOrderEntities.size());
        }

        viewModel.setTotalCountOrders(viewModel.getCountSmallOrders() + viewModel.getCountBigOrders());

        viewModel.setCountAllergicOrders(allOrderEntities.stream()
                                                         .map(OrderEntity::getChild)
                                                         .filter(ChildEntity::isAllergic)
                                                         .toList()
                                                         .size()
        );


        viewModel.setAllAllergicChildren(allOrderEntities.stream()
                                                         .map(OrderEntity::getChild)
                                                         .filter(ChildEntity::isAllergic)
                                                         .distinct()
                                                         .map(this::mapToAllergicChildViewModel)
                                                         .toList()

        );

        list.add(viewModel);

        return list;
    }

    private List<ReferenceViewModel> createListFromAllAgeGroups(List<OrderEntity> allOrderEntities, AdminSearchBindingModel adminSearchBindingModel) {
        String servicePoint = adminSearchBindingModel.getServicePoint();
        LocalDate from = adminSearchBindingModel.getFromDate();
        LocalDate to = adminSearchBindingModel.getToDate();

        List<ReferenceViewModel> list = new ArrayList<>();

        Arrays.stream(AgeGroupEnum.values()).map(AgeGroupEnum::name)
                .forEach(age -> {
                    ReferenceViewModel view = new ReferenceViewModel();
                    view.setAgeGroup(age.equals("МАЛКИ") ? "Small" : "Big");
                    view.setPoint(servicePoint);
                    view.setFromDate(from);
                    view.setToDate(to);

                    if (age.equals(AgeGroupEnum.МАЛКИ.name())){
                        view.setCountBigOrders(0);
                        view.setCountSmallOrders(allOrderEntities.stream()
                                                                 .filter(o -> o.getChild()
                                                                               .getAgeGroup()
                                                                               .name()
                                                                               .equals(age))
                                                                 .toList()
                                                                 .size());
                    }else{
                        view.setCountSmallOrders(0);
                        view.setCountBigOrders(allOrderEntities.stream()
                                                               .filter(o -> o.getChild()
                                                                             .getAgeGroup()
                                                                             .name()
                                                                             .equals(age))
                                                               .toList()
                                                               .size());
                    }

                    view.setTotalCountOrders(view.getCountSmallOrders() + view.getCountBigOrders());
                    view.setCountAllergicOrders(allOrderEntities.stream()
                                                                 .map(OrderEntity::getChild)
                                                                 .filter(ChildEntity::isAllergic)
                                                                 .filter(c -> c.getAgeGroup()
                                                                               .name()
                                                                               .equals(age))
                                                                 .toList()
                                                                 .size());

                    view.setAllAllergicChildren(allOrderEntities.stream()
                                                                 .map(OrderEntity::getChild)
                                                                 .filter(ChildEntity::isAllergic)
                                                                 .filter(c -> c.getAgeGroup()
                                                                               .name()
                                                                               .equals(age))
                                                                 .distinct()
                                                                 .map(this::mapToAllergicChildViewModel)
                                                                 .toList());

                    list.add(view);
                });

        return list;
    }


    private List<OrderEntity> filterByAssignment(List<OrderEntity> allOrderEntities, String servicePoint, String ageGroup) {

        if (ageGroup.equals("All") && servicePoint.equals("All")) {
            return allOrderEntities;
        }

        if (!ageGroup.equals("All") && servicePoint.equals("All")) {
            return allOrderEntities.stream()
                                   .filter(o -> o.getChild()
                                                 .getAgeGroup()
                                                 .name()
                                                 .equals(ageGroup))
                                   .toList();
        }

        if (ageGroup.equals("All") && !servicePoint.equals("All")) {
            return allOrderEntities.stream()
                                   .filter(o -> o.getServicePoint()
                                                 .getName()
                                                 .equals(servicePoint))
                                   .toList();
        }

        if (!ageGroup.equals("All") && !servicePoint.equals("All")) {
            return allOrderEntities.stream()
                                   .filter(o -> o.getServicePoint()
                                                 .getName()
                                                 .equals(servicePoint)
                                           && o.getChild()
                                               .getAgeGroup()
                                               .name()
                                               .equals(ageGroup))
                                   .toList();
        }

        return allOrderEntities;
    }



    private AllergicChildViewModel mapToAllergicChildViewModel(ChildEntity allergicChild) {
        AllergicChildViewModel allergicChildViewModel = modelMapper.map(allergicChild, AllergicChildViewModel.class);
        allergicChildViewModel.setServicePoint(allergicChild.getParent()
                                                            .getServicePoint()
                                                            .getName());

        return allergicChildViewModel;
    }


    @Override
    public OrderViewModel makeOrder(LocalDate date, String servicePointName, String userEmail, String childFullName, String loggedInUserEmail) {

        UserEntity loggedInUser = userService.getByEmail(loggedInUserEmail)
                                             .orElseThrow(ObjectNotFoundException::new);

        UserEntity userToAddOrder = loggedInUser;

        if (!userEmail.equals(loggedInUserEmail)) {
            userToAddOrder = userService.getByEmail(userEmail)
                                        .orElseThrow(ObjectNotFoundException::new);
        }

        //only admin may add more than one order per day
        if (!userService.isAdmin(loggedInUser)) {
            List<OrderEntity> allByDate = orderRepository.findAllByDateAndChildFullName(date, childFullName);
            if (!allByDate.isEmpty()) {
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

        MenuViewModel menuViewModel = menuService.getMenuViewModelByDateAndAgeGroup(orderEntity.getDate(), orderEntity.getChild()
                                                                                                                      .getAgeGroup());
        orderViewModel.setMenuViewModel(menuViewModel);

        orderViewModel.setRemainingCouponsCount(saved
                .getChild()
                .getCoupons()
                .stream()
                .filter(c -> c.getVerifiedDate() == null)
                .toList()
                .size());

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

        if (ordersToDelete.size() == 1) {
            couponService.unverifiedCoupon(ordersToDelete.get(0)
                                                         .getCoupon()
                                                         .getId());
            orderRepository.delete(ordersToDelete.get(0));
        } else {
            OrderEntity toDelete = ordersToDelete.get(ordersToDelete.size() - 1);
            couponService.unverifiedCoupon(toDelete.getCoupon()
                                                   .getId());
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
                                                            .filter(order -> order.getCoupon()
                                                                                  .getVerifiedDate()
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


}
