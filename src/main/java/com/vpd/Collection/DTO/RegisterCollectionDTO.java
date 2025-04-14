package com.vpd.Collection.DTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record RegisterCollectionDTO(String name,
                                    List<String> moviesId,
                                    String travelId,
                                    MultipartFile imageFile) {
}
