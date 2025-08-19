package com.ing.hubs.loan.api.validation;

import com.ing.hubs.loan.api.dto.CustomerDto;
import com.ing.hubs.loan.api.dto.LoanDto;

public record LoanValidationContext(
        LoanDto loanDto,
        CustomerDto customerDto) {
}
