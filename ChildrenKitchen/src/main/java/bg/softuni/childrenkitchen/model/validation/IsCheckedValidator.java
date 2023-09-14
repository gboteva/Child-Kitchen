package bg.softuni.childrenkitchen.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsCheckedValidator implements ConstraintValidator<IsChecked, Boolean> {
    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        return value;
    }
}
