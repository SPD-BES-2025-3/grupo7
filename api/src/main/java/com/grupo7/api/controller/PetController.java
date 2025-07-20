package com.grupo7.api.controller;

import com.grupo7.api.model.Pet;
import com.grupo7.api.service.PetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.findAll();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable String id) {
        Optional<Pet> pet = petService.findById(id);
        return pet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pet>> getPetsByCliente(@PathVariable String clienteId) {
        List<Pet> pets = petService.findPetsPorCliente(clienteId);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Pet>> getPetsAtivos() {
        List<Pet> pets = petService.findPetsAtivos();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Pet>> getPetsByEspecie(@PathVariable String especie) {
        List<Pet> pets = petService.findPetsPorEspecie(especie);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/raca/{raca}")
    public ResponseEntity<List<Pet>> getPetsByRaca(@PathVariable String raca) {
        List<Pet> pets = petService.findPetsPorRaca(raca);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/busca")
    public ResponseEntity<List<Pet>> buscarPorNome(@RequestParam String nome) {
        List<Pet> pets = petService.findByNomeContaining(nome);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/busca/raca")
    public ResponseEntity<List<Pet>> buscarPorRaca(@RequestParam String raca) {
        List<Pet> pets = petService.findByRacaContaining(raca);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/busca/geral")
    public ResponseEntity<List<Pet>> buscarGeral(@RequestParam String termo) {
        List<Pet> pets = petService.buscarPorNomeOuRaca(termo);
        return ResponseEntity.ok(pets);
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet pet) {
        try {
            Pet savedPet = petService.save(pet);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable String id, @Valid @RequestBody Pet pet) {
        try {
            Pet updatedPet = petService.update(id, pet);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable String id) {
        if (petService.existsById(id)) {
            petService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Pet> alterarStatusPet(@PathVariable String id, @RequestParam boolean ativo) {
        Optional<Pet> pet = petService.findById(id);
        if (pet.isPresent()) {
            Pet p = pet.get();
            p.setAtivo(ativo);
            Pet updatedPet = petService.save(p);
            return ResponseEntity.ok(updatedPet);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/peso")
    public ResponseEntity<Pet> atualizarPesoPet(@PathVariable String id, @RequestParam Double peso) {
        Optional<Pet> pet = petService.findById(id);
        if (pet.isPresent()) {
            Pet p = pet.get();
            p.setPeso(peso);
            Pet updatedPet = petService.save(p);
            return ResponseEntity.ok(updatedPet);
        }
        return ResponseEntity.notFound().build();
    }
} 