package com.ing.hubs.loan.api.service.domain;

import com.ing.hubs.loan.api.dto.LoanDto;
import com.ing.hubs.loan.api.mapper.LoanMapper;
import com.ing.hubs.loan.api.model.Customer;
import com.ing.hubs.loan.api.model.Employee;
import com.ing.hubs.loan.api.model.Loan;
import com.ing.hubs.loan.api.model.LoanInstallment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanDomainService {

    public Loan createLoan(Customer customer, Employee employee, LoanDto loanDto) {
        Loan loan = LoanMapper.toEntity(loanDto);
        loan.setCustomer(customer);
        loan.setEmployee(employee);

        BigDecimal totalAmount = loanDto.loanAmount()
                .multiply(BigDecimal.ONE.add(loanDto.interestRate()));
        BigDecimal installmentAmount = totalAmount.divide(
                BigDecimal.valueOf(loanDto.numberOfInstallments()), 2, RoundingMode.HALF_UP);

        List<LoanInstallment> installments = generateInstallments(loan, installmentAmount, loanDto.numberOfInstallments());
        loan.setInstallments(installments);

        customer.setUsedCreditLimit(
                customer.getUsedCreditLimit().add(loanDto.loanAmount()));

        return loan;
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
