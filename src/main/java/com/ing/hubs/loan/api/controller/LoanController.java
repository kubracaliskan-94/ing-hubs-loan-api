package com.ing.hubs.loan.api.controller;

import com.ing.hubs.loan.api.controller.request.LoanPaymentRequest;
import com.ing.hubs.loan.api.controller.response.LoanPaymentResult;
import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.model.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.security.CheckLoanAccessForCustomerId;
import com.ing.hubs.loan.api.security.CheckLoanAccessForLoanId;
import com.ing.hubs.loan.api.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "Loans", description = "APIs for managing loans and installments")
public class LoanController {

    private final LoanService loanService;

    @Operation(
            summary = "Create a new loan",
            description = "Creates a loan for a given customer and employee. Only ADMIN and EMPLOYEE roles are allowed."
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<LoanDto> createLoan(@Valid @RequestBody LoanDto loanDto) {
        LoanDto createdLoan = loanService.save(loanDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
    }

    @Operation(
            summary = "Pay installments for a loan",
            description = "Pays installments for the given loan and amount. " +
                    "Payments must cover full installment amounts, starting with the earliest due date. " +
                    "Installments with due dates more than 3 months ahead cannot be paid."
    )
    @PostMapping("/{loanId}/payments")
    @CheckLoanAccessForLoanId
    public ResponseEntity<LoanPaymentResult> payLoan(@PathVariable Long loanId, @Valid @RequestBody LoanPaymentRequest request) {
        LoanPaymentResult result = loanService.payLoan(loanId, request.amount());
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Retrieve all loans",
            description = "Fetches all loans in the system. Restricted to ADMIN and EMPLOYEE roles."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<LoanDto>> getAllLoans() {
        return ResponseEntity.ok(loanService.findAll());
    }

    @Operation(
            summary = "Retrieve a loan by ID",
            description = "Fetches loan details for the given loan ID. Customers can only access their own loans."
    )
    @GetMapping("/{loanId}")
    @CheckLoanAccessForLoanId
    public ResponseEntity<LoanDto> getLoanById(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.findById(loanId));
    }

    @Operation(
            summary = "Retrieve installments for a loan",
            description = "Returns all installments for a given loan. Customers can only access their own installments."
    )
    @GetMapping("/{loanId}/installments")
    @CheckLoanAccessForLoanId
    public ResponseEntity<List<LoanInstallmentDto>> getLoanInstallments(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.findInstallmentsForLoan(loanId));
    }

    @Operation(
            summary = "Retrieve all loans for a customer",
            description = "Fetches loans for the given customer ID. Customers can only access their own loans."
    )
    @GetMapping("/customers/{customerId}/loans")
    @CheckLoanAccessForCustomerId
    public ResponseEntity<List<LoanDto>> getAllLoansForCustomerId(@PathVariable  Long customerId) {
        return ResponseEntity.ok(loanService.findByIdForCurrentUser(customerId));
    }
}
