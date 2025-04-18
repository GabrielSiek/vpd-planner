package com.vpd.Travel.DTO;

import java.time.LocalDate;

public record UpdateDatesDTO(LocalDate startDate,
                             LocalDate endDate) {
}
