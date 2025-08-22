package com.ing.hubs.loan.api.repository;


import com.ing.hubs.loan.api.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l LEFT JOIN FETCH l.installments WHERE l.id = :id")
    Optional<Loan> findByIdWithInstallments(@Param("id") Long id);

    List<Loan> findByCustomerId(Long customerId);


}