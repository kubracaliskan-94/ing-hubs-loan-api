package com.ing.hubs.loan.api.mapper;

import com.ing.hubs.loan.api.model.dto.LoanDto;
import com.ing.hubs.loan.api.model.entity.Loan;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanMapper {
    public static LoanDto toDto(Loan entity) {
        if (entity == null) return null;

        return new LoanDto(
                entity.getId(),
                entity.getCustomer().getId(),
                entity.getLoanAmount(),
                entity.getNumberOfInstallments(),
                entity.getInterestRate(),
                entity.getIsPaid(),
                entity.getCreateDate(),
                entity.getUpdateDate()
        );
    }

    public static Loan toEntity(LoanDto dto) {
        if (dto == null) return null;

        Loan loan = new Loan();
        loan.setLoanAmount(dto.loanAmount());
        loan.setInterestRate(dto.interestRate());
        loan.setNumberOfInstallments(dto.numberOfInstallments());
        return loan;
    }
}
