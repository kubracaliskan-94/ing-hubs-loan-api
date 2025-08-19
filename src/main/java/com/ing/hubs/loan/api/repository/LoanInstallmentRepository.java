package com.ing.hubs.loan.api.repository;

import com.ing.hubs.loan.api.model.Loan;
import com.ing.hubs.loan.api.model.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    List<LoanInstallment> findByLoan(Loan loan);
}

