package com.grupo7.api.service;

import com.grupo7.api.event.EstoqueEvent;
import com.grupo7.api.model.Produto;
import com.grupo7.api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private EventPublisherService eventPublisherService;

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> findById(String id) {
        return produtoRepository.findById(id);
    }

    public Optional<Produto> findByCodigo(String codigo) {
        return produtoRepository.findByCodigo(codigo);
    }

    public List<Produto> findByAtivo(boolean ativo) {
        return produtoRepository.findByAtivo(ativo);
    }

    public List<Produto> findByCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    public List<Produto> findByMarca(String marca) {
        return produtoRepository.findByMarca(marca);
    }

    public List<Produto> findByEstoqueLessThan(Integer estoque) {
        return produtoRepository.findByEstoqueLessThan(estoque);
    }

    public List<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByPrecoBetween(precoMin, precoMax);
    }

    public List<Produto> findByNomeContaining(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Produto> findByDescricaoContaining(String descricao) {
        return produtoRepository.findByDescricaoContainingIgnoreCase(descricao);
    }

    public List<Produto> findByCategoriaContaining(String categoria) {
        return produtoRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    public List<Produto> findByMarcaContaining(String marca) {
        return produtoRepository.findByMarcaContainingIgnoreCase(marca);
    }

    public Produto save(Produto produto) {
        if (produto.getId() != null) {
            // Verificar se código já existe para outro produto
            Optional<Produto> existingByCodigo = produtoRepository.findByCodigo(produto.getCodigo());
            if (existingByCodigo.isPresent() && !existingByCodigo.get().getId().equals(produto.getId())) {
                throw new RuntimeException("Código já cadastrado para outro produto");
            }
        } else {
            // Novo produto - verificar se código já existe
            if (produtoRepository.existsByCodigo(produto.getCodigo())) {
                throw new RuntimeException("Código já cadastrado");
            }
        }
        return produtoRepository.save(produto);
    }

    public Produto update(String id, Produto produto) {
        Optional<Produto> existingProduto = produtoRepository.findById(id);
        if (existingProduto.isPresent()) {
            Produto updatedProduto = existingProduto.get();
            updatedProduto.setNome(produto.getNome());
            updatedProduto.setDescricao(produto.getDescricao());
            updatedProduto.setCategoria(produto.getCategoria());
            updatedProduto.setCodigo(produto.getCodigo());
            updatedProduto.setPreco(produto.getPreco());
            updatedProduto.setEstoque(produto.getEstoque());
            updatedProduto.setMarca(produto.getMarca());
            updatedProduto.setTamanho(produto.getTamanho());
            updatedProduto.setPeso(produto.getPeso());
            updatedProduto.setAtivo(produto.isAtivo());
            return produtoRepository.save(updatedProduto);
        }
        throw new RuntimeException("Produto não encontrado");
    }

    public void deleteById(String id) {
        produtoRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return produtoRepository.existsById(id);
    }

    public boolean existsByCodigo(String codigo) {
        return produtoRepository.existsByCodigo(codigo);
    }

    // Métodos específicos para petshop
    public List<Produto> findProdutosAtivos() {
        return produtoRepository.findByAtivo(true);
    }

    public List<Produto> findProdutosEmBaixoEstoque(Integer limiteEstoque) {
        return produtoRepository.findByEstoqueLessThan(limiteEstoque);
    }

    public List<Produto> findProdutosPorCategoria(String categoria) {
        return produtoRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    public List<Produto> findProdutosPorMarca(String marca) {
        return produtoRepository.findByMarcaContainingIgnoreCase(marca);
    }

    public List<Produto> findProdutosPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByPrecoBetween(precoMin, precoMax);
    }

    public List<Produto> buscarPorNomeOuDescricao(String termo) {
        List<Produto> porNome = produtoRepository.findByNomeContainingIgnoreCase(termo);
        List<Produto> porDescricao = produtoRepository.findByDescricaoContainingIgnoreCase(termo);
        
        // Combinar resultados removendo duplicatas
        porNome.addAll(porDescricao.stream()
                .filter(produto -> porNome.stream()
                        .noneMatch(existing -> existing.getId().equals(produto.getId())))
                .toList());
        
        return porNome;
    }

    public boolean atualizarEstoque(String produtoId, Integer quantidade) {
        Optional<Produto> produto = produtoRepository.findById(produtoId);
        if (produto.isPresent()) {
            Produto p = produto.get();
            int estoqueAnterior = p.getEstoque();
            int novoEstoque = estoqueAnterior + quantidade;
            if (novoEstoque >= 0) {
                p.setEstoque(novoEstoque);
                produtoRepository.save(p);
                
                // Publicar evento de atualização de estoque
                String motivo = quantidade > 0 ? "REPOSIÇÃO" : "VENDA";
                EstoqueEvent estoqueEvent = new EstoqueEvent(
                    produtoId, 
                    p.getNome(), 
                    estoqueAnterior, 
                    novoEstoque, 
                    motivo
                );
                eventPublisherService.publishEstoqueEvent(estoqueEvent);
                
                return true;
            }
        }
        return false;
    }
} 