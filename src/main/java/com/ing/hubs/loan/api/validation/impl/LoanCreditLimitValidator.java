package com.ing.hubs.loan.api.validation.impl;

import com.ing.hubs.loan.api.model.dto.CustomerDto;
import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.exception.CustomerLimitExceededException;
import com.ing.hubs.loan.api.validation.context.LoanCreationContext;
import com.ing.hubs.loan.api.validation.rule.LoanValidatorRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LoanCreditLimitValidator implements LoanValidatorRule<LoanCreationContext> {


    @Override
    public void validate(LoanCreationContext context) {
        CustomerDto customerDto = context.customerDto();
        LoanDto loanDto = context.loanDto();

        BigDecimal totalLoanAmount = loanDto.loanAmount().multiply(BigDecimal.ONE.add(loanDto.interestRate()));
        BigDecimal availableLimit = customerDto.creditLimit().subtract(customerDto.usedCreditLimit());

        if (totalLoanAmount.compareTo(availableLimit) > 0) {
            throw new CustomerLimitExceededException("Customer does not have enough credit limit");
        }
    }
}
