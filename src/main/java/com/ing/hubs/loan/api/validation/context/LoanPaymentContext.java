package com.ing.hubs.loan.api.validation.context;

import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.model.dto.LoanInstallmentDto;

import java.math.BigDecimal;
import java.util.List;

public record LoanPaymentContext(LoanDto loanDto, List<LoanInstallmentDto> installmentDtoList, BigDecimal amount) {
}
