package com.ing.hubs.loan.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record LoanDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        @NotNull(message = "CustomerId is required")
        Long customerId,
        @NotNull
        @DecimalMin(value = "1000.00", message = "Loan amount must be at least 1000")
        BigDecimal loanAmount,
        @NotNull
        @Min(value = 1, message = "Number of Installment count must be at least 1")
        Integer numberOfInstallments,
        @NotNull
        @DecimalMin(value = "0.01", message = "Interest rate be at least 0.1")
        BigDecimal interestRate,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Boolean isPaid,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime createDate,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime updateDate
) {
}
