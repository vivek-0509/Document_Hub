package com.sstinternaltools.sstinternal_tools.policyChatbot.service.implementation;

import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.CloudStorageService;
import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocCreateDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocResponseDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.entity.ChatBotDoc;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.ResourceNotFoundException;
import com.sstinternaltools.sstinternal_tools.policyChatbot.mapper.interfaces.ChatBotDocMapper;
import com.sstinternaltools.sstinternal_tools.policyChatbot.repository.ChatBotDocRepository;
import com.sstinternaltools.sstinternal_tools.policyChatbot.service.interfaces.ChatBotDocService;
import jakarta.transaction.Transactional;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChatBotDocServiceImpl implements ChatBotDocService {

    private final VectorStore vectorStore;
    private final CloudStorageService cloudStorageService;
    private final ChatBotDocRepository chatBotDocRepository;
    private final ChatBotDocMapper chatBotDocMapper;

    public ChatBotDocServiceImpl(VectorStore vectorStore, CloudStorageService cloudStorageService, ChatBotDocRepository chatBotDocRepository, ChatBotDocMapper chatBotDocMapper) {
        this.vectorStore = vectorStore;
        this.cloudStorageService = cloudStorageService;
        this.chatBotDocRepository = chatBotDocRepository;
        this.chatBotDocMapper = chatBotDocMapper;
    }

    @Transactional
    public ChatBotDoc saveDocumentToCloudStorage(ChatBotDocCreateDto createDto){
        String fileUrl=cloudStorageService.uploadFile(createDto.getFile());
        ChatBotDoc chatBotDoc=chatBotDocMapper.fromCreateDto(createDto,fileUrl);
        chatBotDocRepository.save(chatBotDoc);
        return chatBotDoc;
    }

    @Transactional
    @Override
    public void injectDocument(ChatBotDocCreateDto createDto) {

        try {
            ChatBotDoc chatBotDoc=saveDocumentToCloudStorage(createDto);
            System.out.println(chatBotDoc.getFileUrl());
            Resource resource = new InputStreamResource(createDto.getFile().getInputStream());
            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);

            var documents = pdfReader.get();
            if (documents.isEmpty()) {
                throw new ResourceNotFoundException("No text extracted from file");
            }

            TokenTextSplitter textSplitter = new TokenTextSplitter(150, 30, 20, 500, false);
            var chunks = textSplitter.apply(documents);

            System.out.println("Number of chunks created: " + chunks.size());
            if (chunks.isEmpty()) {
                System.err.println("No chunks created from the document content");
                throw new ResourceNotFoundException("No chunks created from the document content");
            }

            for (int i = 0; i < chunks.size(); i++) {
                var doc = chunks.get(i);
                doc.getMetadata().put("docId", chatBotDoc.getId().toString());
                doc.getMetadata().put("documentName", chatBotDoc.getDocumentName());
                doc.getMetadata().put("fileUrl", chatBotDoc.getFileUrl());

            }

            vectorStore.accept(chunks);
            System.out.println("VectorStore loaded with PDF content.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error during document ingestion: " + e.getMessage());
            throw new RuntimeException("Failed to inject document: likely due to invalid file content or encoding.");
        }
    }

    @Transactional
    @Override
    public void deleteDocumentAndEmbeddings(Long docId) {
        ChatBotDoc doc=chatBotDocRepository.findById(docId).orElseThrow(()->new ResourceNotFoundException("Document not found"));
        String filterExpression = "docId == '" + docId + "'";
        vectorStore.delete(filterExpression);
        cloudStorageService.deleteFile(doc.getFileUrl());
        chatBotDocRepository.deleteById(docId);
    }

    @Override
    public List<ChatBotDocResponseDto> getAllDocuments() {
        return chatBotDocRepository.findAll().stream().map(chatBotDocMapper::fromEntityToDto).collect(Collectors.toList());
    }

}
