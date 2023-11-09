package bg.softuni.childrenkitchen.repository;

import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity, Long> {
    List<ChildEntity> findAllByParentId(Long parentId);
}
