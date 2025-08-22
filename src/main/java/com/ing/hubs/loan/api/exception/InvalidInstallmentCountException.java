package com.ing.hubs.loan.api.exception;


public class InvalidInstallmentCountException extends RuntimeException {

    public InvalidInstallmentCountException(String message) {
        super(message);
    }
}
