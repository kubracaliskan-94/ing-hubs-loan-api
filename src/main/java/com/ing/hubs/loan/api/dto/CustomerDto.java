package com.ing.hubs.loan.api.dto;

import java.time.LocalDateTime;

public record CustomerDto(
        Long id,
        UserDto userDto,
        java.math.BigDecimal creditLimit,
        java.math.BigDecimal userCreditLimit,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
