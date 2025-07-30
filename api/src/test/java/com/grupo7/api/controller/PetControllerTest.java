package com.grupo7.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grupo7.api.model.Pet;
import com.grupo7.api.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        pet1 = new Pet();
        pet1.setId("1");
        pet1.setNome("Rex");
        pet1.setEspecie("Cachorro");
        pet1.setRaca("Labrador");
        pet1.setDataNascimento(java.time.LocalDate.now().minusYears(5));
        pet1.setSexo("M");
        pet1.setClienteId("cli1");
        pet1.setAtivo(true);

        pet2 = new Pet();
        pet2.setId("2");
        pet2.setNome("Mimi");
        pet2.setEspecie("Gato");
        pet2.setRaca("Siamês");
        pet2.setDataNascimento(java.time.LocalDate.now().minusYears(3));
        pet2.setSexo("F");
        pet2.setClienteId("cli2");
        pet2.setAtivo(false);
    }

    @Test
    void testGetAllPets() {
        List<Pet> pets = Arrays.asList(pet1, pet2);
        when(petService.findAll()).thenReturn(pets);

        ResponseEntity<List<Pet>> response = petController.getAllPets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(petService, times(1)).findAll();
    }

    @Test
    void testGetPetById_Success() {
        when(petService.findById("1")).thenReturn(Optional.of(pet1));

        ResponseEntity<Pet> response = petController.getPetById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rex", response.getBody().getNome());
        verify(petService, times(1)).findById("1");
    }

    @Test
    void testGetPetById_NotFound() {
        when(petService.findById("999")).thenReturn(Optional.empty());

        ResponseEntity<Pet> response = petController.getPetById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(petService, times(1)).findById("999");
    }

    @Test
    void testGetPetsByClienteId() {
        List<Pet> pets = Arrays.asList(pet1);
        when(petService.findPetsPorCliente("cli1")).thenReturn(pets);

        ResponseEntity<List<Pet>> response = petController.getPetsByCliente("cli1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("cli1", response.getBody().get(0).getClienteId());
        verify(petService, times(1)).findPetsPorCliente("cli1");
    }

    @Test
    void testGetPetsAtivos() {
        List<Pet> petsAtivos = Arrays.asList(pet1);
        when(petService.findPetsAtivos()).thenReturn(petsAtivos);

        ResponseEntity<List<Pet>> response = petController.getPetsAtivos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).isAtivo());
        verify(petService, times(1)).findPetsAtivos();
    }

    @Test
    void testCreatePet_Success() throws Exception {
        when(petService.save(any(Pet.class))).thenReturn(pet1);

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pet1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Rex"));

        verify(petService, times(1)).save(any(Pet.class));
    }

    @Test
    void testCreatePet_ValidationError() throws Exception {
        Pet petInvalido = new Pet();
        petInvalido.setNome("");

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petInvalido)))
                .andExpect(status().isBadRequest());

        verify(petService, never()).save(any(Pet.class));
    }

    @Test
    void testUpdatePet_Success() throws Exception {
        when(petService.update(eq("1"), any(Pet.class))).thenReturn(pet1);

        mockMvc.perform(put("/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pet1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Rex"));

        verify(petService, times(1)).update(eq("1"), any(Pet.class));
    }

    @Test
    void testUpdatePet_NotFound() throws Exception {
        when(petService.update(eq("999"), any(Pet.class))).thenThrow(new RuntimeException("Pet não encontrado"));

        mockMvc.perform(put("/pets/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pet1)))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).update(eq("999"), any(Pet.class));
    }

    @Test
    void testDeletePet() {
        when(petService.existsById("1")).thenReturn(true);
        doNothing().when(petService).deleteById("1");

        ResponseEntity<Void> response = petController.deletePet("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(petService, times(1)).existsById("1");
        verify(petService, times(1)).deleteById("1");
    }

    @Test
    void testDeletePet_NotFound() {
        when(petService.existsById("999")).thenReturn(false);

        ResponseEntity<Void> response = petController.deletePet("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(petService, times(1)).existsById("999");
        verify(petService, never()).deleteById(anyString());
    }

    @Test
    void testAlterarStatusPet_Success() {
        Pet pet = new Pet();
        pet.setId("1");
        pet.setAtivo(true);

        Pet petAtualizado = new Pet();
        petAtualizado.setId("1");
        petAtualizado.setAtivo(false);

        when(petService.findById("1")).thenReturn(Optional.of(pet));
        when(petService.save(any(Pet.class))).thenReturn(petAtualizado);

        ResponseEntity<Pet> response = petController.alterarStatusPet("1", false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isAtivo());
        verify(petService, times(1)).findById("1");
        verify(petService, times(1)).save(any(Pet.class));
    }

    @Test
    void testAlterarStatusPet_NotFound() {
        when(petService.findById("999")).thenReturn(Optional.empty());

        ResponseEntity<Pet> response = petController.alterarStatusPet("999", false);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(petService, times(1)).findById("999");
        verify(petService, never()).save(any(Pet.class));
    }
} 