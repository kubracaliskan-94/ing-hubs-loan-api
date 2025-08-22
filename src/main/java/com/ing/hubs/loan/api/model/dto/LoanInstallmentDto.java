package com.ing.hubs.loan.api.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanInstallmentDto(
        Long id,
        Long loanId,
        BigDecimal amount,
        BigDecimal paidAmount,
        LocalDate dueDate,
        LocalDateTime paymentDate,
        Boolean isPaid,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}

