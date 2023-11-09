package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.binding.AddRecipeBindingModel;
import bg.softuni.childrenkitchen.model.entity.AllergenEntity;
import bg.softuni.childrenkitchen.model.entity.FoodEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergensEnum;
import bg.softuni.childrenkitchen.model.entity.enums.FoodCategoryEnum;
import bg.softuni.childrenkitchen.model.view.FoodViewModel;
import bg.softuni.childrenkitchen.repository.FoodRepository;
import bg.softuni.childrenkitchen.service.interfaces.AllergenService;
import bg.softuni.childrenkitchen.service.interfaces.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final AllergenService allergenService;
    private final ModelMapper modelMapper;

    public FoodServiceImpl(FoodRepository foodRepository, AllergenService allergenService, ModelMapper modelMapper) {
        this.foodRepository = foodRepository;
        this.allergenService = allergenService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initDB() {
        if (foodRepository.count() > 0) {
            return;
        }

        AllergenEntity gluten = allergenService.findByName("ГЛУТЕН")
                                               .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity cottage = allergenService.findByName("ИЗВАРА")
                                                .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity cheese = allergenService.findByName("КАШКАВАЛ")
                                               .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity fish = allergenService.findByName("РИБА")
                                             .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity eggs = allergenService.findByName("ЯЙЦА")
                                             .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity yoghurt = allergenService.findByName("КИСЕЛО_МЛЯКО")
                                                .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity milk = allergenService.findByName("ПРЯСНО_МЛЯКО")
                                             .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity butter = allergenService.findByName("КРАВЕ_МАСЛО")
                                               .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity cheeseNature = allergenService.findByName("СИРЕНЕ")
                                                     .orElseThrow(ObjectNotFoundException::new);
        AllergenEntity celery = allergenService.findByName("ЦЕЛИНА")
                                               .orElseThrow(ObjectNotFoundException::new);

        saveSoups(gluten, cottage, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

        savePurees(gluten, cottage, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

        saveLittleDesserts(gluten, cottage, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

        saveBigMain(gluten, cottage, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

        saveBigDesserts(gluten, cottage, cheese, fish, eggs, yoghurt, milk, butter, cheeseNature, celery);

    }

    @Override
    public Optional<FoodEntity> getByName(String foodName) {
        return foodRepository.findByName(foodName);
    }

    @Override
    public boolean existByName(String foodName) {
        return foodRepository.existsByName(foodName.toUpperCase());
    }

    @Override
    public FoodViewModel addFood(AddRecipeBindingModel addRecipeBindingModel) {
        FoodEntity foodEntity = modelMapper.map(addRecipeBindingModel, FoodEntity.class);

        foodEntity.setName(addRecipeBindingModel.getFoodName()
                                                .toUpperCase());

        foodEntity.setAllergens(new HashSet<>());

        Set<AllergenEntity> allergenEntities = getAllergenEntities(addRecipeBindingModel.getAllergens(), addRecipeBindingModel.getOtherAllergen());

        foodEntity.setAllergens(allergenEntities);

        FoodEntity saved = foodRepository.save(foodEntity);


        return mapToFoodViewModel(saved);

    }

    private FoodViewModel mapToFoodViewModel(FoodEntity saved) {
        FoodViewModel foodViewModel = new FoodViewModel();
        foodViewModel.setName(saved.getName().toUpperCase());
        foodViewModel.setAgeGroupName(saved.getAgeGroup().name());
        foodViewModel.setCategoryName(saved.getCategory().name());
        foodViewModel.setAllergens(saved.getAllergens().stream()
                                     .map(AllergenEntity::getName)
                                        .map(s -> {
                                            if (s.contains("_")) {
                                                s = s.replace("_", " ");
                                            }
                                            return s;
                                        })
                                        .collect(Collectors.joining(", ")));

        if (foodViewModel.getAllergens().equals("")){
            foodViewModel.setAllergens("Няма");
        }

        return foodViewModel;
    }

    @Override
    public Set<String> findAllFoodsNameByCategoryAndAgeGroup(FoodCategoryEnum foodCategoryEnum, AgeGroupEnum ageGroup) {
        return foodRepository.findAllByCategoryAndAgeGroup(foodCategoryEnum, ageGroup)
                             .stream()
                             .map(FoodEntity::getName)
                             .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAllSoups(FoodCategoryEnum foodCategoryEnum) {
        return foodRepository.findAllByCategory(foodCategoryEnum)
                             .stream()
                             .map(FoodEntity::getName)
                             .collect(Collectors.toSet());

    }

    private Set<AllergenEntity> getAllergenEntities(List<AllergensEnum> allergens, String otherAllergen) {
        Set<AllergenEntity> setToReturn = new HashSet<>();

        if (!allergens.isEmpty()) {
            setToReturn = allergens.stream()
                                   .map(a -> allergenService.findByName(a.name())
                                                            .orElseThrow(ObjectNotFoundException::new))
                                   .collect(Collectors.toSet());
        }

        if (otherAllergen.equals("")) {
            return setToReturn;
        }

        otherAllergen = otherAllergen.toUpperCase();

        List<String> otherAllergensList = new ArrayList<>();

        if (otherAllergen.contains(",")) {
            otherAllergensList = Arrays.stream(otherAllergen.split(","))
                                       .map(String::trim)
                                       .toList();
        } else {
            otherAllergensList.add(otherAllergen);
        }

        Set<AllergenEntity> collect = otherAllergensList.stream()
                                                        .map(allergenService::createAllergenEntity)
                                                        .collect(Collectors.toSet());
        setToReturn.addAll(collect);

        return setToReturn;

    }

    private void saveBigDesserts(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity food1 = new FoodEntity()
                .setName("ЯБЪЛКОВ СЛАДКИШ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(gluten, eggs));

        FoodEntity food2 = new FoodEntity()
                .setName("РУЛО С КОНФИТЮР")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten));

        FoodEntity food3 = new FoodEntity()
                .setName("МАКАРОНИ С МЛЯКО И ЯЙЦА")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(butter, milk, eggs));

        FoodEntity food4 = new FoodEntity()
                .setName("КУС-КУС С МЛЯКО И ЯЙЦА")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(butter, milk, eggs));

        FoodEntity food5 = new FoodEntity()
                .setName("ГРИС ХАЛВА")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(gluten, butter));

        FoodEntity food6 = new FoodEntity()
                .setName("МЛЕЧЕН КИСЕЛ ОТ БАНАНИ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk));

        FoodEntity food7 = new FoodEntity()
                .setName("МЛЕЧЕН КИСЕЛ ОТ ПРАСКОВИ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk));

        FoodEntity food8 = new FoodEntity()
                .setName("МЛЕЧЕН КИСЕЛ ОТ КАЙСИИ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk));

        FoodEntity food9 = new FoodEntity()
                .setName("МЛЕЧЕН КИСЕЛ ОТ НЕКТАРИНИ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk));

        FoodEntity food10 = new FoodEntity()
                .setName("МЛЕЧЕН КИСЕЛ ОТ ПЪПЕШ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk));

        FoodEntity food11 = new FoodEntity()
                .setName("МЛЯКО С ОРИЗ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk));

        FoodEntity food12 = new FoodEntity()
                .setName("МЛЯКО С ОРИЗ И КИНОА")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk));

        FoodEntity food13 = new FoodEntity()
                .setName("ПШЕНИЦА С МЛЯКО")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk, gluten));

        FoodEntity food14 = new FoodEntity()
                .setName("МЛЯКО С ГРИС")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk, gluten));

        FoodEntity food15 = new FoodEntity()
                .setName("КУС-КУС С ПРЯСНО МЛЯКО")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(milk, gluten));

        foodRepository.saveAll(List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food13, food14, food15));

    }

    private void saveBigMain(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity food1 = new FoodEntity()
                .setName("ЯХНИЯ ОТ ЛЕЩА")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(celery));

        FoodEntity food2 = new FoodEntity()
                .setName("ЯХНИЯ ОТ ЗРЯЛ БОБ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(celery));

        FoodEntity food3 = new FoodEntity()
                .setName("ТИКВИЧКИ С ОРИЗ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food4 = new FoodEntity()
                .setName("ПАЙ С РИБА")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(fish, eggs, milk, gluten, cheese, butter));

        FoodEntity food5 = new FoodEntity()
                .setName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ ЛЕЩА")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(fish, eggs, gluten, butter));

        FoodEntity food6 = new FoodEntity()
                .setName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ  БОБ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(fish, eggs, gluten, butter));

        FoodEntity food7 = new FoodEntity()
                .setName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ КАРТОФИ И МОРКОВИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(fish, eggs, gluten, butter));

        FoodEntity food8 = new FoodEntity()
                .setName("КЮФТЕ ОТ РИБА/ ГАРНИТУРА – ПЮРЕ ОТ ГРАХ И МОРКОВИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(fish, eggs, gluten, butter));

        FoodEntity food9 = new FoodEntity()
                .setName("ПЪЛНЕНИ ПИПЕРКИ СЪС СМЛЯНО МЕСО")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten, yoghurt));

        FoodEntity food10 = new FoodEntity()
                .setName("МУСАКА С МЕСО И ОРИЗ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten, yoghurt));

        FoodEntity food11 = new FoodEntity()
                .setName("МУСАКА С МЕСО И  КАРТОФИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten, yoghurt));

        FoodEntity food12 = new FoodEntity()
                .setName("СВИНСКО МЕСО С КАРТОФИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());


        FoodEntity food14 = new FoodEntity()
                .setName("ЗАЕШКО С КАРТОФИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food15 = new FoodEntity()
                .setName("ПИЛЕШКО МЕСО С КАРТОФИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food16 = new FoodEntity()
                .setName("АГНЕШКО МЕСО С КАРТОФИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food17 = new FoodEntity()
                .setName("КЮФТЕ ПО ЧИРПАНСКИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten));

        FoodEntity food18 = new FoodEntity()
                .setName("КЮФТЕТА С БЯЛ СОС")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten, yoghurt));

        FoodEntity food19 = new FoodEntity()
                .setName("КЮФТЕ НА ПАРА; ГАРНИТУРА – ПЮРЕ ОТ ЛЕЩА")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten, butter));

        FoodEntity food20 = new FoodEntity()
                .setName("КЮФТЕ НА ПАРА; ГАРНИТУРА –  ПЮРЕ ОТ  БОБ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten, butter));

        FoodEntity food21 = new FoodEntity()
                .setName("КЮФТЕ НА ПАРА; ГАРНИТУРА – ПЮРЕ ОТ КАРТОФИ И МОРКОВИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten, butter, milk));

        FoodEntity food22 = new FoodEntity()
                .setName("КЮФТЕ НА ПАРА; ГАРНИТУРА – ПЮРЕ ОТ ГРАХ И МОРКОВИ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(eggs, gluten, butter, milk));

        FoodEntity food23 = new FoodEntity()
                .setName("ЗАЕШКО МЕСО ГЮВЕЧ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food24 = new FoodEntity()
                .setName("ПИЛЕШКО МЕСО ГЮВЕЧ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food25 = new FoodEntity()
                .setName("АГНЕШКО МЕСО ГЮВЕЧ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food26 = new FoodEntity()
                .setName("АГНЕШКО МЕСО С ГРАХ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food27 = new FoodEntity()
                .setName("ПИЛЕШКО МЕСО С ГРАХ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food28 = new FoodEntity()
                .setName("ЗАЕШКО МЕСО С ГРАХ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food29 = new FoodEntity()
                .setName("СВИНСКО МЕСО С ГРАХ")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        FoodEntity food30 = new FoodEntity()
                .setName("ТАС КЕБАП")
                .setCategory(FoodCategoryEnum.ОСНОВНО)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of());

        foodRepository.saveAll(List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food14, food15, food16, food17, food18, food19, food20, food21, food22, food23, food24, food25, food26, food27, food28, food29, food30));
    }

    private void saveLittleDesserts(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity food1 = new FoodEntity()
                .setName("ПЛОДОВА КАША ОТ ГРИС")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(butter, gluten));

        FoodEntity food2 = new FoodEntity()
                .setName("ПЛОДОВО ПЮРЕ С ИЗВАРА")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(cotagge));

        FoodEntity food3 = new FoodEntity()
                .setName("ПЛОДОВ МУС ОТ КРУШИ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(gluten));

        FoodEntity food4 = new FoodEntity()
                .setName("ПЛОДОВ МУС ОТ ЯБЪЛКИ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(gluten));

        FoodEntity food5 = new FoodEntity()
                .setName("КИСЕЛ ОТ ЯБЪЛКИ С ПРЕХОДНО МЛЯКО")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food6 = new FoodEntity()
                .setName("КИСЕЛ ОТ КРУШИ С ПРЕХОДНО МЛЯКО")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food7 = new FoodEntity()
                .setName("КИСЕЛ ОТ БАНАНИ С ПРЕХОДНО МЛЯКО")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food8 = new FoodEntity()
                .setName("КИСЕЛ ОТ СЕЗОНЕН ПЛОД С ПРЕХОДНО МЛЯКО")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food9 = new FoodEntity()
                .setName("ПЮРЕ ОТ БАНАНИ С ПРЕХОДНО МЛЯКО")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food10 = new FoodEntity()
                .setName("ОРИЗ С ПЪПЕШ")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food11 = new FoodEntity()
                .setName("МЛЕЧЕН ДЕСЕРТ ОТ ОВЕСЕНИ ЯДКИ И ПЛОД")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(butter, gluten));

        FoodEntity food12 = new FoodEntity()
                .setName("ОВЕСЕНИ ЯДКИ С КРУШИ И ИЗВАРА")
                .setCategory(FoodCategoryEnum.ДЕСЕРТ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(cotagge, gluten));

        foodRepository.saveAll(List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12));
    }

    private void savePurees(AllergenEntity gluten, AllergenEntity cottage, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity food1 = new FoodEntity()
                .setName("ЗЕЛЕНЧУКОВО ПЮРЕ С ИЗВАРА")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(cottage));

        FoodEntity food2 = new FoodEntity()
                .setName("ПЮРЕ МАКАРОНИ СЪС СИРЕНЕ И ЗЕЛЕНЧУЦИ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(cheeseNature, gluten, butter));

        FoodEntity food3 = new FoodEntity()
                .setName("ПЮРЕ ЛЕЩА СЪС ЗЕЛЕНЧУЦИ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food4 = new FoodEntity()
                .setName("ПЮРЕ ОТ БРОКОЛИ И СИРЕНЕ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(cheeseNature, butter));

        FoodEntity food5 = new FoodEntity()
                .setName("ПЮРЕ ОТ ТЕЛЕШКО С ОРИЗ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food6 = new FoodEntity()
                .setName("ПЮРЕ ОТ ПИЛЕШКО СЪС ЗЕЛЕНЧУЦИ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(celery));

        FoodEntity food7 = new FoodEntity()
                .setName("ПЮРЕ ОТ ЗАЕШКО СЪС ЗЕЛЕНЧУЦИ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(celery));

        FoodEntity food8 = new FoodEntity()
                .setName("ПЮРЕ ОТ ТЕЛЕШКО СЪС ЗЕЛЕНЧУЦИ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(celery));

        FoodEntity food9 = new FoodEntity()
                .setName("ПЮРЕ ВАРЕНА МУСАКА")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food10 = new FoodEntity()
                .setName("ПЮРЕ ОТ ПИЛЕШКО С ГРАХ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food11 = new FoodEntity()
                .setName("ПЮРЕ ОТ ТЕЛЕШКО С ГРАХ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food12 = new FoodEntity()
                .setName("ПЮРЕ ОТ ЗАЕШКО С ГРАХ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of());

        FoodEntity food13 = new FoodEntity()
                .setName("ПЮРЕ КУС-КУС СЪС ЗАЕШКО МЕСО И ЗЕЛЕНЧУЦИ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(gluten, butter));

        FoodEntity food14 = new FoodEntity()
                .setName("ПЮРЕ ОТ РИБА И ЗЕЛЕНЧУЦИ")
                .setCategory(FoodCategoryEnum.ПЮРЕ)
                .setAgeGroup(AgeGroupEnum.МАЛКИ)
                .setAllergens(Set.of(fish));

        foodRepository.saveAll(List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10, food11, food12, food13, food14));
    }

    private void saveSoups(AllergenEntity gluten, AllergenEntity cotagge, AllergenEntity cheese, AllergenEntity fish, AllergenEntity eggs, AllergenEntity yoghurt, AllergenEntity milk, AllergenEntity butter, AllergenEntity cheeseNature, AllergenEntity celery) {
        FoodEntity soup1 = new FoodEntity()
                .setName("СУПА ТОПЧЕТА")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup2 = new FoodEntity()
                .setName("СУПА С ПИЛЕШКО МЕСО")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup3 = new FoodEntity()
                .setName("СУПА С ПУЕШКО МЕСО")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup4 = new FoodEntity()
                .setName("СУПА ОТ ДОМАТИ С КАРТОФИ")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup5 = new FoodEntity()
                .setName("СУПА ОТ ТИКВИЧКИ СЪС ЗАСТРОЙКА")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup6 = new FoodEntity()
                .setName("СУПА ОТ КАРТОФИ И МОРКОВИ")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(gluten));

        FoodEntity soup7 = new FoodEntity()
                .setName("СУПА ОТ ЗЕЛЕН ФАСУЛ СЪС ЗАСТРОЙКА")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup8 = new FoodEntity()
                .setName("СУПА С АГНЕШКО МЕСО")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(yoghurt, gluten));

        FoodEntity soup9 = new FoodEntity()
                .setName("СУПА ОТ ЗЕЛЕН ФАСУЛ С ФИДЕ")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(celery, gluten));

        FoodEntity soup10 = new FoodEntity()
                .setName("КРЕМ СУПА ОТ КАРТОФИ, МОРКОВИ С ВАРЕНО ЯЙЦЕ")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(gluten, butter));

        FoodEntity soup11 = new FoodEntity()
                .setName("КРЕМ СУПА ОТ КАРТОФИ, МОРКОВИ БЕЗ ЗАСТРОЙКА")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(gluten, butter));

        FoodEntity soup12 = new FoodEntity()
                .setName("КРЕМ СУПА ОТ ГРАХ, МОРКОВИ И ТИКВИЧКА")
                .setCategory(FoodCategoryEnum.СУПА)
                .setAgeGroup(AgeGroupEnum.ГОЛЕМИ)
                .setAllergens(Set.of(cheeseNature, gluten, butter));

        foodRepository.saveAll(List.of(soup1, soup2, soup3, soup4, soup5, soup6, soup7, soup8, soup9, soup10, soup11, soup12));
    }


}
