package com.ing.hubs.loan.api.aop;

import com.ing.hubs.loan.api.model.Loan;
import com.ing.hubs.loan.api.model.User;
import com.ing.hubs.loan.api.model.enums.RoleType;
import com.ing.hubs.loan.api.service.LoanService;
import com.ing.hubs.loan.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoanSecurityAspect {

    private final UserService userService;
    private final LoanService loanService;

    @Before("@annotation(com.ing.hubs.loan.api.security.CheckLoanAccess) && args(loanId,..)")
    public void checkLoanAccess(Long loanId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isCustomer = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleType.ROLE_CUSTOMER.name()));

        boolean isAdminOrEmployee = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleType.ROLE_ADMIN.name()) || a.getAuthority().equals((RoleType.ROLE_EMPLOYEE.name())));

        if (isAdminOrEmployee) return;

        if (isCustomer) {
            User user = userService.findByUsername(auth.getName());
            Loan loan = loanService.findById(loanId);
            if (!loanId.equals(user.getCustomer().getId())) {
                throw new AccessDeniedException("You can only access your own loans");
            }
        }
    }
}
