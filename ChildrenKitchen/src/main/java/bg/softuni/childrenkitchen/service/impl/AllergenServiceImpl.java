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
        if (allergenRepository.count() > 0){
            return;
        }
        AllergenEntity gluten = new AllergenEntity();
        gluten.setName(AllergensEnum.ГЛУТЕН);
        AllergenEntity cotagge = new AllergenEntity();
        cotagge.setName(AllergensEnum.ИЗВАРА);
        AllergenEntity cheese = new AllergenEntity();
        cheese.setName(AllergensEnum.КАШКАВАЛ);
        AllergenEntity fish = new AllergenEntity();
        fish.setName(AllergensEnum.РИБА);
        AllergenEntity eggs = new AllergenEntity();
        eggs.setName(AllergensEnum.ЯЙЦА);
        AllergenEntity yoghurt = new AllergenEntity();
        yoghurt.setName(AllergensEnum.КИСЕЛО_МЛЯКО);
        AllergenEntity milk = new AllergenEntity();
        milk.setName(AllergensEnum.ПРЯСНО_МЛЯКО);
        AllergenEntity butter = new AllergenEntity();
        butter.setName(AllergensEnum.КРАВЕ_МАСЛО);
        AllergenEntity cheese_nature = new AllergenEntity();
        cheese_nature.setName(AllergensEnum.СИРЕНЕ);
        AllergenEntity celery = new AllergenEntity();
        celery.setName(AllergensEnum.ЦЕЛИНА);

        allergenRepository.saveAll(List.of(gluten, cotagge, cheese, fish, eggs, yoghurt, milk, butter, cheese_nature, celery));
    }

    @Override
    public Optional<AllergenEntity> findById(Long id) {
        return allergenRepository.findById(id);
    }
}
