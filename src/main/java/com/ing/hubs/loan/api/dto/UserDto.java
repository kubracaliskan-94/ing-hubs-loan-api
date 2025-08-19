package com.ing.hubs.loan.api.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserDto(
        Long id,
        String userName,
        String password,
        String name,
        String surname,
        Set<RoleDto> roleDtoList,
        CustomerDto customerDto,
        EmployeeDto employeeDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
