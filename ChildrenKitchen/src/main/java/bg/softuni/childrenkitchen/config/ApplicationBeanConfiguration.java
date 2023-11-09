package bg.softuni.childrenkitchen.config;

import bg.softuni.childrenkitchen.model.entity.AllergyEntity;
import bg.softuni.childrenkitchen.model.entity.ChildEntity;
import bg.softuni.childrenkitchen.model.view.AllergicChildViewModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new Converter<LocalDate, String>() {
            @Override
            public String convert(MappingContext<LocalDate, String> mappingContext) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");

                return mappingContext.getSource().format(formatter);

            }
        });


        modelMapper.createTypeMap(ChildEntity.class, AllergicChildViewModel.class)
                .addMappings(mapping -> {
                    mapping.using(new Converter<Set<AllergyEntity>, String>() {
                               @Override
                               public String convert(MappingContext<Set<AllergyEntity>, String> context) {
                                   return context.getSource()
                                                 .stream()
                                                 .map(allergyEntity -> allergyEntity.getAllergenName()
                                                                                    .name())
                                                 .map(string -> {
                                                     if (string.contains("_")) {
                                                         string = string.replace("_", " ");
                                                     }

                                                     return string;
                                                 })
                                                 .collect(Collectors.joining(", "));
                               }
                           })
                            .map(ChildEntity::getAllergies, AllergicChildViewModel::setAllergies);
                });


        return modelMapper;
    }



}
