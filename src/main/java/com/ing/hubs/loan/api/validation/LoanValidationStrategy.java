package com.ing.hubs.loan.api.validation;

public interface LoanValidationStrategy {
    void validate(LoanValidationContext loanValidationContext);
}
