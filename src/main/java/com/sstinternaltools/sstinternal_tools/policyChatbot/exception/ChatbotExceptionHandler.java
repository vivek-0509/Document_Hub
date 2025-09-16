package com.sstinternaltools.sstinternal_tools.policyChatbot.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChatbotExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotExceptionHandler.class);

    @ExceptionHandler(ChatbotServiceException.class)
    public ResponseEntity<Map<String, Object>> handleChatbotServiceException(ChatbotServiceException ex) {
        logger.error("Chatbot service error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Chatbot Service Error", ex.getMessage());
    }

    @ExceptionHandler(VectorStoreException.class)
    public ResponseEntity<Map<String, Object>> handleVectorStoreException(VectorStoreException ex) {
        logger.error("Vector store error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "Knowledge Base Error", "Unable to search knowledge base. Please try again later.");
    }

    @ExceptionHandler(LLMServiceException.class)
    public ResponseEntity<Map<String, Object>> handleLLMServiceException(LLMServiceException ex) {
        logger.error("LLM service error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "AI Service Error", "AI service is temporarily unavailable. Please try again later.");
    }

    @ExceptionHandler(InvalidConversationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidConversationException(InvalidConversationException ex) {
        logger.warn("Invalid conversation: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Conversation", ex.getMessage());
    }

//    @ExceptionHandler(RateLimitExceededException.class)
//    public ResponseEntity<Map<String, Object>> handleRateLimitExceededException(RateLimitExceededException ex) {
//        logger.warn("Rate limit exceeded: {}", ex.getMessage());
//        return buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, "Rate Limit Exceeded", "Too many requests. Please wait before trying again.");
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Unexpected error in chatbot: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred. Please try again later.");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found.", ex.getMessage());
    }


    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", error,
                "message", message
        );
        return ResponseEntity.status(status).body(response);
    }
}