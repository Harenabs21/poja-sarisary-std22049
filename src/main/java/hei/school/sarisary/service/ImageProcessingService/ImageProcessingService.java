package hei.school.sarisary.service.ImageProcessingService;
import hei.school.sarisary.file.BucketComponent;
import hei.school.sarisary.file.FileHash;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageProcessingService {

    private final BucketComponent bucketComponent;

    public BufferedImage convertToGrayscale(MultipartFile file) {
        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            return Scalr.apply(originalImage, Scalr.OP_GRAYSCALE);
        } catch (Exception e) {
            throw new RuntimeException("Error converting image", e);
        }
    }

    public FileHash upload(BufferedImage image, String bucketKey,  String name) {
        try {
            File convertedFile = File.createTempFile(name + "-grayscale", "png");
            ImageIO.write(image, "png", convertedFile);
            return bucketComponent.upload(convertedFile, bucketKey);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to S3", e);
        }
    }
}
