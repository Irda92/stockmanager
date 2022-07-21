package com.bench.stockmanagement;

public class UnexpectedQueryResultException extends RuntimeException{
    public UnexpectedQueryResultException(String message) {
        super(message);
    }
}
