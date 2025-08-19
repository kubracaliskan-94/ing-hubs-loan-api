package com.ing.hubs.loan.api.mapper;

import com.ing.hubs.loan.api.dto.CustomerDto;
import com.ing.hubs.loan.api.model.Customer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerMapper {
    public static CustomerDto toDto(Customer entity) {
        if (entity == null) return null;

        return new CustomerDto(
                entity.getId(),
                null,
                entity.getCreditLimit(),
                entity.getUsedCreditLimit(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Customer toEntity(CustomerDto dto) {
        if (dto == null) return null;

        Customer entity = new Customer();
        entity.setUser(null);
        entity.setCreditLimit(dto.creditLimit());
        entity.setUsedCreditLimit(dto.userCreditLimit());
        entity.setCreatedAt(dto.createdAt());
        entity.setUpdatedAt(dto.updatedAt());
        return entity;
    }
}
