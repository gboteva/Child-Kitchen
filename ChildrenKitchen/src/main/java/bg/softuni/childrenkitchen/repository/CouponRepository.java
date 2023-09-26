package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
}
