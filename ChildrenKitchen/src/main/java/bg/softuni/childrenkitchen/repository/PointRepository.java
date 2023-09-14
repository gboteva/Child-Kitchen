package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {
    Optional<PointEntity> findByName(String pointName);
}
