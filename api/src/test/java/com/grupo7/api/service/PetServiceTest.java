package com.grupo7.api.service;

import com.grupo7.api.model.Pet;
import com.grupo7.api.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet1 = new Pet();
        pet1.setId("1");
        pet1.setNome("Rex");
        pet1.setEspecie("Cachorro");
        pet1.setRaca("Labrador");
        pet1.setIdade(5);
        pet1.setSexo("M");
        pet1.setClienteId("cli1");
        pet1.setAtivo(true);

        pet2 = new Pet();
        pet2.setId("2");
        pet2.setNome("Mimi");
        pet2.setEspecie("Gato");
        pet2.setRaca("Siamês");
        pet2.setIdade(3);
        pet2.setSexo("F");
        pet2.setClienteId("cli2");
        pet2.setAtivo(false);
    }

    @Test
    void testFindAll() {
        List<Pet> pets = Arrays.asList(pet1, pet2);
        when(petRepository.findAll()).thenReturn(pets);
        List<Pet> result = petService.findAll();
        assertEquals(2, result.size());
        assertEquals("Rex", result.get(0).getNome());
        assertEquals("Mimi", result.get(1).getNome());
        verify(petRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(petRepository.findById("1")).thenReturn(Optional.of(pet1));
        Optional<Pet> result = petService.findById("1");
        assertTrue(result.isPresent());
        assertEquals("Rex", result.get().getNome());
        verify(petRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(petRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Pet> result = petService.findById("999");
        assertFalse(result.isPresent());
        verify(petRepository, times(1)).findById("999");
    }

    @Test
    void testFindByClienteId() {
        List<Pet> pets = Arrays.asList(pet1);
        when(petRepository.findByClienteId("cli1")).thenReturn(pets);
        List<Pet> result = petService.findByClienteId("cli1");
        assertEquals(1, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        verify(petRepository, times(1)).findByClienteId("cli1");
    }

    @Test
    void testFindByAtivo() {
        List<Pet> petsAtivos = Arrays.asList(pet1);
        when(petRepository.findByAtivo(true)).thenReturn(petsAtivos);
        List<Pet> result = petService.findByAtivo(true);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAtivo());
        verify(petRepository, times(1)).findByAtivo(true);
    }

    @Test
    void testSave() {
        when(petRepository.save(any(Pet.class))).thenReturn(pet1);
        Pet saved = petService.save(pet1);
        assertNotNull(saved);
        assertEquals("Rex", saved.getNome());
        verify(petRepository, times(1)).save(pet1);
    }

    @Test
    void testUpdate_Success() {
        when(petRepository.findById("1")).thenReturn(Optional.of(pet1));
        when(petRepository.save(any(Pet.class))).thenReturn(pet1);
        Pet updated = petService.update("1", pet1);
        assertNotNull(updated);
        assertEquals("Rex", updated.getNome());
        verify(petRepository, times(1)).findById("1");
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(petRepository.findById("999")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> petService.update("999", pet1));
        verify(petRepository, times(1)).findById("999");
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(petRepository).deleteById("1");
        petService.deleteById("1");
        verify(petRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(petRepository.existsById("1")).thenReturn(true);
        when(petRepository.existsById("999")).thenReturn(false);
        boolean exists = petService.existsById("1");
        boolean notExists = petService.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(petRepository, times(1)).existsById("1");
        verify(petRepository, times(1)).existsById("999");
    }
}