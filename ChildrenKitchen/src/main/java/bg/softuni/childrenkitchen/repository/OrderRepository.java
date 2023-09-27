package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.OrderEntity;
import bg.softuni.childrenkitchen.model.entity.PointEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByDateBetween(LocalDate from, LocalDate to);

    List<OrderEntity>findAllByDateBetweenAndServicePointAndChildAgeGroup(LocalDate from, LocalDate to, PointEntity point, AgeGroupEnum ageGroup);

    List<OrderEntity>findAllByDateBetweenAndServicePoint(LocalDate from, LocalDate to, PointEntity point);

    List<OrderEntity> findAllByDateBetweenAndChildAgeGroup(LocalDate from, LocalDate to, AgeGroupEnum ageGroup);

    List<OrderEntity> findAllByChildFullName(String childFullName);

    Optional<OrderEntity> findByChildFullNameAndDate(String childFullName, LocalDate date);

    Optional<List<OrderEntity>> findAllByUserEmail(String userEmail);
}
