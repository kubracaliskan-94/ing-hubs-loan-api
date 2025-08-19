package com.ing.hubs.loan.api.validation.validator;

import com.ing.hubs.loan.api.validation.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanValidator {
    private final List<LoanValidationStrategy> strategies;

    public LoanValidator() {
        this.strategies = List.of(
                new InstallmentValidator(),
                new InterestRateValidation(),
                new CreditLimitValidation());
    }

    public void validate(LoanValidationContext loanValidationContext) {
        strategies.forEach(strategy -> strategy.validate(loanValidationContext));
    }
}
