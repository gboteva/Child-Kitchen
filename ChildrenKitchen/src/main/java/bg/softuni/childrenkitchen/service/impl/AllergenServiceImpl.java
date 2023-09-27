package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.AllergenEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergensEnum;
import bg.softuni.childrenkitchen.repository.AllergenRepository;
import bg.softuni.childrenkitchen.service.AllergenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllergenServiceImpl implements AllergenService {
    private final AllergenRepository allergenRepository;

    public AllergenServiceImpl(AllergenRepository allergenRepository) {
        this.allergenRepository = allergenRepository;
    }

    @Override
    public void initDB() {
        if (allergenRepository.count() > 0) {
            return;
        }
        AllergenEntity gluten = new AllergenEntity();
        gluten.setName(AllergensEnum.ГЛУТЕН.name());
        AllergenEntity cotagge = new AllergenEntity();
        cotagge.setName(AllergensEnum.ИЗВАРА.name());
        AllergenEntity cheese = new AllergenEntity();
        cheese.setName(AllergensEnum.КАШКАВАЛ.name());
        AllergenEntity fish = new AllergenEntity();
        fish.setName(AllergensEnum.РИБА.name());
        AllergenEntity eggs = new AllergenEntity();
        eggs.setName(AllergensEnum.ЯЙЦА.name());
        AllergenEntity yoghurt = new AllergenEntity();
        yoghurt.setName(AllergensEnum.КИСЕЛО_МЛЯКО.name());
        AllergenEntity milk = new AllergenEntity();
        milk.setName(AllergensEnum.ПРЯСНО_МЛЯКО.name());
        AllergenEntity butter = new AllergenEntity();
        butter.setName(AllergensEnum.КРАВЕ_МАСЛО.name());
        AllergenEntity cheese_nature = new AllergenEntity();
        cheese_nature.setName(AllergensEnum.СИРЕНЕ.name());
        AllergenEntity celery = new AllergenEntity();
        celery.setName(AllergensEnum.ЦЕЛИНА.name());

        allergenRepository.saveAll(List.of(gluten, cotagge, cheese, fish, eggs, yoghurt, milk, butter, cheese_nature, celery));
    }

    @Override
    public Optional<AllergenEntity> findById(Long id) {
        return allergenRepository.findById(id);
    }

    @Override
    public Optional<AllergenEntity> findByName(String allergenName) {

        return allergenRepository.findByName(allergenName);
    }

    @Override
    public AllergenEntity createAllergenEntity(String allergenName) {
        AllergenEntity allergenEntity = new AllergenEntity();

        allergenEntity.setName(allergenName);

        return allergenRepository.save(allergenEntity);
    }
}
