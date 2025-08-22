package com.ing.hubs.loan.api.controller.response;

import java.math.BigDecimal;

public record LoanPaymentResult(
        int installmentsPaid,
        BigDecimal totalAmountPaid,
        boolean loanFullyPaid
) {
}
