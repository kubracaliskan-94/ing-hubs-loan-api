package com.ing.hubs.loan.api.validation.impl;

import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.exception.InvalidInterestRateException;
import com.ing.hubs.loan.api.validation.context.LoanCreationContext;
import com.ing.hubs.loan.api.validation.rule.LoanValidatorRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LoanInterestRateValidator implements LoanValidatorRule<LoanCreationContext> {

    @Override
    public void validate(LoanCreationContext context) {
        LoanDto loanDto = context.loanDto();
        BigDecimal rate = loanDto.interestRate();
        if (rate.compareTo(BigDecimal.valueOf(0.1)) < 0 || rate.compareTo(BigDecimal.valueOf(0.5)) > 0) {
            throw new InvalidInterestRateException("Interest rate must be between 0.1 and 0.5");
        }
    }
}
