package bg.softuni.childrenkitchen.validation;

import bg.softuni.childrenkitchen.validation.annotation.OutOfAgeKid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AgeBoundariesValidator implements ConstraintValidator<OutOfAgeKid, LocalDate> {


    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null){
            return false;
        }

        if (birthDate.isBefore(LocalDate.now().minusYears(3))){
            return false;
        }

        if (birthDate.isAfter(LocalDate.now().minusMonths(10))){
            return false;
        }

        return true;
    }
}
