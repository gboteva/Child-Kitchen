package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.exception.NoAvailableMenuException;
import bg.softuni.childrenkitchen.model.binding.AddMenuBindingModel;
import bg.softuni.childrenkitchen.model.entity.DailyManuEntity;
import bg.softuni.childrenkitchen.model.entity.FoodEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.view.FoodViewModel;
import bg.softuni.childrenkitchen.model.view.MenuViewModel;
import bg.softuni.childrenkitchen.repository.MenusRepository;
import bg.softuni.childrenkitchen.service.interfaces.FoodService;
import bg.softuni.childrenkitchen.service.interfaces.MenuService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenusRepository menusRepository;
    private final FoodService foodService;

    public MenuServiceImpl(MenusRepository menusRepository, FoodService foodService) {
        this.menusRepository = menusRepository;
        this.foodService = foodService;
    }

    @Override
    public void initDB() {
        if (menusRepository.count() > 0){
            return;
        }

        saveLittleMenu();
        saveBigMenu();

    }

    @Override
    public List<MenuViewModel> getWeeklyMenu() {

        List<LocalDate> mondayAndFriday = getDateOfCurrentMondayAndFriday(LocalDate.now());

        LocalDate monday = mondayAndFriday.get(0);
        LocalDate friday = mondayAndFriday.get(1);


        List<DailyManuEntity> weeklyMenu = menusRepository.findByDateBetween(monday, friday);

        if(weeklyMenu.isEmpty()){
            throw new NoAvailableMenuException();
        }

        return weeklyMenu.stream()
                                                .map(this::mapToViewModel)
                                                .collect(Collectors.toList());

    }

    @Override
    public List<LocalDate> getDateOfCurrentMondayAndFriday(LocalDate now) {
        LocalDate monday = null;
        LocalDate friday = null;

        if (now.getDayOfWeek().name().equals("MONDAY")){
            monday = now;
            friday = LocalDate.now().plusDays(5L);
        }

        if (now.getDayOfWeek().name().equals("TUESDAY")){
            monday = LocalDate.now().minusDays(1L);
            friday = LocalDate.now().plusDays(3L);
        }

        if (now.getDayOfWeek().name().equals("WEDNESDAY")){
            monday = LocalDate.now().minusDays(2L);
            friday = LocalDate.now().plusDays(2L);
        }

        if (now.getDayOfWeek().name().equals("THURSDAY")){
            monday = LocalDate.now().minusDays(3L);
            friday = LocalDate.now().plusDays(1L);
        }

        if (now.getDayOfWeek().name().equals("FRIDAY")){
            friday = now;
            monday = LocalDate.now().minusDays(4L);
        }

        if (now.getDayOfWeek().name().equals("SATURDAY")){
            friday = LocalDate.now().minusDays(1L);
            monday = LocalDate.now().minusDays(5L);
        }

        if (now.getDayOfWeek().name().equals("SUNDAY")){
            friday = LocalDate.now().minusDays(2L);
            monday = LocalDate.now().minusDays(6L);
        }

        if(monday == null || friday == null){
            throw new NullPointerException("Some of date is null!");
        }

        return List.of(monday, friday);
    }

    @Override
    public MenuViewModel createMenu(AddMenuBindingModel addMenuBindingModel) {

        DailyManuEntity menu = new DailyManuEntity();
        menu.setDate(addMenuBindingModel.getDate());
        menu.setAgeGroup(AgeGroupEnum.valueOf(addMenuBindingModel.getAgeGroup()));
        menu.setSoup(foodService.getByName(addMenuBindingModel.getSoup()).orElseThrow(ObjectNotFoundException::new));
        menu.setMain(foodService.getByName(addMenuBindingModel.getMain()).orElseThrow(ObjectNotFoundException::new));
        menu.setDessert(foodService.getByName(addMenuBindingModel.getDessert()).orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity saved = menusRepository.save(menu);

       return mapToViewModel(saved);
    }

    @Override
    public boolean existByDateAndAgeGroup(LocalDate date, AgeGroupEnum ageGroup) {
       return menusRepository.existsByDateAndAgeGroup(date, ageGroup);
    }

    @Override
    public MenuViewModel editMenu(LocalDate date, AgeGroupEnum ageGroup, AddMenuBindingModel addMenuBindingModel) {
        Optional<DailyManuEntity> byDateAndAgeGroup = menusRepository.findByDateAndAgeGroup(date, ageGroup);

        if(byDateAndAgeGroup.isEmpty()){
            return null;
        }

        DailyManuEntity menuEntity = byDateAndAgeGroup.get();
        Optional<FoodEntity> soup = foodService.getByName(addMenuBindingModel.getSoup());
        Optional<FoodEntity> main = foodService.getByName(addMenuBindingModel.getMain());
        Optional<FoodEntity> dessert= foodService.getByName(addMenuBindingModel.getDessert());

        if (soup.isEmpty() || main.isEmpty() || dessert.isEmpty()){
            throw new ObjectNotFoundException();
        }

        menuEntity.setSoup(soup.get());
        menuEntity.setMain(main.get());
        menuEntity.setDessert(dessert.get());

        DailyManuEntity edited = menusRepository.save(menuEntity);

        return mapToViewModel(edited);
    }


    @Override
    public MenuViewModel getMenuViewModelByDateAndAgeGroup(LocalDate date, AgeGroupEnum ageGroup) {
        Optional<DailyManuEntity> dailyManuEntity = menusRepository.findByDateAndAgeGroup(date, ageGroup);

        MenuViewModel viewModel = null;

        if (dailyManuEntity.isEmpty()){
            viewModel = mapToEmptyViewModel(date,ageGroup);
        }else{
            viewModel = mapToViewModel(dailyManuEntity.get());
        }

        return viewModel;

    }
    private MenuViewModel mapToEmptyViewModel(LocalDate date, AgeGroupEnum ageGroup) {
        MenuViewModel model = new MenuViewModel();
        model.setDate(date.toString());
        model.setDayOfWeek(date.getDayOfWeek().name());
        model.setAgeGroupName(ageGroup.name());
        model.setLocalDate(date);

        FoodViewModel soup = new FoodViewModel();
        soup.setName("Няма въведена супа за тази дата");

        FoodViewModel main = new FoodViewModel();
        main.setName("Няма въведено основно за тази дата");

        FoodViewModel dessert = new FoodViewModel();
        dessert.setName("Няма въведен десерт за тази дата");

        model.setSoup(soup);
        model.setMain(main);
        model.setDessert(dessert);
        return model;
    }

    @Override
    public MenuViewModel mapToViewModel(DailyManuEntity entity) {

        MenuViewModel menuViewModel = new MenuViewModel();

        String localDay = entity.getDate().getDayOfWeek().name().toLowerCase();

        String dayOfWeek = null;
        switch (localDay){
            case "monday" -> dayOfWeek = "Понеделник";
            case "tuesday" -> dayOfWeek = "Вторник";
            case "wednesday" -> dayOfWeek = "Сряда";
            case "thursday" -> dayOfWeek = "Четвъртък";
            case "friday" -> dayOfWeek = "Петък";
        }

        menuViewModel.setDayOfWeek(dayOfWeek);
        menuViewModel.setLocalDate(entity.getDate());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

        menuViewModel.setDate(", " + formatter.format(entity.getDate()));

        menuViewModel.setAgeGroupName(entity.getAgeGroup().name());

        menuViewModel.setSoup(new FoodViewModel());
        menuViewModel.getSoup().setName(entity.getSoup().getName());
        menuViewModel.getSoup().setCategoryName(entity.getSoup().getCategory().name());
        menuViewModel.getSoup().setAgeGroupName(entity.getSoup().getAgeGroup().name());
        menuViewModel.getSoup().setAllergens(entity.getSoup().getAllergens().stream()
                                                   .map(allergenEntity->allergenEntity.getName().toLowerCase())
                                                   .map(this::mapToStringWithoutUnderscore)
                                                   .collect(Collectors.joining(", ")));

        menuViewModel.setMain(new FoodViewModel());
        menuViewModel.getMain().setName(entity.getMain().getName());
        menuViewModel.getMain().setCategoryName(entity.getMain().getCategory().name());
        menuViewModel.getMain().setAgeGroupName(entity.getMain().getAgeGroup().name());
        menuViewModel.getMain().setAllergens(entity.getMain().getAllergens().stream()
                                                   .map(allergenEntity->allergenEntity.getName().toLowerCase())
                                                   .map(this::mapToStringWithoutUnderscore)
                                                   .collect(Collectors.joining(", ")));

        menuViewModel.setDessert(new FoodViewModel());
        menuViewModel.getDessert().setName(entity.getDessert().getName());
        menuViewModel.getDessert().setCategoryName(entity.getDessert().getCategory().name());
        menuViewModel.getDessert().setAgeGroupName(entity.getDessert().getAgeGroup().name());
        menuViewModel.getDessert().setAllergens(entity.getDessert().getAllergens().stream()
                                                      .map(allergenEntity->allergenEntity.getName().toLowerCase())
                                                      .map(this::mapToStringWithoutUnderscore)
                                                      .collect(Collectors.joining(", ")));

        return menuViewModel;
    }

    @Scheduled(cron = "00 00 11 20 02 *")
    public void YearlyDeleteCoupons(){
        //delete all menus with date older than 1 year before 20.01 yearly

        List<DailyManuEntity> olderThanOneYear = menusRepository.findAll()
                                                              .stream()
                                                              .filter(menu -> menu.getDate()
                                                                                      .isBefore(LocalDate.now()))
                                                              .collect(Collectors.toList());

        menusRepository.deleteAll(olderThanOneYear);
    }

    private String mapToStringWithoutUnderscore(String value){
        if(value.contains("_")){
            return value.replace("_", " ");
        }
        return value;
    }

    private void saveBigMenu() {
        List<LocalDate> mondayAndFriday = getDateOfCurrentMondayAndFriday(LocalDate.now());
        LocalDate mondayDate = mondayAndFriday.get(0);
        LocalDate fridayDate = mondayAndFriday.get(1);

        DailyManuEntity monday = new DailyManuEntity();
        monday.setDate(mondayDate);
        monday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        monday.setSoup(foodService.getByName("СУПА С ПУЕШКО МЕСО").orElseThrow(ObjectNotFoundException::new));
        monday.setMain(foodService.getByName("ТИКВИЧКИ С ОРИЗ").orElseThrow(ObjectNotFoundException::new));
        monday.setDessert(foodService.getByName("МЛЕЧЕН КИСЕЛ ОТ НЕКТАРИНИ").orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity tuesday = new DailyManuEntity();
        tuesday.setDate(mondayDate.plusDays(1));
        tuesday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        tuesday.setSoup(foodService.getByName("СУПА ОТ КАРТОФИ И МОРКОВИ").orElseThrow(ObjectNotFoundException::new));
        tuesday.setMain(foodService.getByName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ ЛЕЩА").orElseThrow(ObjectNotFoundException::new));
        tuesday.setDessert(foodService.getByName("КУС-КУС С МЛЯКО И ЯЙЦА").orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity wednesday = new DailyManuEntity();
        wednesday.setDate(mondayDate.plusDays(2));
        wednesday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        wednesday.setSoup(foodService.getByName("СУПА ОТ ТИКВИЧКИ СЪС ЗАСТРОЙКА").orElseThrow(ObjectNotFoundException::new));
        wednesday.setMain(foodService.getByName("КЮФТЕТА С БЯЛ СОС").orElseThrow(ObjectNotFoundException::new));
        wednesday.setDessert(foodService.getByName("МЛЯКО С ОРИЗ").orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity thursday = new DailyManuEntity();
        thursday.setDate(mondayDate.plusDays(3));
        thursday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        thursday.setSoup(foodService.getByName("СУПА ОТ ДОМАТИ С КАРТОФИ").orElseThrow(ObjectNotFoundException::new));
        thursday.setMain(foodService.getByName("ПИЛЕШКО МЕСО С КАРТОФИ").orElseThrow(ObjectNotFoundException::new));
        thursday.setDessert(foodService.getByName("ПШЕНИЦА С МЛЯКО").orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity friday = new DailyManuEntity();
        friday.setDate(fridayDate);
        friday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        friday.setSoup(foodService.getByName("СУПА ОТ ЗЕЛЕН ФАСУЛ СЪС ЗАСТРОЙКА").orElseThrow(ObjectNotFoundException::new));
        friday.setMain(foodService.getByName("ПИЛЕШКО МЕСО С КАРТОФИ").orElseThrow(ObjectNotFoundException::new));
        friday.setDessert(foodService.getByName("МЛЕЧЕН КИСЕЛ ОТ ПЪПЕШ").orElseThrow(ObjectNotFoundException::new));

        menusRepository.saveAll(List.of(monday, tuesday, wednesday, thursday, friday));
    }

    private void saveLittleMenu() {
        List<LocalDate> mondayAndFriday = getDateOfCurrentMondayAndFriday(LocalDate.now());
        LocalDate mondayDate = mondayAndFriday.get(0);
        LocalDate fridayDate = mondayAndFriday.get(1);

        DailyManuEntity monday = new DailyManuEntity();
        monday.setDate(mondayDate);
        monday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        monday.setSoup(foodService.getByName("СУПА С ПУЕШКО МЕСО").orElseThrow(ObjectNotFoundException::new));
        monday.setMain(foodService.getByName("ЗЕЛЕНЧУКОВО ПЮРЕ С ИЗВАРА").orElseThrow(ObjectNotFoundException::new));
        monday.setDessert(foodService.getByName("КИСЕЛ ОТ СЕЗОНЕН ПЛОД С ПРЕХОДНО МЛЯКО").orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity tuesday = new DailyManuEntity();
        tuesday.setDate(mondayDate.plusDays(1));
        tuesday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        tuesday.setSoup(foodService.getByName("СУПА ОТ КАРТОФИ И МОРКОВИ").orElseThrow(ObjectNotFoundException::new));
        tuesday.setMain(foodService.getByName("ПЮРЕ ОТ РИБА И ЗЕЛЕНЧУЦИ").orElseThrow(ObjectNotFoundException::new));
        tuesday.setDessert(foodService.getByName("ОРИЗ С ПЪПЕШ").orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity wednesday = new DailyManuEntity();
        wednesday.setDate(mondayDate.plusDays(2));
        wednesday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        wednesday.setSoup(foodService.getByName("СУПА ОТ ТИКВИЧКИ СЪС ЗАСТРОЙКА").orElseThrow((ObjectNotFoundException::new)));
        wednesday.setMain(foodService.getByName("ПЮРЕ ОТ ТЕЛЕШКО С ГРАХ").orElseThrow(ObjectNotFoundException::new));
        wednesday.setDessert(foodService.getByName("ПЛОДОВО ПЮРЕ С ИЗВАРА").orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity thursday = new DailyManuEntity();
        thursday.setDate(mondayDate.plusDays(3));
        thursday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        thursday.setSoup(foodService.getByName("СУПА ОТ ДОМАТИ С КАРТОФИ").orElseThrow(ObjectNotFoundException::new));
        thursday.setMain(foodService.getByName("ПЮРЕ ОТ ПИЛЕШКО СЪС ЗЕЛЕНЧУЦИ").orElseThrow(ObjectNotFoundException::new));
        thursday.setDessert(foodService.getByName("КИСЕЛ ОТ ЯБЪЛКИ С ПРЕХОДНО МЛЯКО").orElseThrow(ObjectNotFoundException::new));

        DailyManuEntity friday = new DailyManuEntity();
        friday.setDate(fridayDate);
        friday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        friday.setSoup(foodService.getByName("СУПА ОТ ЗЕЛЕН ФАСУЛ СЪС ЗАСТРОЙКА").orElseThrow(ObjectNotFoundException::new));
        friday.setMain(foodService.getByName("ПЮРЕ КУС-КУС СЪС ЗАЕШКО МЕСО И ЗЕЛЕНЧУЦИ").orElseThrow(ObjectNotFoundException::new));
        friday.setDessert(foodService.getByName("ПЛОДОВА КАША ОТ ГРИС").orElseThrow(ObjectNotFoundException::new));

        menusRepository.saveAll(List.of(monday, tuesday, wednesday, thursday, friday));
    }
}
