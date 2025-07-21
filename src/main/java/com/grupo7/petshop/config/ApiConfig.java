package com.grupo7.petshop.config;

/**
 * Configurações da API REST
 */
public class ApiConfig {
    
    // URL base da API
    public static final String BASE_URL = "http://localhost:8080/api";
    
    // Endpoints dos recursos
    public static final String CLIENTES_URL = BASE_URL + "/clientes";
    public static final String PETS_URL = BASE_URL + "/pets";
    public static final String PRODUTOS_URL = BASE_URL + "/produtos";
    public static final String AGENDAMENTOS_URL = BASE_URL + "/agendamentos";
    public static final String VENDAS_URL = BASE_URL + "/vendas";
    public static final String USUARIOS_URL = BASE_URL + "/usuarios";
    
    // Headers padrão
    public static final String CONTENT_TYPE = "application/json";
    public static final String ACCEPT = "application/json";
    
    // Timeouts
    public static final int CONNECT_TIMEOUT = 30; // segundos
    public static final int READ_TIMEOUT = 30; // segundos
    
    // Configurações de paginação
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String DEFAULT_SORT = "id";
    
    /**
     * Retorna a URL completa para um endpoint específico
     */
    public static String getUrl(String endpoint) {
        return BASE_URL + endpoint;
    }
    
    /**
     * Retorna a URL com parâmetros de paginação
     */
    public static String getUrlWithPagination(String endpoint, int page, int size) {
        return getUrl(endpoint) + "?page=" + page + "&size=" + size;
    }
    
    /**
     * Retorna a URL com parâmetros de busca
     */
    public static String getUrlWithSearch(String endpoint, String searchTerm) {
        return getUrl(endpoint) + "?search=" + searchTerm;
    }
} 