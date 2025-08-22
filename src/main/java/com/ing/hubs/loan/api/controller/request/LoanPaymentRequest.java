package com.ing.hubs.loan.api.controller.request;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record LoanPaymentRequest(
        @DecimalMin(value="100", message = "Amount must be at least 100")
        BigDecimal amount
) {
}
