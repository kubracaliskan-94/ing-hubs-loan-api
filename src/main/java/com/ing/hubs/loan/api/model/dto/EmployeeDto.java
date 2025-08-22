package com.ing.hubs.loan.api.model.dto;

import java.time.LocalDateTime;

public record EmployeeDto(
        Long id,
        UserDto userDto,
        String branch,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
