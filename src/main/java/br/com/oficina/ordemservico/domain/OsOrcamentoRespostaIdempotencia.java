package br.com.oficina.ordemservico.domain;

import java.util.UUID;

/**
 * Registro de idempotência para resposta externa ao orçamento (domínio puro; persistência em {@code OsOrcamentoRespostaIdempotenciaEntity}).
 */
public class OsOrcamentoRespostaIdempotencia {

    private final UUID id;
    private final String idempotencyKey;
    private final UUID ordemServicoId;
    private final DecisaoRespostaOrcamentoExterna decisao;

    public OsOrcamentoRespostaIdempotencia(UUID id, String idempotencyKey, UUID ordemServicoId, DecisaoRespostaOrcamentoExterna decisao) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.ordemServicoId = ordemServicoId;
        this.decisao = decisao;
    }

    public UUID getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public UUID getOrdemServicoId() {
        return ordemServicoId;
    }

    public DecisaoRespostaOrcamentoExterna getDecisao() {
        return decisao;
    }
}
