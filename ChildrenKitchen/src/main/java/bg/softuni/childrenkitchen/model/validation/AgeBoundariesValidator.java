package bg.softuni.childrenkitchen.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AgeBoundariesValidator implements ConstraintValidator<OutOfAgeKid, LocalDate> {


    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null){
            return false;
        }

        if (birthDate.isBefore(LocalDate.now().minusMonths(12))){
            int years = LocalDate.now().getYear() - birthDate.getYear();
            int months = LocalDate.now().getMonth().getValue() - birthDate.getMonth().getValue();
            int age = years*12 + months;

            if (age > 36){
                return false;
            }

        }else {
            int months = LocalDate.now().getMonth().getValue() - birthDate.getMonth().getValue();

            if (months < 10){
                return false;
            }
        }

        return true;
    }
}
