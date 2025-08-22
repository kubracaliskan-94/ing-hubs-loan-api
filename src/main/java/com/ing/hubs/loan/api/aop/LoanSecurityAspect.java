package com.ing.hubs.loan.api.aop;

import com.ing.hubs.loan.api.model.entity.User;
import com.ing.hubs.loan.api.model.enums.RoleType;
import com.ing.hubs.loan.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoanSecurityAspect {

    private final UserService userService;

    @Before("@annotation(com.ing.hubs.loan.api.security.CheckLoanAccessForLoanId) && args(loanId,..)")
    public void checkLoanAccess(Long loanId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrEmployee = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleType.ROLE_ADMIN.name()) || a.getAuthority().equals((RoleType.ROLE_EMPLOYEE.name())));
        if (isAdminOrEmployee) return;

        boolean isCustomer = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleType.ROLE_CUSTOMER.name()));
        if (isCustomer) {
            User user = userService.findByUsername(auth.getName());
            if (!loanId.equals(user.getCustomer().getId())) {
                throw new AccessDeniedException("You can only access your own loans");
            }
        }
    }

    @Before("@annotation(com.ing.hubs.loan.api.security.CheckLoanAccessForCustomerId) && args(customerId,..)")
    public void checkLoanAccessForCustomer(Long customerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrEmployee = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleType.ROLE_ADMIN.name())
                        || a.getAuthority().equals(RoleType.ROLE_EMPLOYEE.name()));
        if (isAdminOrEmployee) return;

        boolean isCustomer = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleType.ROLE_CUSTOMER.name()));

        if (isCustomer) {
            User user = userService.findByUsername(auth.getName());
            Long loggedInCustomerId = user.getCustomer().getId();

            if (!customerId.equals(loggedInCustomerId)) {
                throw new AccessDeniedException("You can only access your own loans");
            }
        }
    }
}
