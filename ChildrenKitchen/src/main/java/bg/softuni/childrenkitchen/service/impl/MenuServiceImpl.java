package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.DailyManuEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.repository.MenusRepository;
import bg.softuni.childrenkitchen.service.FoodService;
import bg.softuni.childrenkitchen.service.MenuService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    private void saveBigMenu() {
        DailyManuEntity monday = new DailyManuEntity();
        monday.setDate(LocalDate.of(2023, 9, 4));
        monday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        monday.setSoup(foodService.getByName("СУПА С ПУЕШКО МЕСО").orElse(null));
        monday.setMain(foodService.getByName("ТИКВИЧКИ С ОРИЗ").orElse(null));
        monday.setDessert(foodService.getByName("МЛЕЧЕН КИСЕЛ ОТ НЕКТАРИНИ").orElse(null));

        DailyManuEntity tuesday = new DailyManuEntity();
        tuesday.setDate(LocalDate.of(2023, 9, 5));
        tuesday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        tuesday.setSoup(foodService.getByName("СУПА ОТ КАРТОФИ И МОРКОВИ").orElse(null));
        tuesday.setMain(foodService.getByName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ ЛЕЩА").orElse(null));
        tuesday.setDessert(foodService.getByName("КУС-КУС С МЛЯКО И ЯЙЦА").orElse(null));

        DailyManuEntity wednesday = new DailyManuEntity();
        wednesday.setDate(LocalDate.of(2023, 9, 6));
        wednesday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        wednesday.setSoup(foodService.getByName("СУПА ОТ ТИКВИЧКИ СЪС ЗАСТРОЙКА").orElse(null));
        wednesday.setMain(foodService.getByName("КЮФТЕТА С БЯЛ СОС").orElse(null));
        wednesday.setDessert(foodService.getByName("МЛЯКО С ОРИЗ").orElse(null));

        DailyManuEntity thursday = new DailyManuEntity();
        thursday.setDate(LocalDate.of(2023, 9, 7));
        thursday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        thursday.setSoup(foodService.getByName("СУПА ОТ ДОМАТИ С КАРТОФИ").orElse(null));
        thursday.setMain(foodService.getByName("ПИЛЕШКО МЕСО С КАРТОФИ").orElse(null));
        thursday.setDessert(foodService.getByName("ПШЕНИЦА С МЛЯКО").orElse(null));

        DailyManuEntity friday = new DailyManuEntity();
        friday.setDate(LocalDate.of(2023, 9, 8));
        friday.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        friday.setSoup(foodService.getByName("СУПА ОТ ЗЕЛЕН ФАСУЛ СЪС ЗАСТРОЙКА").orElse(null));
        friday.setMain(foodService.getByName("ПИЛЕШКО МЕСО С КАРТОФИ").orElse(null));
        friday.setDessert(foodService.getByName("МЛЕЧЕН КИСЕЛ ОТ ПЪПЕШ").orElse(null));

        menusRepository.saveAll(List.of(monday, tuesday, wednesday, thursday, friday));
    }

    private void saveLittleMenu() {
        DailyManuEntity monday = new DailyManuEntity();
        monday.setDate(LocalDate.of(2023, 9, 4));
        monday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        monday.setSoup(foodService.getByName("СУПА С ПУЕШКО МЕСО").orElse(null));
        monday.setMain(foodService.getByName("ЗЕЛЕНЧУКОВО ПЮРЕ С ИЗВАРА").orElse(null));
        monday.setDessert(foodService.getByName("КИСЕЛ ОТ СЕЗОНЕН ПЛОД С ПРЕХОДНО МЛЯКО").orElse(null));

        DailyManuEntity tuesday = new DailyManuEntity();
        tuesday.setDate(LocalDate.of(2023, 9, 5));
        tuesday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        tuesday.setSoup(foodService.getByName("СУПА ОТ КАРТОФИ И МОРКОВИ").orElse(null));
        tuesday.setMain(foodService.getByName("ПЮРЕ ОТ РИБА И ЗЕЛЕНЧУЦИ").orElse(null));
        tuesday.setDessert(foodService.getByName("ОРИЗ С ПЪПЕШ").orElse(null));

        DailyManuEntity wednesday = new DailyManuEntity();
        wednesday.setDate(LocalDate.of(2023, 9, 6));
        wednesday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        wednesday.setSoup(foodService.getByName("СУПА ОТ ТИКВИЧКИ СЪС ЗАСТРОЙКА").orElse(null));
        wednesday.setMain(foodService.getByName("ПЮРЕ ОТ ТЕЛЕШКО С ГРАХ").orElse(null));
        wednesday.setDessert(foodService.getByName("ПЛОДОВО ПЮРЕ С ИЗВАРА").orElse(null));

        DailyManuEntity thursday = new DailyManuEntity();
        thursday.setDate(LocalDate.of(2023, 9, 7));
        thursday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        thursday.setSoup(foodService.getByName("СУПА ОТ ДОМАТИ С КАРТОФИ").orElse(null));
        thursday.setMain(foodService.getByName("ПЮРЕ ОТ ПИЛЕШКО СЪС ЗЕЛЕНЧУЦИ").orElse(null));
        thursday.setDessert(foodService.getByName("КИСЕЛ ОТ ЯБЪЛКИ С ПРЕХОДНО МЛЯКО").orElse(null));

        DailyManuEntity friday = new DailyManuEntity();
        friday.setDate(LocalDate.of(2023, 9, 8));
        friday.setAgeGroup(AgeGroupEnum.МАЛКИ);
        friday.setSoup(foodService.getByName("СУПА ОТ ЗЕЛЕН ФАСУЛ СЪС ЗАСТРОЙКА").orElse(null));
        friday.setMain(foodService.getByName("ПЮРЕ КУС-КУС СЪС ЗАЕШКО МЕСО И ЗЕЛЕНЧУЦИ").orElse(null));
        friday.setDessert(foodService.getByName("ПЛОДОВА КАША ОТ ГРИС").orElse(null));

        menusRepository.saveAll(List.of(monday, tuesday, wednesday, thursday, friday));
    }
}
