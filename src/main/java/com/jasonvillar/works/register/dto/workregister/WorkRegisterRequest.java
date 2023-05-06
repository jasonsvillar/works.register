package com.jasonvillar.works.register.dto.workregister;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record WorkRegisterRequest(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        LocalDate dateFrom,
        @NotNull
        @Schema(type = "string", format = "time", pattern = "^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$", example = "23:59:59")
        LocalTime timeFrom,
        @NotNull
        LocalDate dateTo,
        @NotNull
        @Schema(type = "string", format = "time", pattern = "^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$", example = "23:59:59")
        LocalTime timeTo,
        @NotNull
        BigDecimal payment,
        @NotNull
        long userId,
        @NotNull
        long serviceId,
        @NotNull
        long clientId
) {
}
