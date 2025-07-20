package com.grupo7.api.controller;

import com.grupo7.api.model.Produto;
import com.grupo7.api.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {
        List<Produto> produtos = produtoService.findAll();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable String id) {
        Optional<Produto> produto = produtoService.findById(id);
        return produto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Produto> getProdutoByCodigo(@PathVariable String codigo) {
        Optional<Produto> produto = produtoService.findByCodigo(codigo);
        return produto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Produto>> getProdutosAtivos() {
        List<Produto> produtos = produtoService.findProdutosAtivos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> getProdutosByCategoria(@PathVariable String categoria) {
        List<Produto> produtos = produtoService.findProdutosPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<Produto>> getProdutosByMarca(@PathVariable String marca) {
        List<Produto> produtos = produtoService.findProdutosPorMarca(marca);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> getProdutosEmBaixoEstoque(@RequestParam(defaultValue = "10") Integer limite) {
        List<Produto> produtos = produtoService.findProdutosEmBaixoEstoque(limite);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/preco")
    public ResponseEntity<List<Produto>> getProdutosPorFaixaPreco(
            @RequestParam BigDecimal precoMin, 
            @RequestParam BigDecimal precoMax) {
        List<Produto> produtos = produtoService.findProdutosPorFaixaPreco(precoMin, precoMax);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/busca")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        List<Produto> produtos = produtoService.findByNomeContaining(nome);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/busca/descricao")
    public ResponseEntity<List<Produto>> buscarPorDescricao(@RequestParam String descricao) {
        List<Produto> produtos = produtoService.findByDescricaoContaining(descricao);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/busca/geral")
    public ResponseEntity<List<Produto>> buscarGeral(@RequestParam String termo) {
        List<Produto> produtos = produtoService.buscarPorNomeOuDescricao(termo);
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public ResponseEntity<Produto> createProduto(@Valid @RequestBody Produto produto) {
        try {
            Produto savedProduto = produtoService.save(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable String id, @Valid @RequestBody Produto produto) {
        try {
            Produto updatedProduto = produtoService.update(id, produto);
            return ResponseEntity.ok(updatedProduto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable String id) {
        if (produtoService.existsById(id)) {
            produtoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Produto> alterarStatusProduto(@PathVariable String id, @RequestParam boolean ativo) {
        Optional<Produto> produto = produtoService.findById(id);
        if (produto.isPresent()) {
            Produto p = produto.get();
            p.setAtivo(ativo);
            Produto updatedProduto = produtoService.save(p);
            return ResponseEntity.ok(updatedProduto);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/estoque")
    public ResponseEntity<Produto> atualizarEstoque(@PathVariable String id, @RequestParam Integer quantidade) {
        boolean sucesso = produtoService.atualizarEstoque(id, quantidade);
        if (sucesso) {
            Optional<Produto> produto = produtoService.findById(id);
            return produto.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}/preco")
    public ResponseEntity<Produto> atualizarPreco(@PathVariable String id, @RequestParam BigDecimal preco) {
        Optional<Produto> produto = produtoService.findById(id);
        if (produto.isPresent()) {
            Produto p = produto.get();
            p.setPreco(preco);
            Produto updatedProduto = produtoService.save(p);
            return ResponseEntity.ok(updatedProduto);
        }
        return ResponseEntity.notFound().build();
    }
} 