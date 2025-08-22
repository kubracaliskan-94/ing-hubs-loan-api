package com.ing.hubs.loan.api.exception;


public class InvalidInterestRateException extends RuntimeException {

    public InvalidInterestRateException(String message) {
        super(message);
    }
}
