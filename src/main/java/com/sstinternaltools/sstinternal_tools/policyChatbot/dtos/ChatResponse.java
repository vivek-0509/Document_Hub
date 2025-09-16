package com.sstinternaltools.sstinternal_tools.policyChatbot.dtos;

public class ChatResponse {

    private String response;
    private String document_name;
    private String page_number;
    private String file_url;


    public ChatResponse(String response, String file_url, String document_name, String page_number) {
        this.response = response;
        this.file_url = file_url;
        this.document_name = document_name;
        this.page_number = page_number;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getPage_number() {
        return page_number;
    }

    public void setPage_number(String page_number) {
        this.page_number = page_number;
    }
}