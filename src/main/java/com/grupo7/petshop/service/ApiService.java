package com.grupo7.petshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grupo7.petshop.config.ApiConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Serviço para comunicação com a API REST
 */
public class ApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    
    public ApiService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
        
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    /**
     * Realiza uma requisição GET
     */
    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", ApiConfig.ACCEPT)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição: " + response.code());
            }
            return response.body().string();
        }
    }
    
    /**
     * Realiza uma requisição POST
     */
    public String post(String url, String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, MediaType.get(ApiConfig.CONTENT_TYPE));
        
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", ApiConfig.CONTENT_TYPE)
                .addHeader("Accept", ApiConfig.ACCEPT)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição: " + response.code());
            }
            return response.body().string();
        }
    }
    
    /**
     * Realiza uma requisição PUT
     */
    public String put(String url, String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, MediaType.get(ApiConfig.CONTENT_TYPE));
        
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", ApiConfig.CONTENT_TYPE)
                .addHeader("Accept", ApiConfig.ACCEPT)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição: " + response.code());
            }
            return response.body().string();
        }
    }
    
    /**
     * Realiza uma requisição DELETE
     */
    public boolean delete(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Accept", ApiConfig.ACCEPT)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }
    
    /**
     * Converte objeto para JSON
     */
    public String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }
    
    /**
     * Converte JSON para objeto
     */
    public <T> T fromJson(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }
    
    /**
     * Verifica se a API está disponível
     */
    public boolean isApiAvailable() {
        try {
            get(ApiConfig.BASE_URL + "/health");
            return true;
        } catch (IOException e) {
            logger.error("API não está disponível: {}", e.getMessage());
            return false;
        }
    }
} 