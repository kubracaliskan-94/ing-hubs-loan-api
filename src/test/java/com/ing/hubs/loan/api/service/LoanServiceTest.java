package com.ing.hubs.loan.api.service;

import com.ing.hubs.loan.api.controller.response.LoanPaymentResult;
import com.ing.hubs.loan.api.exception.ResourceNotFoundException;
import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.model.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.model.entity.*;
import com.ing.hubs.loan.api.model.enums.RoleType;
import com.ing.hubs.loan.api.repository.LoanRepository;
import com.ing.hubs.loan.api.service.domain.LoanDomainService;
import com.ing.hubs.loan.api.validation.LoanCreationValidatorExecutor;
import com.ing.hubs.loan.api.validation.LoanPaymentValidatorExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {


    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private LoanCreationValidatorExecutor loanCreationValidatorExecutor;

    @Mock
    private LoanPaymentValidatorExecutor loanPaymentValidatorExecutor;

    @Mock
    private LoanDomainService loanDomainService;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Disabled
    void shouldSaveLoan() {
        Customer customer = createCustomerWithUser(RoleType.ROLE_CUSTOMER);
        Loan loan = createLoan(customer, BigDecimal.valueOf(10000));
        LoanInstallment installment1 = new LoanInstallment(1L, loan, BigDecimal.valueOf(500), BigDecimal.ZERO, null, null, false, LocalDateTime.now(), LocalDateTime.now());
        LoanInstallment installment2 = new LoanInstallment(2L, loan, BigDecimal.valueOf(500), BigDecimal.ZERO, null, null, false, LocalDateTime.now(), LocalDateTime.now());
        loan.setInstallments(List.of(installment1, installment2));
        LoanDto loanDto = new LoanDto(1L, null, BigDecimal.valueOf(1000), 6,
                BigDecimal.valueOf(0.1), false, LocalDateTime.now(), LocalDateTime.now());

        when(customerService.findById(1L)).thenReturn(customer);
        when(loanDomainService.createLoan(customer, null, loanDto)).thenReturn(loan);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanDto result = loanService.save(loanDto);

        verify(loanCreationValidatorExecutor).validate(any());
        verify(loanRepository).save(any(Loan.class));
        assertNotNull(result);
    }


    @Test
    void shouldPayLoan() {
        Customer customer = createCustomerWithUser(RoleType.ROLE_CUSTOMER);
        Loan loan = createLoan(customer,BigDecimal.valueOf(1000));
        LoanInstallment installment1 = new LoanInstallment(1L, loan, BigDecimal.valueOf(500), BigDecimal.ZERO, null, null, false, LocalDateTime.now(), LocalDateTime.now());
        LoanInstallment installment2 = new LoanInstallment(2L, loan, BigDecimal.valueOf(500), BigDecimal.ZERO, null, null, false, LocalDateTime.now(), LocalDateTime.now());
        loan.setInstallments(List.of(installment1, installment2));

        when(loanRepository.findByIdWithInstallments(1L)).thenReturn(Optional.of(loan));
        LoanPaymentResult paymentResult = new LoanPaymentResult(1, BigDecimal.valueOf(500), false);
        when(loanDomainService.payInstallments(loan, BigDecimal.valueOf(500))).thenReturn(paymentResult);

        LoanPaymentResult result = loanService.payLoan(1L, BigDecimal.valueOf(500));

        verify(loanPaymentValidatorExecutor).validate(any());
        verify(loanRepository).save(loan);
        assertEquals(paymentResult, result);
    }

    @Test
    void shouldFindLoanById() {
        Customer customer = createCustomerWithUser(RoleType.ROLE_CUSTOMER);
        Loan loan = createLoan(customer, BigDecimal.valueOf(10000));
        when(loanRepository.findByIdWithInstallments(1L)).thenReturn(Optional.of(loan));

        LoanDto result = loanService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenLoanNotFound() {
        when(loanRepository.findByIdWithInstallments(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> loanService.findById(1L)
        );

        assertEquals("Loan not found with id 1", exception.getMessage());
    }

    @Test
    void shouldFindAllLoans() {
        Customer customer = createCustomerWithUser(RoleType.ROLE_CUSTOMER);
        Loan loan1 = createLoan(customer,BigDecimal.valueOf(1000));
        Loan loan2 = createLoan(customer,BigDecimal.valueOf(1000));
        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2));

        List<LoanDto> result = loanService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindLoansByCustomerId() {
        Customer customer = createCustomerWithUser(RoleType.ROLE_CUSTOMER);
        Loan loan1 = createLoan(customer,BigDecimal.valueOf(1000));
        Loan loan2 = createLoan(customer,BigDecimal.valueOf(1000));
        when(loanRepository.findByCustomerId(1L)).thenReturn(List.of(loan1, loan2));

        List<LoanDto> result = loanService.findByIdForCurrentUser(1L);

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindInstallmentsForLoan() {
        Customer customer = createCustomerWithUser(RoleType.ROLE_CUSTOMER);
        Loan loan = createLoan(customer,BigDecimal.valueOf(1000));
        LoanInstallment installment1 = new LoanInstallment(1L, loan, BigDecimal.valueOf(500), BigDecimal.ZERO, null, null, false, LocalDateTime.now(), LocalDateTime.now());
        LoanInstallment installment2 = new LoanInstallment(2L, loan, BigDecimal.valueOf(500), BigDecimal.ZERO, null, null, false, LocalDateTime.now(), LocalDateTime.now());
        loan.setInstallments(List.of(installment1, installment2));

        when(loanRepository.findByIdWithInstallments(1L)).thenReturn(Optional.of(loan));

        List<LoanInstallmentDto> result = loanService.findInstallmentsForLoan(1L);

        assertEquals(2, result.size());
    }

    private Customer createCustomerWithUser(RoleType roleType) {
        Role role = new Role();
        role.setName(roleType);

        User user = new User(
                null,
                "admin",
                "{noop}admin123",
                "System",
                "Admin",
                Set.of(role),
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Customer customer = new Customer(
                null,
                user,
                BigDecimal.valueOf(500_000),
                BigDecimal.ZERO,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return customer;
    }

    private Loan createLoan(Customer customer, BigDecimal amount) {
        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(amount);
        return loan;
    }
}
