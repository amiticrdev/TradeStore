package com.tradestore.app.exception;

import org.springframework.validation.FieldError;

public class TradeRejectedException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;
	private FieldError fieldError;

    public TradeRejectedException(String message, FieldError fieldError) {
        super(message);
        this.fieldError = fieldError;
    }
    
    public TradeRejectedException(String message) {
        super(message);
    }

    public FieldError getFieldError() { return fieldError; }
}
