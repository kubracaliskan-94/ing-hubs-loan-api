package com.ing.hubs.loan.api.mapper;

import com.ing.hubs.loan.api.model.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.model.entity.Loan;
import com.ing.hubs.loan.api.model.entity.LoanInstallment;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class LoanInstallmentMapper {

    public static LoanInstallmentDto toDto(LoanInstallment entity) {
        if (entity == null) return null;
        return new LoanInstallmentDto(
                entity.getId(),
                entity.getLoan().getId(),
                entity.getAmount(),
                entity.getPaidAmount(),
                entity.getDueDate(),
                entity.getPaymentDate(),
                entity.getIsPaid(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static LoanInstallment toEntity(LoanInstallmentDto dto, Loan loan) {
        if (dto == null) return null;
        LoanInstallment loaninstallment = new LoanInstallment();
        loaninstallment.setId(dto.id());
        loaninstallment.setLoan(loan);
        loaninstallment.setAmount(dto.amount());
        loaninstallment.setPaidAmount(dto.paidAmount() != null ? dto.paidAmount() : BigDecimal.ZERO);
        loaninstallment.setDueDate(dto.dueDate());
        loaninstallment.setPaymentDate(dto.paymentDate());
        loaninstallment.setIsPaid(dto.isPaid() != null ? dto.isPaid() : false);
        return loaninstallment;
    }
}

