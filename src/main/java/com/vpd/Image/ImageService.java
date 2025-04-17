package com.vpd.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image addImage(MultipartFile imageFile) {

        try {
            Image image = new Image();

            image.setImageName(imageFile.getOriginalFilename());
            image.setImageType(imageFile.getContentType());
            image.setImageData(imageFile.getBytes());

            if(image.getImageData() == null)
                return null;

            imageRepository.save(image);

            return image;
        } catch (Exception exception) {
            return null;
        }
    }

    public Image addImage(Image image) {

        try {
            imageRepository.save(image);

            return image;
        } catch (Exception exception) {
            return null;
        }
    }

    public Image deleteImage(Image poster) {

        try {
            if(poster == null)
                return null;

            imageRepository.delete(poster);

            return poster;
        } catch (Exception exception) {
            return null;
        }
    }
}