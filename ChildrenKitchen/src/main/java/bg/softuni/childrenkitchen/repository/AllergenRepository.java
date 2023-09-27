package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.AllergenEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergensEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllergenRepository extends JpaRepository<AllergenEntity, Long> {
    Optional<AllergenEntity> findByName(String allergenName);
}
