package hei.school.sarisary.endpoint.rest.controller.ImageProcessingController;

import hei.school.sarisary.file.FileHash;
import hei.school.sarisary.service.ImageProcessingService.ImageProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/black-and-white")
@AllArgsConstructor
public class ImageProcessingController {
    private ImageProcessingService imageProcessingService;


    @PutMapping
    public ResponseEntity<FileHash> convertAndUploadToS3(
            @RequestParam MultipartFile file,
            @RequestParam String bucketKey,
            @RequestParam(required = false) String name) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            BufferedImage convertedImage = imageProcessingService.convertToGrayscale(file);
            String originalFileName = name != null ? name : file.getOriginalFilename();
            assert originalFileName != null;
            FileHash fileHash =
                    imageProcessingService.upload(convertedImage, bucketKey, originalFileName);
            return new ResponseEntity<>(fileHash, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

