package com.ing.hubs.loan.api.service.domain;

import com.ing.hubs.loan.api.controller.response.LoanPaymentResult;
import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.exception.NoInstallmentsPayableException;
import com.ing.hubs.loan.api.mapper.LoanMapper;
import com.ing.hubs.loan.api.model.entity.Customer;
import com.ing.hubs.loan.api.model.entity.Employee;
import com.ing.hubs.loan.api.model.entity.Loan;
import com.ing.hubs.loan.api.model.entity.LoanInstallment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class LoanDomainService {

    public LoanPaymentResult payInstallments(Loan loan, BigDecimal amount) {
        List<LoanInstallment> payableInstallments = findPayableInstallments(loan);

        BigDecimal remainingAmount = amount;
        int installmentsPaid = 0;

        if (payableInstallments.isEmpty() || amount.compareTo(payableInstallments.get(0).getAmount()) < 0) {
            throw new NoInstallmentsPayableException(
                    "The provided amount is insufficient to pay even a single installment."
            );
        }

        for (LoanInstallment inst : payableInstallments) {
            if (remainingAmount.compareTo(inst.getAmount()) >= 0) {
                inst.markAsPaid();
                remainingAmount = remainingAmount.subtract(inst.getAmount());
                installmentsPaid++;
            } else {
                break;
            }
        }

        BigDecimal totalPaid = amount.subtract(remainingAmount);
        loan.updatePaymentStatus();

        return new LoanPaymentResult(installmentsPaid, totalPaid, loan.getIsPaid());
    }

    public Loan createLoan(Customer customer, Employee employee, LoanDto loanDto) {
        Loan loan = LoanMapper.toEntity(loanDto);
        loan.setCustomer(customer);
        loan.setEmployee(employee);

        BigDecimal totalAmount = loanDto.loanAmount().multiply(BigDecimal.ONE.add(loanDto.interestRate()));
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(loanDto.numberOfInstallments()), 2, RoundingMode.HALF_UP);

        List<LoanInstallment> installments = generateInstallments(loan, installmentAmount, loanDto.numberOfInstallments());
        loan.setInstallments(installments);

        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(loanDto.loanAmount()));

        return loan;
    }

    private List<LoanInstallment> findPayableInstallments(Loan loan) {
        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);
        return loan.getInstallments().stream()
                .filter(i -> !i.getIsPaid())
                .filter(i -> !i.getDueDate().isAfter(threeMonthsLater))
                .sorted(Comparator.comparing(LoanInstallment::getDueDate))
                .toList();
    }

    private List<LoanInstallment> generateInstallments(Loan loan, BigDecimal installmentAmount, int numberOfInstallments) {
        List<LoanInstallment> installments = new ArrayList<>();
        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        for (int i = 0; i < numberOfInstallments; i++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setDueDate(dueDate.plusMonths(i));
            installment.setIsPaid(false);
            installments.add(installment);
        }
        return installments;
    }
}
