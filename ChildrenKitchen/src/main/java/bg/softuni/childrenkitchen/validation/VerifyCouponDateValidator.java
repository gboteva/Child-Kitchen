package bg.softuni.childrenkitchen.validation;

import bg.softuni.childrenkitchen.validation.annotation.DateVerifyCoupon;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class VerifyCouponDateValidator implements ConstraintValidator<DateVerifyCoupon, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if(date == null){
            return false;
        }

        // TODO: 16.9.2023 г. this must be uncomment!
//        if (LocalDate.now().getDayOfWeek().name().equals("SATURDAY") || LocalDate.now().getDayOfWeek().name().equals("SUNDAY")){
//            return false;
//        }


        if (date.getDayOfWeek().name().equals("SATURDAY") || date.getDayOfWeek().name().equals("SUNDAY")){
            return false;
        }

        if (date.isBefore(LocalDate.now().plusDays(2))){
            return false;
        }

        return true;
    }
}
