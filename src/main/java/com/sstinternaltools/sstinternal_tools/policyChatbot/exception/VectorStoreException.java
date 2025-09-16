package com.sstinternaltools.sstinternal_tools.policyChatbot.exception;

public class VectorStoreException extends RuntimeException {
    public VectorStoreException(String message) {
        super(message);
    }

    public VectorStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}