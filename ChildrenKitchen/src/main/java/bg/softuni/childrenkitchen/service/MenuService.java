package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.binding.AddMenuBindingModel;
import bg.softuni.childrenkitchen.model.entity.DailyManuEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.view.MenuViewModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuService {
    void initDB();

    List<MenuViewModel> getWeeklyMenu();

    Optional<DailyManuEntity> getMenuByDateAndAgeGroup(LocalDate date, AgeGroupEnum ageGroup);
    MenuViewModel getMenuViewModelByDateAndAgeGroup(LocalDate date, AgeGroupEnum ageGroup);

    MenuViewModel mapToViewModel(DailyManuEntity entity);

    List<LocalDate> getDateOfCurrentMondayAndFriday(LocalDate fromNow);

    MenuViewModel createMenu(AddMenuBindingModel addMenuBindingModel);
    boolean existByDateAndAgeGroup(LocalDate date, AgeGroupEnum ageGroup);

    MenuViewModel editMenu(LocalDate date, AgeGroupEnum ageGroup, AddMenuBindingModel addMenuBindingModel);
}
