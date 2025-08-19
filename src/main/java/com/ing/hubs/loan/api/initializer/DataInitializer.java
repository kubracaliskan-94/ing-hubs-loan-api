package com.ing.hubs.loan.api.initializer;

import com.ing.hubs.loan.api.model.Customer;
import com.ing.hubs.loan.api.model.Employee;
import com.ing.hubs.loan.api.model.Role;
import com.ing.hubs.loan.api.model.User;
import com.ing.hubs.loan.api.model.enums.RoleType;
import com.ing.hubs.loan.api.repository.CustomerRepository;
import com.ing.hubs.loan.api.repository.EmployeeRepository;
import com.ing.hubs.loan.api.repository.RoleRepository;
import com.ing.hubs.loan.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Role adminRole = roleRepository.save(new Role(null, RoleType.ROLE_ADMIN));
            Role employeeRole = roleRepository.save(new Role(null, RoleType.ROLE_EMPLOYEE));
            Role customerRole = roleRepository.save(new Role(null, RoleType.ROLE_CUSTOMER));


            User adminUser = new User(null, "admin", "{noop}admin123", "System", "Admin", Set.of(adminRole), null, null, LocalDateTime.now(), LocalDateTime.now());
            User loanOfficerUser = new User(null, "employee", "{noop}employee123", "Employee", "Employee", Set.of(employeeRole), null, null, LocalDateTime.now(), LocalDateTime.now());
            User customer1User = new User(null, "customer1", "{noop}customer1123", "Customer1", "Customer1", Set.of(customerRole), null, null, LocalDateTime.now(), LocalDateTime.now());
            userRepository.saveAll(List.of(adminUser, loanOfficerUser, customer1User));

            Employee employee = new Employee(null, loanOfficerUser, "Loan Management", LocalDateTime.now(), LocalDateTime.now());
            employeeRepository.save(employee);

            Customer customer1 = new Customer(null, customer1User, BigDecimal.valueOf(500000), BigDecimal.valueOf(50000), LocalDateTime.now(), LocalDateTime.now());
            customerRepository.save(customer1);
        }
    }
}
