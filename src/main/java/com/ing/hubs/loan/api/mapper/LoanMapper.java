package com.ing.hubs.loan.api.mapper;

import com.ing.hubs.loan.api.dto.LoanDto;
import com.ing.hubs.loan.api.model.Loan;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanMapper {
    public static LoanDto toDto(Loan entity) {
        if (entity == null) return null;

        return new LoanDto(
                entity.getId(),
                null,
                null,
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
        loan.setId(dto.id());
        loan.setLoanAmount(dto.loanAmount());
        loan.setNumberOfInstallments(dto.numberOfInstallments());
        loan.setCreateDate(dto.createDate());
        loan.setIsPaid(dto.isPaid());
        return loan;
    }
}
