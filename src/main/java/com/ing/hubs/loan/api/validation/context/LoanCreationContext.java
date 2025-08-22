package com.ing.hubs.loan.api.validation.context;

import com.ing.hubs.loan.api.model.dto.CustomerDto;
import com.ing.hubs.loan.api.model.dto.LoanDto;

public record LoanCreationContext(LoanDto loanDto, CustomerDto customerDto) {
}
