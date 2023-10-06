package bg.softuni.childrenkitchen.validation;

import bg.softuni.childrenkitchen.validation.annotation.ExistFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ExistFileValidator implements ConstraintValidator<ExistFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return !file.isEmpty();
    }
}
