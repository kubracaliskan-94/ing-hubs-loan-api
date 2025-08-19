package com.ing.hubs.loan.api.mapper;

import com.ing.hubs.loan.api.dto.RoleDto;
import com.ing.hubs.loan.api.model.Role;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleMapper {
    public static RoleDto toDto(Role entity) {
        if (entity == null) return null;

        return new RoleDto(
                entity.getId(),
                entity.getName(),
                null
        );
    }

    public static Role toEntity(RoleDto dto) {
        if (dto == null) return null;

        Role role = new Role();
        role.setName(dto.roleType());
        return role;
    }
}
