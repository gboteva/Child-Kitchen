package bg.softuni.childrenkitchen.service.interfaces;

import bg.softuni.childrenkitchen.model.CloudinaryImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    CloudinaryImage uploadImage(MultipartFile multipartFile) throws IOException;
    boolean deleteImage(String publicKey);
}
