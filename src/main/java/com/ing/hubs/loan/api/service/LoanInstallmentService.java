package com.ing.hubs.loan.api.service;

import com.ing.hubs.loan.api.dto.LoanInstallmentDto;
import com.ing.hubs.loan.api.exception.ResourceNotFoundException;
import com.ing.hubs.loan.api.mapper.LoanInstallmentMapper;
import com.ing.hubs.loan.api.model.Loan;
import com.ing.hubs.loan.api.model.LoanInstallment;
import com.ing.hubs.loan.api.repository.LoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanInstallmentService {
    private final LoanInstallmentRepository installmentRepository;
    private final LoanService loanService;

    public LoanInstallmentDto save(LoanInstallmentDto installmentDto) {
        Loan loan = loanService.findById(installmentDto.loanId());
        LoanInstallment loanInstallment= LoanInstallmentMapper.toEntity(installmentDto,loan);

        LoanInstallment savedLoanInstallment = installmentRepository.save(loanInstallment);
        return LoanInstallmentMapper.toDto(savedLoanInstallment);
    }

    public List<LoanInstallmentDto> findAll() {
        return installmentRepository.findAll()
                .stream()
                .map(LoanInstallmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public LoanInstallmentDto findById(Long id) {
        LoanInstallment loanInstallment =  installmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoanInstallment not found with id " + id));
        return LoanInstallmentMapper.toDto(loanInstallment);
    }

    public void delete(Long id) {
        installmentRepository.deleteById(id);
    }

    public List<LoanInstallmentDto> findByLoan(Loan loan) {
        return installmentRepository.findByLoan(loan)
                .stream()
                .map(LoanInstallmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
