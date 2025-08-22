package com.ing.hubs.loan.api.exception;


public class CustomerLimitExceededException extends RuntimeException {

    public CustomerLimitExceededException(String message) {
        super(message);
    }
}
