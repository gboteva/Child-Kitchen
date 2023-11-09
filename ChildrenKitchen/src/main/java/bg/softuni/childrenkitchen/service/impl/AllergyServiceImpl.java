package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.AllergyEntity;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import bg.softuni.childrenkitchen.repository.AllergyRepository;
import bg.softuni.childrenkitchen.service.interfaces.AllergyService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;

    public AllergyServiceImpl(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    @Override
    public void initDB() {
        if (allergyRepository.count() > 0){
            return;
        }
        AllergyEntity gluten = new AllergyEntity().setAllergenName(AllergyEnum.ГЛУТЕН);

        AllergyEntity egg = new AllergyEntity().setAllergenName(AllergyEnum.ЯЙЦА);

        AllergyEntity lactose = new AllergyEntity().setAllergenName(AllergyEnum.ЛАКТОЗА);

        AllergyEntity milk = new AllergyEntity().setAllergenName(AllergyEnum.МЛЯКО);

        AllergyEntity bird = new AllergyEntity().setAllergenName(AllergyEnum.ПТИЧЕ_МЕСО);

        AllergyEntity veal = new AllergyEntity().setAllergenName(AllergyEnum.ТЕЛЕШКО_МЕСО);

        AllergyEntity fish = new AllergyEntity().setAllergenName(AllergyEnum.РИБА);

        AllergyEntity other = new AllergyEntity().setAllergenName(AllergyEnum.ДРУГО);

        AllergyEntity none = new AllergyEntity().setAllergenName(AllergyEnum.НЯМА);

        allergyRepository.saveAll(List.of(gluten, egg, milk, bird, veal, fish, other, none, lactose));

    }

    @Override
    public Optional<AllergyEntity> findByName(AllergyEnum allergyName) {
        return allergyRepository.findByAllergenName(allergyName);
    }

    @Override
    public void deleteAllAllergiesByOwner(Set<ChildEntity> children) {

        for(ChildEntity child : children){
            child.getAllergies().removeAll(child.getAllergies());
        }

    }

}
