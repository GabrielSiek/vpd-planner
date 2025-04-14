package com.vpd.Travel.DTO;

import java.time.LocalDate;

public record UpdateDatesDTO(String id,
                             LocalDate startDate,
                             LocalDate endDate) {
}
