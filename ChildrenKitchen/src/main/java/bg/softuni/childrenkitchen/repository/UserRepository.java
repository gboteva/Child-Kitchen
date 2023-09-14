package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String mail);

    boolean existsByEmail(String email);
}
