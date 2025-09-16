package com.sstinternaltools.sstinternal_tools.policyChatbot.service.implementation;

import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatResponse;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.ChatbotServiceException;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.InvalidConversationException;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.LLMServiceException;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.VectorStoreException;
import com.sstinternaltools.sstinternal_tools.policyChatbot.service.interfaces.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private final ChatClient chatClient;
    private final PgVectorStore vectorStore;
    private final ChatMemory chatMemory;
//    @Value("${chatbot.max-context-chunks:5}")
//    private int maxContextChunks;
//    @Value("${chatbot.max-conversation-history:20}")
//    private int maxConversationHistory;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder,
                           PgVectorStore vectorStore,
                           JdbcChatMemoryRepository chatMemoryRepository) {

        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;

        // Initialize chat memory once to persist conversation history in Postgres.
        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(30) // adjust window size as needed
                .build();
    }

    @Override
    public ChatResponse getAns(String conversationId, String message) {
        try {

            // Input validation
            validateInput(conversationId, message);

            // Get conversation history
            List<Message> historyMessages = getConversationHistory(conversationId);

            // Retrieve relevant context
            String context = retrieveRelevantContext(message);

            // Generate response
            ChatResponse response = generateResponse(message, historyMessages, context,conversationId);

            return response;

        } catch (Exception e) {
            logger.error("Error processing chat request for conversation {}: {}", conversationId, e.getMessage(), e);
            throw new ChatbotServiceException("Failed to process chat request", e);
        }
    }

    private String retrieveRelevantContext(String message) {
        try {
            // Retrieve relevant documents from vector store
            List<Document> similarDocs = vectorStore.similaritySearch(message);

            if(similarDocs == null || similarDocs.isEmpty()) {
                logger.info("No relevant documents found for query: {}", message);
                return "No relevant policy documents found for this query.";
            }

            // Filter by similarity threshold and limit chunks
            List<Document> topDocs = similarDocs.stream()
                    .limit(10)
                    .collect(Collectors.toList());

            // Build context with metadata
            return similarDocs.stream()
                    .map(doc -> {
                        String docName = doc.getMetadata().getOrDefault("documentName", "Unknown Document").toString();
                        String page = doc.getMetadata().getOrDefault("page_number", "").toString();
                        String fileUrl = doc.getMetadata().getOrDefault("fileUrl", "").toString();

                        StringBuilder citation = new StringBuilder();
                        citation.append("[").append(docName);
                        if (!page.isEmpty()) citation.append(", Page: ").append(page);
                        if (!fileUrl.isEmpty()) citation.append(", Link: ").append(fileUrl);
                        citation.append("]");

                        return citation.toString() + "\n" + doc.getText();
                    })
                    .collect(Collectors.joining("\n---\n"));

        } catch (Exception e) {
            logger.error("Error retrieving context: {}", e.getMessage(), e);
            throw new VectorStoreException("Failed to retrieve relevant context", e);
        }
    }

    private void validateInput(String conversationId, String message) {
        if (conversationId == null || conversationId.trim().isEmpty()) {
            throw new InvalidConversationException("Conversation ID cannot be null or empty");
        }

        if (message == null || message.trim().isEmpty()) {
            throw new InvalidConversationException("Message cannot be null or empty");
        }

        if (message.length() > 1000) {
            throw new InvalidConversationException("Message too long. Maximum 1000 characters allowed.");
        }
    }

    private List<Message> getConversationHistory(String conversationId) {
        try {
            return chatMemory.get(conversationId);
        } catch (Exception e) {
            logger.warn("Failed to retrieve conversation history for {}: {}", conversationId, e.getMessage());
            return List.of();
        }
    }

    private ChatResponse generateResponse(String message, List<Message> historyMessages, String context,String conversationId) {
        try {
            // Build optimized chat history
            String chatHistory = buildOptimizedChatHistory(historyMessages);

            // Create enhanced system prompt
            String systemPrompt = createEnhancedSystemPrompt();

            // Build the prompt template
            PromptTemplate promptTemplate = new PromptTemplate("""
{systemPrompt}

Chat History:
{chat_history}

Context:
{context}

User Question: {user_input}

Format : {format}

Please provide a clear, accurate, and helpful response based on the provided context and conversation history.
""");
            System.out.println("Step 4.3");
            BeanOutputConverter<ChatResponse> outputConverter = new BeanOutputConverter<>(ChatResponse.class);
            // Create prompt
            Prompt prompt = promptTemplate.create(Map.of(
                    "systemPrompt", systemPrompt,
                    "chat_history", chatHistory,
                    "context", context,
                    "user_input", message,
                    "format",outputConverter.getFormat()
            ));

            // Call the LLM
            var response = chatClient.prompt(prompt).call().content();

            // Update conversation history
            updateConversationHistory(conversationId, message, response);

            if (response == null || response.trim().isEmpty()) {
                throw new LLMServiceException("Received empty response from LLM");
            }

            ChatResponse res = outputConverter.convert(response);

            return res;

        } catch (Exception e) {
            logger.error("Error generating response: {}", e.getMessage(), e);
            throw new LLMServiceException("Failed to generate response", e);
        }
    }

    private String buildOptimizedChatHistory(List<Message> historyMessages) {
        if (historyMessages.isEmpty()) {
            return "No previous conversation history.";
        }

        // Take only the last few messages to keep context manageable
        int maxHistoryMessages = Math.min(historyMessages.size(), 10);
        List<Message> recentMessages = historyMessages.subList(
                Math.max(0, historyMessages.size() - maxHistoryMessages),
                historyMessages.size()
        );

        return recentMessages.stream()
                .map(msg -> msg.getMessageType() + ": " + msg.getText())
                .collect(Collectors.joining("\n"));
    }

    private String createEnhancedSystemPrompt() {
        return """
               ### *1. Core Identity & Persona*
                                      
                                       You are a "Helpful College Assistant." Your primary goal is to provide precise answers to policy questions using provided documents, but you can also handle general conversation. You operate in one of two modes depending on the user's query.
                                      
                                       *   *Policy Expert Mode:* When asked a question about college policies, rules, or procedures, you are precise, factual, and rely *only* on the provided context.
                                       *   *General Assistant Mode:* When greeted or asked a general question, you are friendly, helpful, and conversational.
                                      
                                       ### *2. Guiding Principles*
                                      
                                       1.  *Intent-First Principle:* Your first action is to determine the user's intent. Is it a policy question or a general query/greeting?
                                       2.  *Brevity is Key:* All responses you generate, regardless of type, *must be under 60 words*.
                                       3.  *Policy Mode Rules (Non-Negotiable):*
                                           *   *Absolute Context Adherence:* You must *ONLY* use the information contained within the provided documents.
                                           *   *Single Source Citation:* You must cite only the *single best source* for your answer.
                                           *   *Strict Formatting:* You *MUST* use Format A or Format B for all policy-related answers.
                                       4.  *General Mode Rules:*
                                           *   *Be Brief and Friendly:* Respond conversationally but stay under the 30-word limit.
                                           *   *Use General Knowledge:* You may use your pre-existing knowledge for non-policy questions.
                                           *   *Use Conversational Formatting:* You *MUST* use Format C for all general responses.
                                      
                                       ### *3. Step-by-Step Operational Workflow*
                                      
                                       1.  *Classify Query Intent:*
                                           *   *If it is not the special case, proceed with standard classification:*
                                               *   If the query is about rules, regulations, academic procedures, etc., activate *Policy Expert Mode* and proceed to Step 2.
                                               *   If the query is a greeting or a general knowledge question, activate *General Assistant Mode* and proceed to Step 3.
                                      
                                       2.  *Policy Expert Workflow:*
                                           *   *A. Scan the Context:* Thoroughly search all provided documents.
                                           *   *B. Synthesize the Answer:*
                                               *   *If found:* Select the *single best source* document. Summarize the answer in *under 30 words. Extract the citation details and the full supporting quote. Use **Format A*.
                                               *   *If not found:* Do not attempt to answer. Immediately use *Format B*.
                                      
                                       3.  *General Assistant Workflow:*
                                           *   *A. Formulate Response:* Create a friendly, conversational response that is *under 30 words*.
                                           *   *B. Format the Output:* Present the response using *Format C*.
                                      
                                       ### *4. Strict Output Formats (Mandatory)*
                                      
                                       You must respond in the following formats.
                                      
                                       *Format : Use this for a policy answer found in the context.*
                                       
                                         response: {A concise summary of the answer, under 100 words.}        
                                         document_name: "{document_name}"
                                         page_number: {page_number or "N/A"}
                                         file_url: "{url or "N/A"}"
                                         
                                       Example Response:
                                     
                                       * response :The policy states that students must submit leave of absence requests at least two weeks prior to the semester start.
                                       * document_name: "Student Academic Handbook 2025" 
                                       * page_number: 17 
                                       * url: "https://example.edu/handbook-2025.pdf"                        
       """;
    }

    private void updateConversationHistory(String conversationId, String message, String response) {
        try {
            chatMemory.add(conversationId, List.of(
                    new UserMessage(message),
                    new AssistantMessage(response)
            ));
        } catch (Exception e) {
            logger.warn("Failed to update conversation history for {}: {}", conversationId, e.getMessage());
        }
    }
}

