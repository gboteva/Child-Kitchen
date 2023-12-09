package bg.softuni.childrenkitchen.validation.annotation;

import bg.softuni.childrenkitchen.validation.WorkingDayValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WorkingDayValidator.class)
public @interface WorkingDay {
    String message() default "The date must be a business day";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
