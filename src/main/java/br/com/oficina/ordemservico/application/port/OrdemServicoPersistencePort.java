package br.com.oficina.ordemservico.application.port;

import br.com.oficina.ordemservico.application.OrdemServicoListagemFiltro;
import br.com.oficina.ordemservico.domain.OrdemServico;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdemServicoPersistencePort {

    OrdemServico save(OrdemServico ordemServico);

    Optional<OrdemServico> findById(UUID id);

    Optional<OrdemServico> findByTrackingCode(String trackingCode);

    Optional<OrdemServico> findDetailedById(UUID id);

    Optional<OrdemServico> findDetailedByIdForUpdate(UUID id);

    Optional<OrdemServico> findDetailedByTrackingCode(String trackingCode);

    List<OrdemServico> findAllFiltered(OrdemServicoListagemFiltro filtro);

    Double avgExecutionSeconds(OffsetDateTime from, OffsetDateTime to);

    Long countExecutionMeasured(OffsetDateTime from, OffsetDateTime to);
}
