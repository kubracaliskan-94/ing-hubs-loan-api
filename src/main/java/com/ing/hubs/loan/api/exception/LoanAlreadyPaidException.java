package com.ing.hubs.loan.api.exception;

public class LoanAlreadyPaidException extends RuntimeException {

    public LoanAlreadyPaidException(String message) {
        super(message);
    }
}
