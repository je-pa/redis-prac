package com.redisprac.common.exception;

import lombok.Getter;

@Getter
public class Exception extends RuntimeException {
    
    private final ExceptionInterface exceptionInterface;

    public Exception(ExceptionInterface i) {
        super(i.getMessage());
        this.exceptionInterface = i;
    }
}
