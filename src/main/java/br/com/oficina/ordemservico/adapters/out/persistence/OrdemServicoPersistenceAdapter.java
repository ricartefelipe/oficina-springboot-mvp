package br.com.oficina.ordemservico.adapters.out.persistence;

import br.com.oficina.ordemservico.adapters.out.persistence.entity.OrdemServicoEntity;
import br.com.oficina.ordemservico.application.OrdemServicoListagemFiltro;
import br.com.oficina.ordemservico.application.port.OrdemServicoPersistencePort;
import br.com.oficina.ordemservico.domain.OrdemServico;
import br.com.oficina.shared.domain.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrdemServicoPersistenceAdapter implements OrdemServicoPersistencePort {

    private final OrdemServicoJpaRepository jpa;
    private final OrdemServicoPersistenceMapper mapper;

    public OrdemServicoPersistenceAdapter(OrdemServicoJpaRepository jpa, OrdemServicoPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public OrdemServico save(OrdemServico domain) {
        if (domain.getId() == null) {
            OrdemServicoEntity entity = new OrdemServicoEntity();
            mapper.mergeDomainIntoEntity(domain, entity);
            return mapper.toDomain(jpa.save(entity));
        }
        OrdemServicoEntity managed = jpa.findDetailedByIdForUpdate(domain.getId())
                .orElseThrow(() -> new NotFoundException("Ordem de Servico nao encontrada"));
        mapper.mergeDomainIntoEntity(domain, managed);
        return mapper.toDomain(jpa.save(managed));
    }

    @Override
    public Optional<OrdemServico> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<OrdemServico> findByTrackingCode(String trackingCode) {
        return jpa.findByTrackingCode(trackingCode).map(mapper::toDomain);
    }

    @Override
    public Optional<OrdemServico> findDetailedById(UUID id) {
        return jpa.findDetailedById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<OrdemServico> findDetailedByIdForUpdate(UUID id) {
        return jpa.findDetailedByIdForUpdate(id).map(mapper::toDomain);
    }

    @Override
    public Optional<OrdemServico> findDetailedByTrackingCode(String trackingCode) {
        return jpa.findDetailedByTrackingCode(trackingCode).map(mapper::toDomain);
    }

    @Override
    public List<OrdemServico> findAllFiltered(OrdemServicoListagemFiltro filtro) {
        Specification<OrdemServicoEntity> spec = OrdemServicoEntitySpecifications.from(filtro);
        return jpa.findAll(spec).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Double avgExecutionSeconds(OffsetDateTime from, OffsetDateTime to) {
        return jpa.avgExecutionSeconds(from, to);
    }

    @Override
    public Long countExecutionMeasured(OffsetDateTime from, OffsetDateTime to) {
        return jpa.countExecutionMeasured(from, to);
    }
}
