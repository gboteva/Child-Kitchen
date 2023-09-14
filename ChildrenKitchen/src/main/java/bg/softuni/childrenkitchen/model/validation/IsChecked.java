package bg.softuni.childrenkitchen.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsCheckedValidator.class)
public @interface IsChecked {
    String message() default "Consent to the condition for processing personal data is mandatory!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
