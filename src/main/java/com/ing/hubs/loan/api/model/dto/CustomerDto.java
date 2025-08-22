package com.ing.hubs.loan.api.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerDto(
        Long id,
        UserDto userDto,
        BigDecimal creditLimit,
        BigDecimal usedCreditLimit,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
