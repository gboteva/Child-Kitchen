package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.AllergenEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AllergensEnum;
import bg.softuni.childrenkitchen.repository.AllergenRepository;
import bg.softuni.childrenkitchen.service.interfaces.AllergenService;
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
        AllergenEntity gluten = new AllergenEntity().setName(AllergensEnum.ГЛУТЕН.name());
        AllergenEntity cottage = new AllergenEntity().setName(AllergensEnum.ИЗВАРА.name());
        AllergenEntity cheese = new AllergenEntity().setName(AllergensEnum.КАШКАВАЛ.name());
        AllergenEntity fish = new AllergenEntity().setName(AllergensEnum.РИБА.name());
        AllergenEntity eggs = new AllergenEntity().setName(AllergensEnum.ЯЙЦА.name());
        AllergenEntity yoghurt = new AllergenEntity().setName(AllergensEnum.КИСЕЛО_МЛЯКО.name());
        AllergenEntity milk = new AllergenEntity().setName(AllergensEnum.ПРЯСНО_МЛЯКО.name());
        AllergenEntity butter = new AllergenEntity().setName(AllergensEnum.КРАВЕ_МАСЛО.name());
        AllergenEntity cheese_nature = new AllergenEntity().setName(AllergensEnum.СИРЕНЕ.name());
        AllergenEntity celery = new AllergenEntity().setName(AllergensEnum.ЦЕЛИНА.name());

        allergenRepository.saveAll(List.of(gluten, cottage, cheese, fish, eggs, yoghurt, milk, butter, cheese_nature, celery));
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
        AllergenEntity allergenEntity = new AllergenEntity().setName(allergenName);

        return allergenRepository.save(allergenEntity);
    }
}
