package br.com.oficina.ordemservico.domain;

import br.com.oficina.shared.domain.ValidationException;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class OrdemServicoTransicaoStatus {

    private UUID id;
    private StatusOrdemServico deStatus;
    private StatusOrdemServico paraStatus;
    private OffsetDateTime ocorridoEm;

    private OrdemServicoTransicaoStatus() {
    }

    public static OrdemServicoTransicaoStatus criar(StatusOrdemServico deStatus, StatusOrdemServico paraStatus, OffsetDateTime ocorridoEm) {
        if (paraStatus == null) {
            throw new ValidationException("paraStatus nao pode ser null");
        }
        if (ocorridoEm == null) {
            throw new ValidationException("ocorridoEm nao pode ser null");
        }
        OrdemServicoTransicaoStatus t = new OrdemServicoTransicaoStatus();
        t.deStatus = deStatus;
        t.paraStatus = paraStatus;
        t.ocorridoEm = ocorridoEm;
        return t;
    }

    public static OrdemServicoTransicaoStatus restaurar(
            UUID id,
            StatusOrdemServico deStatus,
            StatusOrdemServico paraStatus,
            OffsetDateTime ocorridoEm
    ) {
        Objects.requireNonNull(id, "id");
        OrdemServicoTransicaoStatus t = new OrdemServicoTransicaoStatus();
        t.id = id;
        t.deStatus = deStatus;
        t.paraStatus = Objects.requireNonNull(paraStatus, "paraStatus");
        t.ocorridoEm = Objects.requireNonNull(ocorridoEm, "ocorridoEm");
        return t;
    }

    void definirId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public StatusOrdemServico getDeStatus() {
        return deStatus;
    }

    public StatusOrdemServico getParaStatus() {
        return paraStatus;
    }

    public OffsetDateTime getOcorridoEm() {
        return ocorridoEm;
    }
}
