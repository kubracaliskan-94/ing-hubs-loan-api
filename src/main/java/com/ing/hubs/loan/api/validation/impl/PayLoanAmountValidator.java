package com.ing.hubs.loan.api.validation.impl;

import com.ing.hubs.loan.api.model.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.exception.NoInstallmentsPayableException;
import com.ing.hubs.loan.api.validation.context.LoanPaymentContext;
import com.ing.hubs.loan.api.validation.rule.LoanValidatorRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class PayLoanAmountValidator implements LoanValidatorRule<LoanPaymentContext> {

    @Override
    public void validate(LoanPaymentContext context) {
        BigDecimal amount = context.amount();

        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);

        List<LoanInstallmentDto> payableInstallments = context.installmentDtoList().stream()
                .filter(i -> !i.isPaid())
                .filter(i -> i.dueDate().isBefore(threeMonthsLater))
                .sorted(Comparator.comparing(LoanInstallmentDto::dueDate))
                .toList();

        if (payableInstallments.isEmpty()) {
            throw new NoInstallmentsPayableException("There is not any installment to pay within next three month.");
        }

        if (amount.compareTo(payableInstallments.get(0).amount()) < 0) {
            throw new NoInstallmentsPayableException("The amount is insufficient to pay even a single installment.");
        }
    }
}
