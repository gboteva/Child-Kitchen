package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.AllergyEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import bg.softuni.childrenkitchen.repository.AllergyRepository;
import bg.softuni.childrenkitchen.service.AllergyService;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        AllergyEntity gluten = new AllergyEntity();
        gluten.setAllergenName(AllergyEnum.ГЛУТЕН);

        AllergyEntity egg = new AllergyEntity();
        egg.setAllergenName(AllergyEnum.ЯЙЦА);

        AllergyEntity lactose = new AllergyEntity();
        lactose.setAllergenName(AllergyEnum.ЛАКТОЗА);

        AllergyEntity milk = new AllergyEntity();
        milk.setAllergenName(AllergyEnum.МЛЯКО);

        AllergyEntity bird = new AllergyEntity();
        bird.setAllergenName(AllergyEnum.ПТИЧЕ_МЕСО);

        AllergyEntity veal = new AllergyEntity();
        veal.setAllergenName(AllergyEnum.ТЕЛЕШКО_МЕСО);

        AllergyEntity fish = new AllergyEntity();
        fish.setAllergenName(AllergyEnum.РИБА);

        AllergyEntity other = new AllergyEntity();
        other.setAllergenName(AllergyEnum.ДРУГО);

        AllergyEntity none = new AllergyEntity();
        none.setAllergenName(AllergyEnum.НЯМА);

        allergyRepository.save(gluten);
        allergyRepository.save(egg);
        allergyRepository.save(milk);
        allergyRepository.save(bird);
        allergyRepository.save(veal);
        allergyRepository.save(fish);
        allergyRepository.save(other);
        allergyRepository.save(none);
        allergyRepository.save(lactose);
    }

    @Override
    public Optional<AllergyEntity> findByName(AllergyEnum allergyName) {
        return allergyRepository.findByAllergenName(allergyName);
    }
}
