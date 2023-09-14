package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.DailyManuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenusRepository extends JpaRepository<DailyManuEntity, Long> {

}
