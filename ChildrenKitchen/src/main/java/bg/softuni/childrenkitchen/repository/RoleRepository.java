package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.RoleEntity;
import bg.softuni.childrenkitchen.model.entity.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(UserRoleEnum userRoleEnum);
}
