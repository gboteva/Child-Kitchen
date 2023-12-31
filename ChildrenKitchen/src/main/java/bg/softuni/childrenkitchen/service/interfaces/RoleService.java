package bg.softuni.childrenkitchen.service.interfaces;

import bg.softuni.childrenkitchen.model.entity.RoleEntity;

import java.util.Optional;

public interface RoleService {
    void initDB();

    Optional<RoleEntity> getRole(String role);
}
