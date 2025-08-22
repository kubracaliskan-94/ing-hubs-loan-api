package com.ing.hubs.loan.api.validation.impl;

import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.exception.InvalidInstallmentCountException;
import com.ing.hubs.loan.api.validation.context.LoanCreationContext;
import com.ing.hubs.loan.api.validation.rule.LoanValidatorRule;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LoanInstallmentCountValidator implements LoanValidatorRule<LoanCreationContext> {
    private static final Set<Integer> ALLOWED_INSTALLMENTS = Set.of(6, 9, 12, 24);

    @Override
    public void validate(LoanCreationContext context) {
        LoanDto loanDto = context.loanDto();
        if (!ALLOWED_INSTALLMENTS.contains(loanDto.numberOfInstallments())) {
            throw new InvalidInstallmentCountException("Number of installments must be 6, 9, 12, or 24");
        }
    }
}
