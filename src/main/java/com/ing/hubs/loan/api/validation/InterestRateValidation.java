package com.ing.hubs.loan.api.validation;

import com.ing.hubs.loan.api.dto.LoanDto;
import jakarta.validation.ValidationException;

import java.math.BigDecimal;

public class InterestRateValidation implements LoanValidationStrategy {

    @Override
    public void validate(LoanValidationContext loanValidationContext) {
        LoanDto loanDto = loanValidationContext.loanDto();
        BigDecimal rate = loanDto.interestRate();
        if (rate.compareTo(BigDecimal.valueOf(0.1)) < 0 || rate.compareTo(BigDecimal.valueOf(0.5)) > 0) {
            throw new ValidationException("Interest rate must be between 0.1 and 0.5");
        }

    }
}
