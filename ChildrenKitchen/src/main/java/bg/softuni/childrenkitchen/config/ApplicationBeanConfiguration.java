package bg.softuni.childrenkitchen.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        modelMapper.addConverter(new Converter<String, BigDecimal>() {
            @Override
            public BigDecimal convert(MappingContext<String, BigDecimal> mappingContext) {

                String price = mappingContext.getSource().substring(0, mappingContext.getSource().indexOf(" "));
                price = price.replace(",", ".");

                return BigDecimal.valueOf(Double.parseDouble(price));
            }
        });

        return modelMapper;
    }

    @Bean
    public RestTemplate create(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.
                build();
    }


}
