package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.DailyManuEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenusRepository extends JpaRepository<DailyManuEntity, Long> {
    List<DailyManuEntity> findByDateBetween(LocalDate monday, LocalDate friday);

    Optional<DailyManuEntity> findByDateAndAgeGroup(LocalDate date, AgeGroupEnum ageGroup);

    boolean existsByDateAndAgeGroup(LocalDate date, AgeGroupEnum ageGroup);
}
