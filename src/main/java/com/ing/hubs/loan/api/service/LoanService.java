package com.ing.hubs.loan.api.service;

import com.ing.hubs.loan.api.controller.response.LoanPaymentResult;
import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.model.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.exception.ResourceNotFoundException;
import com.ing.hubs.loan.api.mapper.CustomerMapper;
import com.ing.hubs.loan.api.mapper.LoanInstallmentMapper;
import com.ing.hubs.loan.api.mapper.LoanMapper;
import com.ing.hubs.loan.api.model.entity.Customer;
import com.ing.hubs.loan.api.model.entity.Loan;
import com.ing.hubs.loan.api.repository.LoanRepository;
import com.ing.hubs.loan.api.service.domain.LoanDomainService;
import com.ing.hubs.loan.api.validation.LoanCreationValidatorExecutor;
import com.ing.hubs.loan.api.validation.LoanPaymentValidatorExecutor;
import com.ing.hubs.loan.api.validation.context.LoanCreationContext;
import com.ing.hubs.loan.api.validation.context.LoanPaymentContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    private final CustomerService customerService;
    private final EmployeeService employeeService;

    private final LoanCreationValidatorExecutor loanCreationValidatorExecutor;
    private final LoanPaymentValidatorExecutor loanPaymentValidatorExecutor;
    private final LoanDomainService loanDomainService;

    @Transactional
    public LoanDto save(LoanDto loanDto) {
        Customer customer = customerService.findById(loanDto.customerId());
        //Employee employee = employeeService.findById(loanDto.employeeId()); // current employee loggedUSer

        loanCreationValidatorExecutor.validate(new LoanCreationContext(loanDto, CustomerMapper.toDto(customer)));

        Loan loan = loanDomainService.createLoan(customer, null, loanDto);
        loanRepository.save(loan);

        return LoanMapper.toDto(loan);
    }

    @Transactional
    public LoanPaymentResult payLoan(Long loanId, BigDecimal amount) {
        Loan loan = findByIdWithInstallments(loanId);

        List<LoanInstallmentDto> installmentDtoList = loan.getInstallments().stream().map(LoanInstallmentMapper::toDto).collect(Collectors.toList());
        LoanPaymentContext context = new LoanPaymentContext(LoanMapper.toDto(loan), installmentDtoList, amount);
        loanPaymentValidatorExecutor.validate(context);

        LoanPaymentResult response = loanDomainService.payInstallments(loan, amount);

        loanRepository.save(loan);
        return response;
    }

    public List<LoanDto> findAll() {
        return loanRepository.findAll()
                .stream()
                .map(LoanMapper::toDto)
                .collect(Collectors.toList());
    }

    public LoanDto findById(Long loanId) {
        Loan loan = findByIdWithInstallments(loanId);
        return LoanMapper.toDto(loan);
    }

    public List<LoanDto> findByIdForCurrentUser(Long customerId) {
        return loanRepository.findByCustomerId(customerId)
                .stream()
                .map(LoanMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LoanInstallmentDto> findInstallmentsForLoan(Long loanId) {
        return findByIdWithInstallments(loanId)
                .getInstallments()
                .stream()
                .map(LoanInstallmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public Loan findByIdWithInstallments(Long loanId) {
        return loanRepository.findByIdWithInstallments(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id " + loanId));
    }
}
