package com.ing.hubs.loan.api.controller;

import com.ing.hubs.loan.api.dto.LoanDto;
import com.ing.hubs.loan.api.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.security.CheckLoanAccess;
import com.ing.hubs.loan.api.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "Loan", description = "Manage loans")
public class LoanController {

    private final LoanService loanService;

    @Operation(summary = "Create a new loan")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<LoanDto> createLoan(@Valid @RequestBody LoanDto loanDto) {
        return ResponseEntity.ok(loanService.save(loanDto));
    }

    @Operation(summary = "Get all loans")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<LoanDto>> retrieveAllLoans() {
        return ResponseEntity.ok(loanService.findAll());
    }

    @Operation(summary = "Get loan by ID")
    @GetMapping("/{loanId}")
    @CheckLoanAccess
    public ResponseEntity<LoanDto> retrieveLoanById(@PathVariable Long loanId) throws AccessDeniedException {
        return ResponseEntity.ok(loanService.findByIdForCurrentUser(loanId));
    }

    @GetMapping("/{loanId}/installments")
    @CheckLoanAccess
    public ResponseEntity<List<LoanInstallmentDto>> retrieveInstallments(@PathVariable Long loanId) throws AccessDeniedException {
        List<LoanInstallmentDto> installments = loanService.findInstallmentsForLoan(loanId);
        return ResponseEntity.ok(installments);
    }


    @Operation(summary = "Delete loan by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long loanId) {
        loanService.delete(loanId);
        return ResponseEntity.noContent().build();
    }
}

