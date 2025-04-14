package com.vpd.Image;

import com.vpd.ApiResponse.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image addImage(MultipartFile imageFile) {

        try {
            Image image = new Image();

            image.setImageName(imageFile.getOriginalFilename());
            image.setImageType(imageFile.getContentType());
            image.setImageData(imageFile.getBytes());

            imageRepository.save(image);

            return image;
        } catch (Exception exception) {
            return null;
        }
    }
}
