package com.ing.hubs.loan.api.validation;

import com.ing.hubs.loan.api.dto.LoanDto;
import jakarta.validation.ValidationException;

import java.util.Set;

public class InstallmentValidator implements LoanValidationStrategy {
    private static final Set<Integer> ALLOWED_INSTALLMENTS = Set.of(6, 9, 12, 24);

    @Override
    public void validate(LoanValidationContext loanValidationContext) {
        LoanDto loanDto = loanValidationContext.loanDto();
        if (!ALLOWED_INSTALLMENTS.contains(loanDto.numberOfInstallments())) {
            throw new ValidationException("Number of installments must be 6, 9, 12, or 24");
        }
    }
}
