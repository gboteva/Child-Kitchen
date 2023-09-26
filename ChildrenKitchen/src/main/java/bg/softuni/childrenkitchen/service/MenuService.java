package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.entity.DailyManuEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.view.MenuViewModel;
import java.time.LocalDate;
import java.util.List;

public interface MenuService {
    void initDB();

    List<MenuViewModel> getWeeklyMenu();

    DailyManuEntity getMenuByDateAndAgeGroup(LocalDate date, AgeGroupEnum ageGroup);

    MenuViewModel mapToViewModel(DailyManuEntity entity);

    List<LocalDate> getDateOfCurrentMondayAndFriday(LocalDate fromNow);
}
