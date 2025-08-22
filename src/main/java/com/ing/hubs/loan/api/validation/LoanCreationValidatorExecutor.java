package com.ing.hubs.loan.api.validation;

import com.ing.hubs.loan.api.validation.context.LoanCreationContext;
import com.ing.hubs.loan.api.validation.rule.LoanValidatorRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoanCreationValidatorExecutor {

    private final List<LoanValidatorRule<LoanCreationContext>> validatorRules;

    public void validate(LoanCreationContext context) {
        validatorRules.forEach(v -> v.validate(context));
    }
}
