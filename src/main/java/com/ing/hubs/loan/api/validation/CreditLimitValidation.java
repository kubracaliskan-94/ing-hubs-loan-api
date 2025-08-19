package com.ing.hubs.loan.api.validation;

import com.ing.hubs.loan.api.dto.CustomerDto;
import com.ing.hubs.loan.api.dto.LoanDto;
import jakarta.validation.ValidationException;

import java.math.BigDecimal;

public class CreditLimitValidation implements LoanValidationStrategy {


    @Override
    public void validate(LoanValidationContext loanValidationContext) {
        CustomerDto customerDto = loanValidationContext.customerDto();
        LoanDto loanDto = loanValidationContext.loanDto();

        BigDecimal totalLoanAmount = loanDto.loanAmount().multiply(BigDecimal.ONE.add(loanDto.interestRate()));
        BigDecimal availableLimit = customerDto.creditLimit().subtract(customerDto.userCreditLimit());

        if (totalLoanAmount.compareTo(availableLimit) > 0) {
            throw new ValidationException("Customer does not have enough credit limit");
        }
    }
}
