package com.ing.hubs.loan.api.validation.impl;

import com.ing.hubs.loan.api.exception.LoanAlreadyPaidException;
import com.ing.hubs.loan.api.validation.context.LoanPaymentContext;
import com.ing.hubs.loan.api.validation.rule.LoanValidatorRule;
import org.springframework.stereotype.Component;

@Component
public class PayLoanAlreadyPaidValidator implements LoanValidatorRule<LoanPaymentContext> {

    @Override
    public void validate(LoanPaymentContext context) {
        if (Boolean.TRUE.equals(context.loanDto().isPaid())) {
            throw new LoanAlreadyPaidException("Loan is already fully paid.");
        }
    }
}
