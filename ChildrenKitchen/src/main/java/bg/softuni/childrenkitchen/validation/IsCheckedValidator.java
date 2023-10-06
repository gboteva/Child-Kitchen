package bg.softuni.childrenkitchen.validation;

import bg.softuni.childrenkitchen.validation.annotation.IsChecked;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsCheckedValidator implements ConstraintValidator<IsChecked, Boolean> {
    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        return value;
    }
}
