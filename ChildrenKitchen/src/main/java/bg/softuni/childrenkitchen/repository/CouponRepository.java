package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    List<CouponEntity> findAllByVerifiedDateAndOwnerFullName(LocalDate verifyDate, String childName);
}
