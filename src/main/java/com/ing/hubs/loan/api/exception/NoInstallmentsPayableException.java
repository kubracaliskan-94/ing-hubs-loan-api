package com.ing.hubs.loan.api.exception;

public class NoInstallmentsPayableException extends RuntimeException {

    public NoInstallmentsPayableException(String message) {
        super(message);
    }
}
