package br.com.oficina.ordemservico.infra.persistence;

import br.com.oficina.ordemservico.domain.OrdemServico;
import br.com.oficina.ordemservico.domain.StatusOrdemServico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdemServicoJpaRepository extends JpaRepository<OrdemServico, UUID>, JpaSpecificationExecutor<OrdemServico> {

    Optional<OrdemServico> findByTrackingCode(String trackingCode);

    List<OrdemServico> findByStatus(StatusOrdemServico status);

    @EntityGraph(attributePaths = {
            "cliente",
            "veiculo"
    })
    List<OrdemServico> findAll(Specification<OrdemServico> spec);

    @EntityGraph(attributePaths = {
            "cliente",
            "veiculo",
            "itensServico",
            "itensServico.servico",
            "itensPeca",
            "itensPeca.peca",
            "transicoesStatus"
    })
    Optional<OrdemServico> findDetailedById(UUID id);

    @EntityGraph(attributePaths = {
            "cliente",
            "veiculo",
            "itensServico",
            "itensServico.servico",
            "itensPeca",
            "itensPeca.peca",
            "transicoesStatus"
    })
    Optional<OrdemServico> findDetailedByTrackingCode(String trackingCode);

    /**
     * Media (em segundos) do tempo entre a transicao para EM_EXECUCAO e para FINALIZADA.
     *
     * Filtra por periodo usando o timestamp da transicao FINALIZADA.
     */
    @Query(
            value = """
                    select avg(extract(epoch from (t_final.ocorrido_em - t_exec.ocorrido_em)))
                    from os_transicoes_status t_exec
                    join os_transicoes_status t_final
                      on t_final.ordem_servico_id = t_exec.ordem_servico_id
                    where t_exec.para_status = 'EM_EXECUCAO'
                      and t_final.para_status = 'FINALIZADA'
                      and (:from is null or t_final.ocorrido_em >= :from)
                      and (:to is null or t_final.ocorrido_em <= :to)
                    """,
            nativeQuery = true
    )
    Double avgExecutionSeconds(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);

    @Query(
            value = """
                    select count(distinct t_final.ordem_servico_id)
                    from os_transicoes_status t_exec
                    join os_transicoes_status t_final
                      on t_final.ordem_servico_id = t_exec.ordem_servico_id
                    where t_exec.para_status = 'EM_EXECUCAO'
                      and t_final.para_status = 'FINALIZADA'
                      and (:from is null or t_final.ocorrido_em >= :from)
                      and (:to is null or t_final.ocorrido_em <= :to)
                    """,
            nativeQuery = true
    )
    Long countExecutionMeasured(@Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);
}
