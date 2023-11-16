package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.model.cloudinary.CloudinaryImage;
import bg.softuni.childrenkitchen.service.interfaces.CloudinaryService;
import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private static final String TEMP_FILE = "temp_file";
    private static  final String URL = "url";
    private static final String PUBLIC_ID = "public_id";
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public CloudinaryImage uploadImage(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile(TEMP_FILE, multipartFile.getOriginalFilename());

        multipartFile.transferTo(tempFile);

        try {
            @SuppressWarnings("unchecked")
            Map<String, String> uploadResult = cloudinary.uploader().upload(tempFile, Map.of());

            String url = uploadResult.getOrDefault(URL,
                    "https://static.vecteezy.com/system/resources/previews/005/337/799/original/icon-image-not-found-free-vector.jpg");

            String publicId = uploadResult.getOrDefault(PUBLIC_ID, "");

            CloudinaryImage cloudinaryImage = new CloudinaryImage();
            cloudinaryImage.setUrl(url);
            cloudinaryImage.setPublicKey(publicId);

            return cloudinaryImage;


        } catch (IOException e) {
            throw new RuntimeException("Not uploaded pictures");
        } finally {
            tempFile.delete();
        }
    }

    @Override
    public boolean deleteImage(String publicKey) {
        try {
            this.cloudinary.uploader().destroy(publicKey, Map.of());
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
