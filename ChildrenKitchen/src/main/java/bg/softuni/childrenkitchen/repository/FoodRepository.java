package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.FoodEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
    Optional<FoodEntity> findByName(String foodName);

    List<FoodEntity> findAllByCategoryAndAgeGroup(FoodCategoryEnum foodCategoryEnum, AgeGroupEnum ageGroup);
}
