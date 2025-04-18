package com.vpd.Travel.DTO;

import java.time.LocalDate;
import java.util.List;

public record TravelBasicDTO(String id,
                             String name,
                             int totalCollections,
                             int totalMovies,
                             List<TravellerDTO> travelers,
                             LocalDate startDate,
                             LocalDate endDate) {
}
