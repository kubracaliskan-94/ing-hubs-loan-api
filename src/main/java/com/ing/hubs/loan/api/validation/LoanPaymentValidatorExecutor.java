package com.ing.hubs.loan.api.validation;

import com.ing.hubs.loan.api.validation.context.LoanPaymentContext;
import com.ing.hubs.loan.api.validation.rule.LoanValidatorRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoanPaymentValidatorExecutor {

    private final List<LoanValidatorRule<LoanPaymentContext>> validators;

    public void validate(LoanPaymentContext context) {
        validators.forEach(v -> v.validate(context));
    }
}
