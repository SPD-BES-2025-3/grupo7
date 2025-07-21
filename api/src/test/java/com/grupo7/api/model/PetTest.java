package com.grupo7.api.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class PetTest {

    @Test
    public void testSettersAndGetters() {
        Pet pet = new Pet();

        pet.setNome("Rex");
        pet.setEspecie("Cachorro");
        pet.setRaca("Labrador");
        pet.setDataNascimento(LocalDate.of(2020, 1, 10));
        pet.setCor("Preto");
        pet.setSexo("M");
        pet.setPeso(25.5);
        pet.setObservacoes("Muito ativo");
        pet.setAtivo(true);
        pet.setClienteId("cliente123");

        assertEquals("Rex", pet.getNome());
        assertEquals("Cachorro", pet.getEspecie());
        assertEquals("Labrador", pet.getRaca());
        assertEquals(LocalDate.of(2020, 1, 10), pet.getDataNascimento());
        assertEquals("Preto", pet.getCor());
        assertEquals("M", pet.getSexo());
        assertEquals(25.5, pet.getPeso());
        assertEquals("Muito ativo", pet.getObservacoes());
        assertTrue(pet.isAtivo());
        assertEquals("cliente123", pet.getClienteId());
    }

    @Test
    public void testConstrutorComParametros() {
        LocalDate nascimento = LocalDate.of(2019, 6, 15);
        Pet pet = new Pet("Luna", "Gato", "Siamês", nascimento, "cliente456");

        assertEquals("Luna", pet.getNome());
        assertEquals("Gato", pet.getEspecie());
        assertEquals("Siamês", pet.getRaca());
        assertEquals(nascimento, pet.getDataNascimento());
        assertEquals("cliente456", pet.getClienteId());

        // Campos não inicializados no construtor devem estar null ou default
        assertNull(pet.getCor());
        assertNull(pet.getSexo());
        assertNull(pet.getPeso());
        assertNull(pet.getObservacoes());
        assertTrue(pet.isAtivo()); // pois é inicializado como true na declaração
    }
}
