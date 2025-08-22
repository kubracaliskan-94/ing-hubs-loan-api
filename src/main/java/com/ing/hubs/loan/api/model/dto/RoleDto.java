package com.ing.hubs.loan.api.model.dto;

import com.ing.hubs.loan.api.model.enums.RoleType;

import java.util.Set;

public record RoleDto(
        Long id,
        RoleType roleType,
        Set<UserDto> userDtoList
) {
}
