package com.sstinternaltools.sstinternal_tools.policyChatbot.exception;

public class ChatbotServiceException extends RuntimeException {
    public ChatbotServiceException(String message) {
        super(message);
    }

    public ChatbotServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}