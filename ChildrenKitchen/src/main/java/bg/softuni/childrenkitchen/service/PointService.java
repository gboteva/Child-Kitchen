package bg.softuni.childrenkitchen.service;

import bg.softuni.childrenkitchen.model.entity.PointEntity;

import java.util.Optional;
import java.util.Set;

public interface PointService {
    void initDB();
    Optional<PointEntity> getByName(String pointName);

    Set<String> getAll();
}
