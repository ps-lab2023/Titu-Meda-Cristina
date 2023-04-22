package com.nagarro.af.bookingtablesystem.exception;

public class NotEnoughCapacityAndTablesException extends RuntimeException {

    public NotEnoughCapacityAndTablesException() {
    }

    public NotEnoughCapacityAndTablesException(String message) {
        super(message);
    }
}
