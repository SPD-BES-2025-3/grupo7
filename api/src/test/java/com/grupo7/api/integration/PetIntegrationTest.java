package com.grupo7.api.integration;

import com.grupo7.api.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PetIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private Pet pet;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/pets";
        
        pet = new Pet();
        pet.setNome("Rex");
        pet.setEspecie("Cachorro");
        pet.setRaca("Labrador");
        pet.setIdade(5);
        pet.setSexo("M");
        pet.setClienteId("cli1");
        pet.setAtivo(true);
    }

    @Test
    void testCreateAndRetrievePet() {
        // Create pet
        ResponseEntity<Pet> createResponse = restTemplate.postForEntity(baseUrl, pet, Pet.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals("Rex", createResponse.getBody().getNome());

        String petId = createResponse.getBody().getId();

        // Retrieve pet
        ResponseEntity<Pet> getResponse = restTemplate.getForEntity(baseUrl + "/" + petId, Pet.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Rex", getResponse.getBody().getNome());
    }

    @Test
    void testGetAllPets() {
        ResponseEntity<List<Pet>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Pet>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetPetsByClienteId() {
        ResponseEntity<List<Pet>> response = restTemplate.exchange(
                baseUrl + "/cliente/cli1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Pet>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetPetsAtivos() {
        ResponseEntity<List<Pet>> response = restTemplate.exchange(
                baseUrl + "/ativos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Pet>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdatePet() {
        // Create pet first
        ResponseEntity<Pet> createResponse = restTemplate.postForEntity(baseUrl, pet, Pet.class);
        String petId = createResponse.getBody().getId();

        // Update pet
        pet.setNome("Rex Atualizado");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Pet> requestEntity = new HttpEntity<>(pet, headers);

        ResponseEntity<Pet> updateResponse = restTemplate.exchange(
                baseUrl + "/" + petId,
                HttpMethod.PUT,
                requestEntity,
                Pet.class
        );
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Rex Atualizado", updateResponse.getBody().getNome());
    }

    @Test
    void testDeletePet() {
        // Create pet first
        ResponseEntity<Pet> createResponse = restTemplate.postForEntity(baseUrl, pet, Pet.class);
        String petId = createResponse.getBody().getId();

        // Delete pet
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + petId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verify pet is deleted
        ResponseEntity<Pet> getResponse = restTemplate.getForEntity(baseUrl + "/" + petId, Pet.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testAlterarStatusPet() {
        // Create pet first
        ResponseEntity<Pet> createResponse = restTemplate.postForEntity(baseUrl, pet, Pet.class);
        String petId = createResponse.getBody().getId();

        // Alter status
        ResponseEntity<Pet> statusResponse = restTemplate.exchange(
                baseUrl + "/" + petId + "/status?ativo=false",
                HttpMethod.PUT,
                null,
                Pet.class
        );
        assertEquals(HttpStatus.OK, statusResponse.getStatusCode());
        assertFalse(statusResponse.getBody().isAtivo());
    }

    @Test
    void testCreatePetWithInvalidData() {
        Pet invalidPet = new Pet();
        invalidPet.setNome(""); // Invalid name

        ResponseEntity<Pet> response = restTemplate.postForEntity(baseUrl, invalidPet, Pet.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetPetNotFound() {
        ResponseEntity<Pet> response = restTemplate.getForEntity(baseUrl + "/999", Pet.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdatePetNotFound() {
        pet.setNome("Rex Atualizado");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Pet> requestEntity = new HttpEntity<>(pet, headers);

        ResponseEntity<Pet> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.PUT,
                requestEntity,
                Pet.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeletePetNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
} 