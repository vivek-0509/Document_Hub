package com.sstinternaltools.sstinternal_tools.policyChatbot.exception;

public class LLMServiceException extends RuntimeException {
    public LLMServiceException(String message) {
        super(message);
    }

    public LLMServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}