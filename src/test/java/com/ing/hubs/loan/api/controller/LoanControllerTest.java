package com.ing.hubs.loan.api.controller;

import com.ing.hubs.loan.api.controller.request.LoanPaymentRequest;
import com.ing.hubs.loan.api.controller.response.LoanPaymentResult;
import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.model.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoanControllerTest {

    private LoanService loanService;
    private LoanController loanController;

    @BeforeEach
    void setUp() {
        loanService = mock(LoanService.class);
        loanController = new LoanController(loanService);
    }

    @Test
    void shouldCreateLoan() {
        LoanDto loanDto = new LoanDto(1L, null, BigDecimal.valueOf(1000), 6, BigDecimal.valueOf(0.1), false, LocalDateTime.now(), LocalDateTime.now());
        when(loanService.save(loanDto)).thenReturn(loanDto);

        ResponseEntity<LoanDto> response = loanController.createLoan(loanDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(loanDto, response.getBody());
        verify(loanService).save(loanDto);
    }

    @Test
    void shouldPayLoan() {
        LoanPaymentRequest request = new LoanPaymentRequest(BigDecimal.valueOf(500));
        LoanPaymentResult result = new LoanPaymentResult(1, BigDecimal.valueOf(500), false);
        when(loanService.payLoan(1L, BigDecimal.valueOf(500))).thenReturn(result);

        ResponseEntity<LoanPaymentResult> response = loanController.payLoan(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(result, response.getBody());
        verify(loanService).payLoan(1L, BigDecimal.valueOf(500));
    }

    @Test
    void shouldGetAllLoans() {
        LoanDto loanDto1 = new LoanDto(1L, null, BigDecimal.valueOf(1000), 6, BigDecimal.valueOf(0.1), false, LocalDateTime.now(), LocalDateTime.now());
        LoanDto loanDto2 = new LoanDto(2L, null, BigDecimal.valueOf(2000), 6, BigDecimal.valueOf(0.1), false, LocalDateTime.now(), LocalDateTime.now());

        when(loanService.findAll()).thenReturn(List.of(loanDto1, loanDto2));

        ResponseEntity<List<LoanDto>> response = loanController.getAllLoans();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(loanService).findAll();
    }

    @Test
    void shouldGetLoanById() {
        LoanDto loanDto = new LoanDto(1L, null, BigDecimal.valueOf(1000), 6, BigDecimal.valueOf(0.1), false, LocalDateTime.now(), LocalDateTime.now());
        when(loanService.findById(1L)).thenReturn(loanDto);

        ResponseEntity<LoanDto> response = loanController.getLoanById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(loanDto, response.getBody());
        verify(loanService).findById(1L);
    }

    @Test
    void shouldGetLoanInstallments() {
        LoanInstallmentDto installment1 = new LoanInstallmentDto(1L, 1L, BigDecimal.valueOf(500), BigDecimal.ZERO, null, null, false, LocalDateTime.now(), LocalDateTime.now());
        LoanInstallmentDto installment2 = new LoanInstallmentDto(2L, 1L, BigDecimal.valueOf(500), BigDecimal.ZERO, null, null, false, LocalDateTime.now(), LocalDateTime.now());
        when(loanService.findInstallmentsForLoan(1L)).thenReturn(List.of(installment1, installment2));

        ResponseEntity<List<LoanInstallmentDto>> response = loanController.getLoanInstallments(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(loanService).findInstallmentsForLoan(1L);
    }

    @Test
    void shouldGetAllLoansForCustomerId() {
        LoanDto loanDto1 = new LoanDto(1L, null, BigDecimal.valueOf(1000), 6, BigDecimal.valueOf(0.1), false, LocalDateTime.now(), LocalDateTime.now());
        LoanDto loanDto2 = new LoanDto(2L, null, BigDecimal.valueOf(2000), 6, BigDecimal.valueOf(0.1), false, LocalDateTime.now(), LocalDateTime.now());

        when(loanService.findByIdForCurrentUser(1L)).thenReturn(List.of(loanDto1, loanDto2));

        ResponseEntity<List<LoanDto>> response = loanController.getAllLoansForCustomerId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(loanService).findByIdForCurrentUser(1L);
    }
}
