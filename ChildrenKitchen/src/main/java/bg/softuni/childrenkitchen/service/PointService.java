package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.entity.PointEntity;
import bg.softuni.childrenkitchen.model.view.PointViewModel;

import java.util.Optional;
import java.util.Set;

public interface PointService {
    void initDB();
    Optional<PointEntity> getByName(String pointName);

    Set<String> getAllNames();
    Set<PointViewModel> getAll();
}
