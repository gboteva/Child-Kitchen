package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.CloudinaryImage;
import bg.softuni.childrenkitchen.model.CustomUserDetails;
import bg.softuni.childrenkitchen.model.binding.ChildRegisterBindingModel;
import bg.softuni.childrenkitchen.model.entity.AllergyEntity;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.entity.enums.AgeGroupEnum;
import bg.softuni.childrenkitchen.model.entity.enums.AllergyEnum;
import bg.softuni.childrenkitchen.exception.ObjectNotFoundException;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
import bg.softuni.childrenkitchen.model.view.ChildViewModel;
import bg.softuni.childrenkitchen.repository.ChildRepository;
import bg.softuni.childrenkitchen.service.*;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChildServiceImpl implements ChildService {

    private static final String recipientEmail = "adminKitchen@gmail.com";
    private final static String subjectAddKid = "Регистрирано ново дете в системата!";
    private final ChildRepository childRepository;
    private final UserService userService;
    private final AllergyService allergyService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final EmailService emailService;

    public ChildServiceImpl(ChildRepository childRepository, UserService userService, AllergyService allergyService, ModelMapper modelMapper, CloudinaryService cloudinaryService, EmailService emailService) {
        this.childRepository = childRepository;
        this.userService = userService;
        this.allergyService = allergyService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.emailService = emailService;
    }

    @Override
    public void initDB() {
        if (childRepository.count() > 0) {
            return;
        }

        ChildEntity misho = new ChildEntity();
        misho.setFullName("МИХАИЛ ИВАНОВ ИВАНОВ");
        misho.setBirthDate(LocalDate.of(2022, 10, 24));

        misho.setAgeGroup(defineAgeGroup(misho.getBirthDate()));

        misho.setCoupons(new ArrayList<>());

        misho.setAllergies(Set.of(allergyService.findByName(AllergyEnum.ГЛУТЕН)
                                                .orElseThrow(ObjectNotFoundException::new)));
        misho.setParent(userService.getByEmail("mama@test.com")
                                   .orElseThrow(ObjectNotFoundException::new));

        misho.setBirthCertificateURL("https://res.cloudinary.com/galkab/image/upload/v1694687128/DK_PROJECT/ChildrenDocuments/birth-cert-example-2_ul05f9.jpg");
        misho.setMedicalListURL("https://res.cloudinary.com/galkab/image/upload/v1694686942/DK_PROJECT/ChildrenDocuments/medicalList-example-3_mefusb.jpg");

        ChildEntity gosho = new ChildEntity();
        gosho.setFullName("ГЕОРГИ ИВАНОВ ИВАНОВ");
        gosho.setBirthDate(LocalDate.of(2022, 9, 5));

        gosho.setAgeGroup(defineAgeGroup(gosho.getBirthDate()));

        gosho.setMedicalListURL("https://res.cloudinary.com/galkab/image/upload/v1694686942/DK_PROJECT/ChildrenDocuments/medicalList-example-2_ksnivh.jpg");
        gosho.setBirthCertificateURL("https://res.cloudinary.com/galkab/image/upload/v1694686942/DK_PROJECT/ChildrenDocuments/Birth-Certificate-example-1_oudx0u.jpg");

        gosho.setCoupons(new ArrayList<>());

        gosho.setAllergies(Set.of(allergyService.findByName(AllergyEnum.НЯМА)
                                                .orElseThrow(ObjectNotFoundException::new)));
        gosho.setParent(userService.getByEmail("mama@test.com")
                                   .orElseThrow(ObjectNotFoundException::new));

        ChildEntity ivan = new ChildEntity();
        ivan.setFullName("ИВАН ГЕОРГИЕВ ТОДОРОВ");
        ivan.setBirthDate(LocalDate.of(2022, 11, 16));
        ivan.setAgeGroup(defineAgeGroup(ivan.getBirthDate()));

        ivan.setMedicalListURL("https://res.cloudinary.com/galkab/image/upload/v1694686942/DK_PROJECT/ChildrenDocuments/medicalList-example-1_i2rary.jpg");
        ivan.setBirthCertificateURL("https://res.cloudinary.com/galkab/image/upload/v1694687305/DK_PROJECT/ChildrenDocuments/birth-cert-example-3_rzi5wo.png");

        ivan.setCoupons(new ArrayList<>());


        ivan.setAllergies(Set.of(allergyService.findByName(AllergyEnum.НЯМА)
                                               .orElseThrow(ObjectNotFoundException::new)));
        ivan.setParent(userService.getByEmail("mama2@test.com")
                                  .orElseThrow(ObjectNotFoundException::new));


        ChildEntity adminChild = new ChildEntity();
        adminChild.setFullName("TЕСТОВО АДМИНСКО ДЕТЕ");
        adminChild.setBirthDate(LocalDate.of(2022, 10, 4));
        adminChild.setAgeGroup(defineAgeGroup(adminChild.getBirthDate()));
        adminChild.setCoupons(new ArrayList<>());
        adminChild.setAllergies(Set.of(allergyService.findByName(AllergyEnum.РИБА)
                                                     .orElseThrow(ObjectNotFoundException::new)));

        adminChild.setParent(userService.getByEmail("admin@test.com").orElseThrow(ObjectNotFoundException::new));

        childRepository.saveAll(List.of(misho, gosho, ivan, adminChild));
    }

    @Override
    public ChildViewModel saveChild(ChildRegisterBindingModel childRegisterBindingModel, @AuthenticationPrincipal CustomUserDetails loggedInUser) throws IOException {

        ChildEntity childEntity = modelMapper.map(childRegisterBindingModel, ChildEntity.class);
        childEntity.setAgeGroup(defineAgeGroup(childRegisterBindingModel.getBirthDate()));
        childEntity.setAllergies(defineAllergies(childRegisterBindingModel.getAllergy()));
        childEntity.setParent(userService.getByEmail(loggedInUser.getUsername())
                                         .orElseThrow(ObjectNotFoundException::new));
        childEntity.setCoupons(new ArrayList<>());


        CloudinaryImage medicalList = cloudinaryService.uploadImage(childRegisterBindingModel.getMedicalList());
        CloudinaryImage birthCert = cloudinaryService.uploadImage(childRegisterBindingModel.getBirthCertificate());


        childEntity.setBirthCertificateURL(birthCert.getUrl());
        childEntity.setMedicalListURL(medicalList.getUrl());
        childEntity.setFullName(childRegisterBindingModel.getFullName()
                                                         .toUpperCase());

        ChildEntity savedChild = childRepository.save(childEntity);
        loggedInUser.getChildren()
                    .add(modelMapper.map(savedChild, ChildViewModel.class));

        ChildViewModel childViewModel = modelMapper.map(savedChild, ChildViewModel.class);

        childViewModel.setAllergies(savedChild.getAllergies()
                                              .stream()
                                              .map(allergyEntity -> allergyEntity.getAllergenName()
                                                                                 .name())
                                              .map(string -> {
                                                  if (string.contains("_")) {
                                                      string = string.replace("_", " ");
                                                  }

                                                  return string;
                                              })
                                              .collect(Collectors.joining(", ")));

        String emailContent = String.format("В базата данни е регистрирано ново дете с id = %d! Моля проверете приложените документи и извършете необходимите действия, ако е необходимо!", savedChild.getId());

        try {
            emailService.sendEmail(recipientEmail, subjectAddKid, emailContent);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return childViewModel;

    }

    @Override
    public List<AllergicChildViewModel> getAllAllergicChildren() {
        return childRepository.findAll()
                              .stream()
                              .filter(childEntity -> !childEntity.getAllergies()
                                                                 .contains(allergyService.findByName(AllergyEnum.НЯМА)
                                                                                         .orElseThrow(ObjectNotFoundException::new))
                              )
                              .map(this::mapToAllergicChildViewModel)
                              .collect(Collectors.toList());
    }

    private AllergicChildViewModel mapToAllergicChildViewModel(ChildEntity entity) {
        AllergicChildViewModel allergicChildViewModel = modelMapper.map(entity, AllergicChildViewModel.class);

        allergicChildViewModel.setServicePoint(entity.getParent()
                                                     .getServicePoint()
                                                     .getName());

        allergicChildViewModel.setAllergies(entity.getAllergies()
                                                  .stream()
                                                  .map(allergyEntity -> allergyEntity.getAllergenName()
                                                                                     .name())
                                                  .map(str -> {
                                                      if (str.contains("_")) {
                                                          str = str.replace("_", " ");
                                                      }

                                                      return str;
                                                  })
                                                  .collect(Collectors.joining(", ")));

        return allergicChildViewModel;
    }

    private AgeGroupEnum defineAgeGroup(LocalDate birthDate) {
        if (birthDate.isBefore(LocalDate.now()
                                        .minusMonths(12))) {
            return AgeGroupEnum.ГОЛЕМИ;
        } else {
            return AgeGroupEnum.МАЛКИ;
        }
    }

    private Set<AllergyEntity> defineAllergies(String allergy) {
        String[] allergies = allergy.split(",");

        return Arrays.stream(allergies)
                     .map(this::mapToEntity)
                     .collect(Collectors.toSet());
    }

    private AllergyEntity mapToEntity(String name) {
        return allergyService.findByName(AllergyEnum.valueOf(name.toUpperCase()))
                             .orElseThrow(ObjectNotFoundException::new);
    }

}
