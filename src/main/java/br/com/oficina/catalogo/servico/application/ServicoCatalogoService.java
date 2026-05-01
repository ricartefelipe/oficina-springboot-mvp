package br.com.oficina.catalogo.servico.application;

import br.com.oficina.catalogo.servico.domain.ServicoCatalogo;
import br.com.oficina.catalogo.servico.infra.persistence.ServicoCatalogoEntityMapper;
import br.com.oficina.catalogo.servico.infra.persistence.ServicoCatalogoJpaRepository;
import br.com.oficina.catalogo.servico.infra.persistence.entity.ServicoCatalogoEntity;
import br.com.oficina.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ServicoCatalogoService {

    private final ServicoCatalogoJpaRepository repository;
    private final ServicoCatalogoEntityMapper mapper;

    public ServicoCatalogoService(ServicoCatalogoJpaRepository repository, ServicoCatalogoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ServicoCatalogo criar(String nome, String descricao, BigDecimal preco, Integer tempoEstimadoMin) {
        ServicoCatalogo s = ServicoCatalogo.novo(nome, descricao, preco, tempoEstimadoMin);
        return mapper.toDomain(repository.save(mapper.toNewEntity(s)));
    }

    @Transactional(readOnly = true)
    public ServicoCatalogo obter(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("Servico nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ServicoCatalogo> listar() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Transactional
    public ServicoCatalogo atualizar(UUID id, String nome, String descricao, BigDecimal preco, Integer tempoEstimadoMin) {
        ServicoCatalogoEntity e = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servico nao encontrado"));
        ServicoCatalogo s = mapper.toDomain(e);
        s.atualizar(nome, descricao, preco, tempoEstimadoMin);
        mapper.aplicar(s, e);
        return mapper.toDomain(repository.save(e));
    }

    @Transactional
    public void remover(UUID id) {
        ServicoCatalogoEntity e = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servico nao encontrado"));
        repository.delete(e);
    }
}
