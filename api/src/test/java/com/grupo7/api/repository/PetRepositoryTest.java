package com.grupo7.api.repository;

import com.grupo7.api.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetRepositoryTest {

    @Mock
    private PetRepository petRepository;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
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
    void testFindAll() {
        List<Pet> pets = Arrays.asList(pet1, pet2);
        when(petRepository.findAll()).thenReturn(pets);
        List<Pet> result = petRepository.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Rex", result.get(0).getNome());
        assertEquals("Mimi", result.get(1).getNome());
        verify(petRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(petRepository.findById("1")).thenReturn(Optional.of(pet1));
        when(petRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Pet> found = petRepository.findById("1");
        Optional<Pet> notFound = petRepository.findById("999");
        assertTrue(found.isPresent());
        assertEquals("Rex", found.get().getNome());
        assertFalse(notFound.isPresent());
        verify(petRepository, times(1)).findById("1");
        verify(petRepository, times(1)).findById("999");
    }

    @Test
    void testFindByClienteId() {
        List<Pet> pets = Arrays.asList(pet1);
        when(petRepository.findByClienteId("cli1")).thenReturn(pets);
        List<Pet> result = petRepository.findByClienteId("cli1");
        assertEquals(1, result.size());
        assertEquals("cli1", result.get(0).getClienteId());
        verify(petRepository, times(1)).findByClienteId("cli1");
    }

    @Test
    void testFindByAtivo() {
        List<Pet> petsAtivos = Arrays.asList(pet1);
        List<Pet> petsInativos = Arrays.asList(pet2);
        when(petRepository.findByAtivo(true)).thenReturn(petsAtivos);
        when(petRepository.findByAtivo(false)).thenReturn(petsInativos);
        List<Pet> ativos = petRepository.findByAtivo(true);
        List<Pet> inativos = petRepository.findByAtivo(false);
        assertEquals(1, ativos.size());
        assertTrue(ativos.get(0).isAtivo());
        assertEquals(1, inativos.size());
        assertFalse(inativos.get(0).isAtivo());
        verify(petRepository, times(1)).findByAtivo(true);
        verify(petRepository, times(1)).findByAtivo(false);
    }

    @Test
    void testSave() {
        Pet novoPet = new Pet();
        novoPet.setNome("Toby");
        when(petRepository.save(any(Pet.class))).thenReturn(novoPet);
        Pet saved = petRepository.save(novoPet);
        assertNotNull(saved);
        assertEquals("Toby", saved.getNome());
        verify(petRepository, times(1)).save(novoPet);
    }

    @Test
    void testDeleteById() {
        doNothing().when(petRepository).deleteById("1");
        petRepository.deleteById("1");
        verify(petRepository, times(1)).deleteById("1");
    }

    @Test
    void testExistsById() {
        when(petRepository.existsById("1")).thenReturn(true);
        when(petRepository.existsById("999")).thenReturn(false);
        boolean exists = petRepository.existsById("1");
        boolean notExists = petRepository.existsById("999");
        assertTrue(exists);
        assertFalse(notExists);
        verify(petRepository, times(1)).existsById("1");
        verify(petRepository, times(1)).existsById("999");
    }
}