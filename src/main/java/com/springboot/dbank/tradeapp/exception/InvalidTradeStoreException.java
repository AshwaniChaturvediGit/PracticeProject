package com.springboot.dbank.tradeapp.exception;

public class InvalidTradeStoreException extends RuntimeException {

    private final String id;

    public InvalidTradeStoreException(final String id) {
        super("Invalid Trade: " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
