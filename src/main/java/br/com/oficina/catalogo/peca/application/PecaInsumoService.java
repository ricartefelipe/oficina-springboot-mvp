package br.com.oficina.catalogo.peca.application;

import br.com.oficina.catalogo.peca.domain.PecaInsumo;
import br.com.oficina.catalogo.peca.infra.persistence.PecaInsumoEntityMapper;
import br.com.oficina.catalogo.peca.infra.persistence.PecaInsumoJpaRepository;
import br.com.oficina.catalogo.peca.infra.persistence.entity.PecaInsumoEntity;
import br.com.oficina.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PecaInsumoService {

    private final PecaInsumoJpaRepository repository;
    private final PecaInsumoEntityMapper mapper;

    public PecaInsumoService(PecaInsumoJpaRepository repository, PecaInsumoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public PecaInsumo criar(String nome, String descricao, BigDecimal preco, Integer estoqueAtual, Integer estoqueMinimo) {
        PecaInsumo p = PecaInsumo.nova(nome, descricao, preco, estoqueAtual, estoqueMinimo);
        return mapper.toDomain(repository.save(mapper.toNewEntity(p)));
    }

    @Transactional(readOnly = true)
    public PecaInsumo obter(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("Peca/Insumo nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<PecaInsumo> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Transactional
    public PecaInsumo atualizar(UUID id, String nome, String descricao, BigDecimal preco, Integer estoqueAtual, Integer estoqueMinimo) {
        PecaInsumoEntity e = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Peca/Insumo nao encontrado"));
        PecaInsumo p = mapper.toDomain(e);
        p.atualizar(nome, descricao, preco, estoqueAtual, estoqueMinimo);
        mapper.aplicar(p, e);
        return mapper.toDomain(repository.save(e));
    }

    @Transactional
    public void remover(UUID id) {
        PecaInsumoEntity e = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Peca/Insumo nao encontrado"));
        repository.delete(e);
    }
}
