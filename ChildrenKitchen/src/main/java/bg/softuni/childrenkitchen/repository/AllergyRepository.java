package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.AllergyEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllergyRepository extends JpaRepository<AllergyEntity, Long> {
    Optional<AllergyEntity> findByAllergenName(AllergyEnum allergyName);
}
