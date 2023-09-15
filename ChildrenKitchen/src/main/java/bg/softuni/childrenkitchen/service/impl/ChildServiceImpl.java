package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.CloudinaryImage;
import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.entity.AllergyEntity;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.repository.ChildRepository;
import bg.softuni.childrenkitchen.service.AllergyService;
import bg.softuni.childrenkitchen.service.ChildService;
import bg.softuni.childrenkitchen.service.CloudinaryService;
import bg.softuni.childrenkitchen.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final UserService userService;
    private final AllergyService allergyService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public ChildServiceImpl(ChildRepository childRepository, UserService userService, AllergyService allergyService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.childRepository = childRepository;
        this.userService = userService;
        this.allergyService = allergyService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public void initDB(){
        if(childRepository.count() > 0){
            return;
        }

        ChildEntity misho = new ChildEntity();
        misho.setFullName("Михаил Иванов Иванов");
        misho.setBirthDate(LocalDate.of(2022,10,24));

        misho.setAgeGroup(defineAgeGroup(misho.getBirthDate()));

        misho.setCoupons(new ArrayList<>());
        //todo exception throw
        misho.setAllergies(Set.of(allergyService.findByName(AllergyEnum.ГЛУТЕН).orElseThrow()));
        misho.setParent(userService.getByEmail("mama@test.com").orElseThrow());

        misho.setBirthCertificateURL("https://res.cloudinary.com/galkab/image/upload/v1694687128/DK_PROJECT/ChildrenDocuments/birth-cert-example-2_ul05f9.jpg");
        misho.setMedicalListURL("https://res.cloudinary.com/galkab/image/upload/v1694686942/DK_PROJECT/ChildrenDocuments/medicalList-example-3_mefusb.jpg");

        ChildEntity gosho = new ChildEntity();
        gosho.setFullName("Георги Иванов Иванов");
        gosho.setBirthDate(LocalDate.of(2022,9,5));

        gosho.setAgeGroup(defineAgeGroup(gosho.getBirthDate()));

        gosho.setMedicalListURL("https://res.cloudinary.com/galkab/image/upload/v1694686942/DK_PROJECT/ChildrenDocuments/medicalList-example-2_ksnivh.jpg");
        gosho.setBirthCertificateURL("https://res.cloudinary.com/galkab/image/upload/v1694686942/DK_PROJECT/ChildrenDocuments/Birth-Certificate-example-1_oudx0u.jpg");

        gosho.setCoupons(new ArrayList<>());
        //todo exception throw
        gosho.setAllergies(Set.of(allergyService.findByName(AllergyEnum.НЯМА).orElseThrow()));
        gosho.setParent(userService.getByEmail("mama@test.com").orElseThrow());

        ChildEntity ivan = new ChildEntity();
        ivan.setFullName("Иван Георгиев Тодоров");
        ivan.setBirthDate(LocalDate.of(2022,11,16));
        ivan.setAgeGroup(defineAgeGroup(ivan.getBirthDate()));

        ivan.setMedicalListURL("https://res.cloudinary.com/galkab/image/upload/v1694686942/DK_PROJECT/ChildrenDocuments/medicalList-example-1_i2rary.jpg");
        ivan.setBirthCertificateURL("https://res.cloudinary.com/galkab/image/upload/v1694687305/DK_PROJECT/ChildrenDocuments/birth-cert-example-3_rzi5wo.png");

        ivan.setCoupons(new ArrayList<>());
        //todo exception throw
        ivan.setAllergies(Set.of(allergyService.findByName(AllergyEnum.НЯМА).orElseThrow()));
        ivan.setParent(userService.getByEmail("mama2@test.com").orElseThrow());

        childRepository.saveAll(List.of(misho, gosho, ivan));
    }

    @Override
    public ChildViewModel saveChild(ChildRegisterBindingModel childRegisterBindingModel, @AuthenticationPrincipal CustomUserDetails loggedInUser) throws IOException {

        ChildEntity childEntity = modelMapper.map(childRegisterBindingModel, ChildEntity.class);
        childEntity.setAgeGroup(defineAgeGroup(childRegisterBindingModel.getBirthDate()));
        childEntity.setAllergies(defineAllergies(childRegisterBindingModel.getAllergy()));
        //todo exception  handling
        childEntity.setParent(userService.getByEmail(loggedInUser.getUsername()).orElseThrow());
        childEntity.setCoupons(new ArrayList<>());

        CloudinaryImage medicalList = cloudinaryService.uploadImage(childRegisterBindingModel.getMedicalList());
        CloudinaryImage birthCert = cloudinaryService.uploadImage(childRegisterBindingModel.getBirthCertificate());

        childEntity.setBirthCertificateURL(birthCert.getUrl());
        childEntity.setMedicalListURL(medicalList.getUrl());

        
        ChildEntity savedChild = childRepository.save(childEntity);

        ChildViewModel childViewModel = modelMapper.map(savedChild, ChildViewModel.class);
        childViewModel.setAllergies(childRegisterBindingModel.getAllergy());

        return childViewModel;

    }

    private AgeGroupEnum defineAgeGroup(LocalDate birthDate) {
        if (birthDate.isBefore(LocalDate.now().minusMonths(12))){
            return AgeGroupEnum.ГОЛЕМИ;
        }else {
            return AgeGroupEnum.МАЛКИ;
        }
    }

    private Set<AllergyEntity> defineAllergies(String allergy) {
        String[] allergies = allergy.split(",");

        return Arrays.stream(allergies).map(this::mapToEntity)
                    .collect(Collectors.toSet());
    }

    private AllergyEntity mapToEntity(String name) {
        return allergyService.findByName(AllergyEnum.valueOf(name.toUpperCase())).orElseThrow();
    }

}
