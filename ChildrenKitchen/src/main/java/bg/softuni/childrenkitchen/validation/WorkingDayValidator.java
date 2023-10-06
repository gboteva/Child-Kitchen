package bg.softuni.childrenkitchen.validation;

import bg.softuni.childrenkitchen.validation.annotation.WorkingDay;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class WorkingDayValidator implements ConstraintValidator<WorkingDay, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return !date.getDayOfWeek()
                    .name()
                    .equals("SATURDAY") && !date.getDayOfWeek()
                                                .name()
                                                .equals("SUNDAY");
    }
}
