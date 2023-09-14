package bg.softuni.childrenkitchen.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistFileValidator.class)
public @interface ExistFile {
    String message() default "There is not submitted file!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
