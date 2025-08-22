package com.ing.hubs.loan.api.mapper;

import com.ing.hubs.loan.api.model.dto.UserDto;
import com.ing.hubs.loan.api.model.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public static UserDto toDto(User entity) {
        if (entity == null) return null;

        return new UserDto(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getName(),
                entity.getSurname(),
                null,
                null,
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.userName());
        user.setPassword(dto.password());
        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setRoles(null);
        user.setEmployee(null);
        return null;
    }
}
