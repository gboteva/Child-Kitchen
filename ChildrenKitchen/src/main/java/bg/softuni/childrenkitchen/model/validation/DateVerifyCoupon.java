package bg.softuni.childrenkitchen.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VerifyCouponDateValidator.class)
public @interface DateVerifyCoupon {
    String message() default "Invalid date. The date must be after two day from now!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
