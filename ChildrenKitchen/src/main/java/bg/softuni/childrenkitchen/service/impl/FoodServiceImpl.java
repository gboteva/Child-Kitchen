package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.entity.AllergenEntity;
import bg.softuni.childrenkitchen.model.entity.FoodEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
import bg.softuni.childrenkitchen.repository.FoodRepository;
import bg.softuni.childrenkitchen.service.AllergenService;
import bg.softuni.childrenkitchen.service.FoodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final AllergenService allergenService;

    public FoodServiceImpl(FoodRepository foodRepository, AllergenService allergenService) {
        this.foodRepository = foodRepository;
        this.allergenService = allergenService;
    }

    @Override
    public void initDB() {
        if (foodRepository.count() > 0){
            return;
        }

        AllergenEntity gluten = allergenService.findById(1L).orElse(null);
        AllergenEntity cotagge = allergenService.findById(2L).orElse(null);
        AllergenEntity cheese = allergenService.findById(3L).orElse(null);
        AllergenEntity fish = allergenService.findById(4L).orElse(null);
        AllergenEntity eggs = allergenService.findById(5L).orElse(null);
        AllergenEntity yoghurt = allergenService.findById(6L).orElse(null);
        AllergenEntity milk = allergenService.findById(7L).orElse(null);
        AllergenEntity butter = allergenService.findById(8L).orElse(null);
        AllergenEntity cheeseNature = allergenService.findById(9L).orElse(null);
        AllergenEntity celery = allergenService.findById(10L).orElse(null);

        saveSoups(gluten, cotagge, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

        savePurees(gluten, cotagge, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

        saveLittleDesserts(gluten, cotagge, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

        saveBigMain(gluten, cotagge, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

        saveBigDesserts(gluten, cotagge, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

    }

    @Override
    public Optional<FoodEntity> getByName(String foodName) {
        return foodRepository.findByName(foodName);
    }

    private void saveBigDesserts(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity food1 = new FoodEntity();
        food1.setName("ЯБЪЛКОВ СЛАДКИШ");
        food1.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food1.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food1.setAllergens(Set.of(gluten, eggs));

        FoodEntity food2 = new FoodEntity();
        food2.setName("РУЛО С КОНФИТЮР");
        food2.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food2.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food2.setAllergens(Set.of(eggs, gluten));

        FoodEntity food3 = new FoodEntity();
        food3.setName("МАКАРОНИ С МЛЯКО И ЯЙЦА");
        food3.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food3.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food3.setAllergens(Set.of(butter, milk, eggs));

        FoodEntity food4 = new FoodEntity();
        food4.setName("КУС-КУС С МЛЯКО И ЯЙЦА");
        food4.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food4.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food4.setAllergens(Set.of(butter, milk, eggs));

        FoodEntity food5 = new FoodEntity();
        food5.setName("ГРИС ХАЛВА");
        food5.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food5.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food5.setAllergens(Set.of(gluten, butter));

        FoodEntity food6 = new FoodEntity();
        food6.setName("МЛЕЧЕН КИСЕЛ ОТ БАНАНИ");
        food6.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food6.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food6.setAllergens(Set.of(milk));

        FoodEntity food7 = new FoodEntity();
        food7.setName("МЛЕЧЕН КИСЕЛ ОТ ПРАСКОВИ");
        food7.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food7.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food7.setAllergens(Set.of(milk));

        FoodEntity food8 = new FoodEntity();
        food8.setName("МЛЕЧЕН КИСЕЛ ОТ КАЙСИИ");
        food8.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food8.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food8.setAllergens(Set.of(milk));

        FoodEntity food9 = new FoodEntity();
        food9.setName("МЛЕЧЕН КИСЕЛ ОТ НЕКТАРИНИ");
        food9.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food9.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food9.setAllergens(Set.of(milk));

        FoodEntity food10 = new FoodEntity();
        food10.setName("МЛЕЧЕН КИСЕЛ ОТ ПЪПЕШ");
        food10.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food10.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food10.setAllergens(Set.of(milk));

        FoodEntity food11 = new FoodEntity();
        food11.setName("МЛЯКО С ОРИЗ");
        food11.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food11.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food11.setAllergens(Set.of(milk));

        FoodEntity food12 = new FoodEntity();
        food12.setName("МЛЯКО С ОРИЗ И КИНОА");
        food12.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food12.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food12.setAllergens(Set.of(milk));

        FoodEntity food13 = new FoodEntity();
        food13.setName("ПШЕНИЦА С МЛЯКО");
        food13.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food13.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food13.setAllergens(Set.of(milk, gluten));

        FoodEntity food14 = new FoodEntity();
        food14.setName("МЛЯКО С ГРИС");
        food14.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food14.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food14.setAllergens(Set.of(milk, gluten));

        FoodEntity food15 = new FoodEntity();
        food15.setName("КУС-КУС С ПРЯСНО МЛЯКО");
        food15.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food15.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food15.setAllergens(Set.of(milk, gluten));

        foodRepository.saveAll(List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food13, food14, food15));

    }

    private void saveBigMain(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity food1 = new FoodEntity();
        food1.setName("ЯХНИЯ ОТ ЛЕЩА");
        food1.setCategory(FoodCategoryEnum.ОСНОВНО);
        food1.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food1.setAllergens(Set.of(celery));

        FoodEntity food2 = new FoodEntity();
        food2.setName("ЯХНИЯ ОТ ЗРЯЛ БОБ");
        food2.setCategory(FoodCategoryEnum.ОСНОВНО);
        food2.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food2.setAllergens(Set.of(celery));

        FoodEntity food3 = new FoodEntity();
        food3.setName("ТИКВИЧКИ С ОРИЗ");
        food3.setCategory(FoodCategoryEnum.ОСНОВНО);
        food3.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food3.setAllergens(Set.of());

        FoodEntity food4 = new FoodEntity();
        food4.setName("ПАЙ С РИБА");
        food4.setCategory(FoodCategoryEnum.ОСНОВНО);
        food4.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food4.setAllergens(Set.of(fish, eggs, milk, gluten, cheese, butter));

        FoodEntity food5 = new FoodEntity();
        food5.setName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ ЛЕЩА");
        food5.setCategory(FoodCategoryEnum.ОСНОВНО);
        food5.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food5.setAllergens(Set.of(fish, eggs, gluten, butter));

        FoodEntity food6 = new FoodEntity();
        food6.setName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ  БОБ");
        food6.setCategory(FoodCategoryEnum.ОСНОВНО);
        food6.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food6.setAllergens(Set.of(fish, eggs, gluten, butter));

        FoodEntity food7 = new FoodEntity();
        food7.setName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ КАРТОФИ И МОРКОВИ");
        food7.setCategory(FoodCategoryEnum.ОСНОВНО);
        food7.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food7.setAllergens(Set.of(fish, eggs, gluten, butter));

        FoodEntity food8 = new FoodEntity();
        food8.setName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ ГРАХ И МОРКОВИ");
        food8.setCategory(FoodCategoryEnum.ОСНОВНО);
        food8.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food8.setAllergens(Set.of(fish, eggs, gluten, butter));

        FoodEntity food9 = new FoodEntity();
        food9.setName("ПЪЛНЕНИ ПИПЕРКИ СЪС СМЛЯНО МЕСО");
        food9.setCategory(FoodCategoryEnum.ОСНОВНО);
        food9.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food9.setAllergens(Set.of(eggs, gluten, yoghurt));

        FoodEntity food10 = new FoodEntity();
        food10.setName("МУСАКА С МЕСО И ОРИЗ");
        food10.setCategory(FoodCategoryEnum.ОСНОВНО);
        food10.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food10.setAllergens(Set.of(eggs, gluten, yoghurt));

        FoodEntity food11 = new FoodEntity();
        food11.setName("МУСАКА С МЕСО И  КАРТОФИ");
        food11.setCategory(FoodCategoryEnum.ОСНОВНО);
        food11.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food11.setAllergens(Set.of(eggs, gluten, yoghurt));

        FoodEntity food12 = new FoodEntity();
        food12.setName("СВИНСКО МЕСО С КАРТОФИ");
        food12.setCategory(FoodCategoryEnum.ОСНОВНО);
        food12.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food12.setAllergens(Set.of());


        FoodEntity food14 = new FoodEntity();
        food14.setName("ЗАЕШКО С КАРТОФИ");
        food14.setCategory(FoodCategoryEnum.ОСНОВНО);
        food14.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food14.setAllergens(Set.of());

        FoodEntity food15 = new FoodEntity();
        food15.setName("ПИЛЕШКО МЕСО С КАРТОФИ");
        food15.setCategory(FoodCategoryEnum.ОСНОВНО);
        food15.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food15.setAllergens(Set.of());

        FoodEntity food16 = new FoodEntity();
        food16.setName("АГНЕШКО МЕСО С КАРТОФИ");
        food16.setCategory(FoodCategoryEnum.ОСНОВНО);
        food16.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food16.setAllergens(Set.of());

        FoodEntity food17 = new FoodEntity();
        food17.setName("КЮФТЕ ПО ЧИРПАНСКИ");
        food17.setCategory(FoodCategoryEnum.ОСНОВНО);
        food17.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food17.setAllergens(Set.of(eggs, gluten));

        FoodEntity food18 = new FoodEntity();
        food18.setName("КЮФТЕТА С БЯЛ СОС");
        food18.setCategory(FoodCategoryEnum.ОСНОВНО);
        food18.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food18.setAllergens(Set.of(eggs, gluten, yoghurt));

        FoodEntity food19 = new FoodEntity();
        food19.setName("КЮФТЕ НА ПАРА; ГАРНИТУРА – ПЮРЕ ОТ ЛЕЩА");
        food19.setCategory(FoodCategoryEnum.ОСНОВНО);
        food19.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food19.setAllergens(Set.of(eggs, gluten, butter));

        FoodEntity food20 = new FoodEntity();
        food20.setName("КЮФТЕ НА ПАРА; ГАРНИТУРА –  ПЮРЕ ОТ  БОБ");
        food20.setCategory(FoodCategoryEnum.ОСНОВНО);
        food20.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food20.setAllergens(Set.of(eggs, gluten, butter));

        FoodEntity food21 = new FoodEntity();
        food21.setName("КЮФТЕ НА ПАРА; ГАРНИТУРА – ПЮРЕ ОТ КАРТОФИ И МОРКОВИ");
        food21.setCategory(FoodCategoryEnum.ОСНОВНО);
        food21.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food21.setAllergens(Set.of(eggs, gluten, butter, milk));

        FoodEntity food22 = new FoodEntity();
        food22.setName("КЮФТЕ НА ПАРА; ГАРНИТУРА – ПЮРЕ ОТ ГРАХ И МОРКОВИ");
        food22.setCategory(FoodCategoryEnum.ОСНОВНО);
        food22.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food22.setAllergens(Set.of(eggs, gluten, butter, milk));

        FoodEntity food23 = new FoodEntity();
        food23.setName("ЗАЕШКО МЕСО ГЮВЕЧ");
        food23.setCategory(FoodCategoryEnum.ОСНОВНО);
        food23.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food23.setAllergens(Set.of());

        FoodEntity food24 = new FoodEntity();
        food24.setName("ПИЛЕШКО МЕСО ГЮВЕЧ");
        food24.setCategory(FoodCategoryEnum.ОСНОВНО);
        food24.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food24.setAllergens(Set.of());

        FoodEntity food25 = new FoodEntity();
        food25.setName("АГНЕШКО МЕСО ГЮВЕЧ");
        food25.setCategory(FoodCategoryEnum.ОСНОВНО);
        food25.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food25.setAllergens(Set.of());

        FoodEntity food26 = new FoodEntity();
        food26.setName("АГНЕШКО МЕСО С ГРАХ");
        food26.setCategory(FoodCategoryEnum.ОСНОВНО);
        food26.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food26.setAllergens(Set.of());

        FoodEntity food27 = new FoodEntity();
        food27.setName("ПИЛЕШКО МЕСО С ГРАХ");
        food27.setCategory(FoodCategoryEnum.ОСНОВНО);
        food27.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food27.setAllergens(Set.of());

        FoodEntity food28 = new FoodEntity();
        food28.setName("ЗАЕШКО МЕСО С ГРАХ");
        food28.setCategory(FoodCategoryEnum.ОСНОВНО);
        food28.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food28.setAllergens(Set.of());

        FoodEntity food29 = new FoodEntity();
        food29.setName("СВИНСКО МЕСО С ГРАХ");
        food29.setCategory(FoodCategoryEnum.ОСНОВНО);
        food29.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food29.setAllergens(Set.of());

        FoodEntity food30 = new FoodEntity();
        food30.setName("ТАС КЕБАП");
        food30.setCategory(FoodCategoryEnum.ОСНОВНО);
        food30.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        food30.setAllergens(Set.of());

        foodRepository.saveAll(List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food14, food15, food16, food17, food18, food19, food20, food21, food22, food23, food24, food25, food26, food27, food28, food29, food30));
    }

    private void saveLittleDesserts(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity food1 = new FoodEntity();
        food1.setName("ПЛОДОВА КАША ОТ ГРИС");
        food1.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food1.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food1.setAllergens(Set.of(butter, gluten));

        FoodEntity food2 = new FoodEntity();
        food2.setName("ПЛОДОВО ПЮРЕ С ИЗВАРА");
        food2.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food2.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food2.setAllergens(Set.of(cotagge));

        FoodEntity food3 = new FoodEntity();
        food3.setName("ПЛОДОВ МУС ОТ КРУШИ");
        food3.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food3.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food3.setAllergens(Set.of(gluten));

        FoodEntity food4 = new FoodEntity();
        food4.setName("ПЛОДОВ МУС ОТ ЯБЪЛКИ");
        food4.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food4.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food4.setAllergens(Set.of(gluten));

        FoodEntity food5 = new FoodEntity();
        food5.setName("КИСЕЛ ОТ ЯБЪЛКИ С ПРЕХОДНО МЛЯКО");
        food5.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food5.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food5.setAllergens(Set.of());

        FoodEntity food6 = new FoodEntity();
        food6.setName("КИСЕЛ ОТ КРУШИ С ПРЕХОДНО МЛЯКО");
        food6.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food6.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food6.setAllergens(Set.of());

        FoodEntity food7 = new FoodEntity();
        food7.setName("КИСЕЛ ОТ БАНАНИ С ПРЕХОДНО МЛЯКО");
        food7.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food7.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food7.setAllergens(Set.of());

        FoodEntity food8 = new FoodEntity();
        food8.setName("КИСЕЛ ОТ СЕЗОНЕН ПЛОД С ПРЕХОДНО МЛЯКО");
        food8.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food8.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food8.setAllergens(Set.of());

        FoodEntity food9 = new FoodEntity();
        food9.setName("ПЮРЕ ОТ БАНАНИ С ПРЕХОДНО МЛЯКО");
        food9.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food9.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food9.setAllergens(Set.of());

        FoodEntity food10 = new FoodEntity();
        food10.setName("ОРИЗ С ПЪПЕШ");
        food10.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food10.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food10.setAllergens(Set.of());

        FoodEntity food11 = new FoodEntity();
        food11.setName("МЛЕЧЕН ДЕСЕРТ ОТ ОВЕСЕНИ ЯДКИ И ПЛОД");
        food11.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food11.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food11.setAllergens(Set.of(butter, gluten));

        FoodEntity food12 = new FoodEntity();
        food12.setName("ОВЕСЕНИ ЯДКИ С КРУШИ И ИЗВАРА");
        food12.setCategory(FoodCategoryEnum.ДЕСЕРТ);
        food12.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food12.setAllergens(Set.of(cotagge, gluten));

        foodRepository.saveAll(List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12));
    }

    private void savePurees(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity food1 = new FoodEntity();
        food1.setName("ЗЕЛЕНЧУКОВО ПЮРЕ С ИЗВАРА");
        food1.setCategory(FoodCategoryEnum.ПЮРЕ);
        food1.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food1.setAllergens(Set.of(cotagge));

        FoodEntity food2 = new FoodEntity();
        food2.setName("ПЮРЕ МАКАРОНИ СЪС СИРЕНЕ И ЗЕЛЕНЧУЦИ");
        food2.setCategory(FoodCategoryEnum.ПЮРЕ);
        food2.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food2.setAllergens(Set.of(cheeseNature, gluten, butter));

        FoodEntity food3 = new FoodEntity();
        food3.setName("ПЮРЕ ЛЕЩА СЪС ЗЕЛЕНЧУЦИ");
        food3.setCategory(FoodCategoryEnum.ПЮРЕ);
        food3.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food3.setAllergens(Set.of());

        FoodEntity food4 = new FoodEntity();
        food4.setName("ПЮРЕ ОТ БРОКОЛИ И СИРЕНЕ");
        food4.setCategory(FoodCategoryEnum.ПЮРЕ);
        food4.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food4.setAllergens(Set.of(cheeseNature, butter));

        FoodEntity food5 = new FoodEntity();
        food5.setName("ПЮРЕ ОТ ТЕЛЕШКО С ОРИЗ");
        food5.setCategory(FoodCategoryEnum.ПЮРЕ);
        food5.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food5.setAllergens(Set.of());

        FoodEntity food6 = new FoodEntity();
        food6.setName("ПЮРЕ ОТ ПИЛЕШКО СЪС ЗЕЛЕНЧУЦИ");
        food6.setCategory(FoodCategoryEnum.ПЮРЕ);
        food6.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food6.setAllergens(Set.of(celery));

        FoodEntity food7 = new FoodEntity();
        food7.setName("ПЮРЕ ОТ ЗАЕШКО СЪС ЗЕЛЕНЧУЦИ");
        food7.setCategory(FoodCategoryEnum.ПЮРЕ);
        food7.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food7.setAllergens(Set.of(celery));

        FoodEntity food8 = new FoodEntity();
        food8.setName("ПЮРЕ ОТ ТЕЛЕШКО СЪС ЗЕЛЕНЧУЦИ");
        food8.setCategory(FoodCategoryEnum.ПЮРЕ);
        food8.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food8.setAllergens(Set.of(celery));

        FoodEntity food9 = new FoodEntity();
        food9.setName("ПЮРЕ ВАРЕНА МУСАКА");
        food9.setCategory(FoodCategoryEnum.ПЮРЕ);
        food9.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food9.setAllergens(Set.of());

        FoodEntity food10 = new FoodEntity();
        food10.setName("ПЮРЕ ОТ ПИЛЕШКО С ГРАХ");
        food10.setCategory(FoodCategoryEnum.ПЮРЕ);
        food10.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food10.setAllergens(Set.of());

        FoodEntity food11 = new FoodEntity();
        food11.setName("ПЮРЕ ОТ ТЕЛЕШКО С ГРАХ");
        food11.setCategory(FoodCategoryEnum.ПЮРЕ);
        food11.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food11.setAllergens(Set.of());

        FoodEntity food12 = new FoodEntity();
        food12.setName("ПЮРЕ ОТ ЗАЕШКО С ГРАХ");
        food12.setCategory(FoodCategoryEnum.ПЮРЕ);
        food12.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food12.setAllergens(Set.of());

        FoodEntity food13 = new FoodEntity();
        food13.setName("ПЮРЕ КУС-КУС СЪС ЗАЕШКО МЕСО И ЗЕЛЕНЧУЦИ");
        food13.setCategory(FoodCategoryEnum.ПЮРЕ);
        food13.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food13.setAllergens(Set.of(gluten, butter));

        FoodEntity food14 = new FoodEntity();
        food14.setName("ПЮРЕ ОТ РИБА И ЗЕЛЕНЧУЦИ");
        food14.setCategory(FoodCategoryEnum.ПЮРЕ);
        food14.setAgeGroup(AgeGroupEnum.МАЛКИ);
        food14.setAllergens(Set.of(fish));

        foodRepository.saveAll(List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food13, food14));
    }

    private void saveSoups(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity soup1 = new FoodEntity();
        soup1.setName("СУПА ТОПЧЕТА");
        soup1.setCategory(FoodCategoryEnum.СУПА);
        soup1.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup1.setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup2 = new FoodEntity();
        soup2.setName("СУПА С ПИЛЕШКО МЕСО");
        soup2.setCategory(FoodCategoryEnum.СУПА);
        soup2.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup2.setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup3 = new FoodEntity();
        soup3.setName("СУПА С ПУЕШКО МЕСО");
        soup3.setCategory(FoodCategoryEnum.СУПА);
        soup3.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup3.setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup4 = new FoodEntity();
        soup4.setName("СУПА ОТ ДОМАТИ С КАРТОФИ");
        soup4.setCategory(FoodCategoryEnum.СУПА);
        soup4.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup4.setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup5 = new FoodEntity();
        soup5.setName("СУПА ОТ ТИКВИЧКИ СЪС ЗАСТРОЙКА");
        soup5.setCategory(FoodCategoryEnum.СУПА);
        soup5.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup5.setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup6 = new FoodEntity();
        soup6.setName("СУПА ОТ КАРТОФИ И МОРКОВИ");
        soup6.setCategory(FoodCategoryEnum.СУПА);
        soup6.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup6.setAllergens(Set.of(gluten));

        FoodEntity soup7 = new FoodEntity();
        soup7.setName("СУПА ОТ ЗЕЛЕН ФАСУЛ СЪС ЗАСТРОЙКА");
        soup7.setCategory(FoodCategoryEnum.СУПА);
        soup7.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup7.setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup8 = new FoodEntity();
        soup8.setName("СУПА С АГНЕШКО МЕСО");
        soup8.setCategory(FoodCategoryEnum.СУПА);
        soup8.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup8.setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup9 = new FoodEntity();
        soup9.setName("СУПА ОТ ЗЕЛЕН ФАСУЛ С ФИДЕ");
        soup9.setCategory(FoodCategoryEnum.СУПА);
        soup9.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup9.setAllergens(Set.of(celery, gluten));

        FoodEntity soup10 = new FoodEntity();
        soup10.setName("КРЕМ СУПА ОТ КАРТОФИ, МОРКОВИ С ВАРЕНО ЯЙЦЕ");
        soup10.setCategory(FoodCategoryEnum.СУПА);
        soup10.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup10.setAllergens(Set.of(gluten, butter));

        FoodEntity soup11 = new FoodEntity();
        soup11.setName("КРЕМ СУПА ОТ КАРТОФИ, МОРКОВИ БЕЗ ЗАСТРОЙКА");
        soup11.setCategory(FoodCategoryEnum.СУПА);
        soup11.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup11.setAllergens(Set.of(gluten, butter));

        FoodEntity soup12 = new FoodEntity();
        soup12.setName("КРЕМ СУПА ОТ ГРАХ, МОРКОВИ И ТИКВИЧКА");
        soup12.setCategory(FoodCategoryEnum.СУПА);
        soup12.setAgeGroup(AgeGroupEnum.ГОЛЕМИ);
        soup12.setAllergens(Set.of(cheeseNature, gluten, butter));

        foodRepository.saveAll(List.of(soup1, soup2, soup3, soup4, soup5, soup6, soup7, soup8, soup9, soup10, soup11, soup12));
    }




}
