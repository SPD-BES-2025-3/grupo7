package com.grupo7.api.service;

import com.grupo7.api.model.Pet;
import com.grupo7.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Optional<Pet> findById(String id) {
        return petRepository.findById(id);
    }

    public List<Pet> findByClienteId(String clienteId) {
        return petRepository.findByClienteId(clienteId);
    }

    public List<Pet> findByAtivo(boolean ativo) {
        return petRepository.findByAtivo(ativo);
    }

    public List<Pet> findByEspecie(String especie) {
        return petRepository.findByEspecie(especie);
    }

    public List<Pet> findByRaca(String raca) {
        return petRepository.findByRaca(raca);
    }

    public List<Pet> findByNomeContaining(String nome) {
        return petRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Pet> findByRacaContaining(String raca) {
        return petRepository.findByRacaContainingIgnoreCase(raca);
    }

    public List<Pet> findByClienteIdAndAtivo(String clienteId, boolean ativo) {
        return petRepository.findByClienteIdAndAtivo(clienteId, ativo);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet update(String id, Pet pet) {
        Optional<Pet> existingPet = petRepository.findById(id);
        if (existingPet.isPresent()) {
            Pet updatedPet = existingPet.get();
            updatedPet.setNome(pet.getNome());
            updatedPet.setEspecie(pet.getEspecie());
            updatedPet.setRaca(pet.getRaca());
            updatedPet.setDataNascimento(pet.getDataNascimento());
            updatedPet.setCor(pet.getCor());
            updatedPet.setSexo(pet.getSexo());
            updatedPet.setPeso(pet.getPeso());
            updatedPet.setObservacoes(pet.getObservacoes());
            updatedPet.setAtivo(pet.isAtivo());
            updatedPet.setClienteId(pet.getClienteId());
            return petRepository.save(updatedPet);
        }
        throw new RuntimeException("Pet não encontrado");
    }

    public void deleteById(String id) {
        petRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return petRepository.existsById(id);
    }

    // Métodos específicos para petshop
    public List<Pet> findPetsAtivos() {
        return petRepository.findByAtivo(true);
    }

    public List<Pet> findPetsPorCliente(String clienteId) {
        return petRepository.findByClienteIdAndAtivo(clienteId, true);
    }

    public List<Pet> buscarPorNomeOuRaca(String termo) {
        List<Pet> porNome = petRepository.findByNomeContainingIgnoreCase(termo);
        List<Pet> porRaca = petRepository.findByRacaContainingIgnoreCase(termo);
        
        // Combinar resultados removendo duplicatas
        porNome.addAll(porRaca.stream()
                .filter(pet -> porNome.stream()
                        .noneMatch(existing -> existing.getId().equals(pet.getId())))
                .toList());
        
        return porNome;
    }

    public List<Pet> findPetsPorEspecie(String especie) {
        return petRepository.findByEspecie(especie);
    }

    public List<Pet> findPetsPorRaca(String raca) {
        return petRepository.findByRaca(raca);
    }
} 