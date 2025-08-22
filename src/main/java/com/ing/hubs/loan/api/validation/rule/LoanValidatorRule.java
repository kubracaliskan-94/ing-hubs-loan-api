package com.ing.hubs.loan.api.validation.rule;

public interface LoanValidatorRule<T> {

    void validate(T context);
}
