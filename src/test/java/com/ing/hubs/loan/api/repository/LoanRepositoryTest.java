package com.ing.hubs.loan.api.repository;

import com.ing.hubs.loan.api.model.entity.Customer;
import com.ing.hubs.loan.api.model.entity.Loan;
import com.ing.hubs.loan.api.model.entity.Role;
import com.ing.hubs.loan.api.model.entity.User;
import com.ing.hubs.loan.api.model.enums.RoleType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Disabled
    void shouldFindLoansByCustomerId() {
        Customer customer = persistCustomerWithRole(RoleType.ROLE_CUSTOMER);

        Loan loan = createLoan(customer, BigDecimal.valueOf(1000));
        entityManager.persist(loan);

        List<Loan> loans = loanRepository.findByCustomerId(customer.getId());

        assertEquals(1, loans.size());
        assertEquals(BigDecimal.valueOf(1000), loans.get(0).getLoanAmount());
    }

    private Customer persistCustomerWithRole(RoleType roleType) {
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
        entityManager.persist(user);

        Customer customer = new Customer(
                null,
                user,
                BigDecimal.valueOf(500_000),
                BigDecimal.ZERO,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        entityManager.persist(customer);
        return customer;
    }

    private Loan createLoan(Customer customer, BigDecimal amount) {
        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(amount);
        loan.setNumberOfInstallments(6);
        loan.setIsPaid(false);
        loan.setInterestRate(BigDecimal.valueOf(0.1));
        return loan;
    }
}

