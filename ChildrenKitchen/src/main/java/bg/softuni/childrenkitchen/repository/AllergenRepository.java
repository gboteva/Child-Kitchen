package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.AllergenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergenRepository extends JpaRepository<AllergenEntity, Long> {
}
