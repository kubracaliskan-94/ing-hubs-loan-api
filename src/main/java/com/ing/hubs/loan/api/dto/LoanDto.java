package com.ing.hubs.loan.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record LoanDto(
        Long id,
        @NotNull(message = "CustomerId is required")
        Long customerId,
        @NotNull(message = "EmployeeId is required")
        Long employeeId,
        @NotNull
        @DecimalMin(value = "100.00", message = "Loan amount must be at least 100")
        BigDecimal loanAmount,
        @NotNull
        Integer numberOfInstallments,
        @NotNull
        BigDecimal interestRate,
        Boolean isPaid,
        LocalDateTime createDate,
        LocalDateTime updateDate
) {
}
