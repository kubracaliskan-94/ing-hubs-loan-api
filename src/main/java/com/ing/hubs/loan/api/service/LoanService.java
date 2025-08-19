package com.ing.hubs.loan.api.service;

import com.ing.hubs.loan.api.dto.LoanDto;
import com.ing.hubs.loan.api.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.exception.ResourceNotFoundException;
import com.ing.hubs.loan.api.mapper.CustomerMapper;
import com.ing.hubs.loan.api.mapper.LoanInstallmentMapper;
import com.ing.hubs.loan.api.mapper.LoanMapper;
import com.ing.hubs.loan.api.model.Customer;
import com.ing.hubs.loan.api.model.Employee;
import com.ing.hubs.loan.api.model.Loan;
import com.ing.hubs.loan.api.repository.CustomerRepository;
import com.ing.hubs.loan.api.repository.EmployeeRepository;
import com.ing.hubs.loan.api.repository.LoanRepository;
import com.ing.hubs.loan.api.service.domain.LoanDomainService;
import com.ing.hubs.loan.api.validation.LoanValidationContext;
import com.ing.hubs.loan.api.validation.validator.LoanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final LoanValidator loanValidator;
    private final LoanDomainService loanDomainService;
    private final UserService userService;

    @Transactional
    public LoanDto save(LoanDto loanDto) {
        Customer customer = customerRepository.findById(loanDto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Employee employee = employeeRepository.findById(loanDto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));


        loanValidator.validate(new LoanValidationContext(loanDto, CustomerMapper.toDto(customer)));

        Loan loan = loanDomainService.createLoan(customer, employee, loanDto);

        loanRepository.save(loan);
        //customerRepository.save(customer);

        return LoanMapper.toDto(loan);
    }

    public List<LoanDto> findAll() {
        return loanRepository.findAll()
                .stream()
                .map(LoanMapper::toDto)
                .collect(Collectors.toList());
    }

    public LoanDto findDtoById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id " + id));
        return LoanMapper.toDto(loan);
    }

    public LoanDto findByIdForCurrentUser(Long loanId) throws AccessDeniedException {

        Loan loan = loanRepository.findByIdWithInstallments(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id " + loanId));
        return LoanMapper.toDto(loan);
    }

    public Loan findById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id " + id));
    }

    public void delete(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Loan not found with id " + id);
        }
        loanRepository.deleteById(id);
    }

    public List<LoanInstallmentDto> findInstallmentsForLoan(Long loanId) {
        Loan loan = loanRepository.findByIdWithInstallments(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id " + loanId));

        return loan.getInstallments().stream()
                .map(LoanInstallmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
