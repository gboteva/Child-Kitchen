package bg.softuni.childrenkitchen.validation.annotation;

import bg.softuni.childrenkitchen.validation.AgeBoundariesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeBoundariesValidator.class)
public @interface OutOfAgeKid {
    String message() default "The child does not fall within the age limit with which we work!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
