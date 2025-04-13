package com.vpd.Travel.DTO;

import java.time.LocalDate;
import java.util.List;

public record RegisterTravelDTO(String name,
                                List<String> emails,
                                LocalDate startDate,
                                LocalDate endDate) {
}
