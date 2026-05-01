package br.com.oficina.ordemservico.adapters.out.persistence.entity;

import br.com.oficina.ordemservico.domain.StatusOrdemServico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "os_transicoes_status")
public class OrdemServicoTransicaoStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServicoEntity ordemServico;

    @Enumerated(EnumType.STRING)
    @Column(name = "de_status", length = 40)
    private StatusOrdemServico deStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "para_status", nullable = false, length = 40)
    private StatusOrdemServico paraStatus;

    @Column(name = "ocorrido_em", nullable = false)
    private OffsetDateTime ocorridoEm;

    public OrdemServicoTransicaoStatusEntity() {
    }

    public UUID getId() {
        return id;
    }

    public OrdemServicoEntity getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServicoEntity ordemServico) {
        this.ordemServico = ordemServico;
    }

    public StatusOrdemServico getDeStatus() {
        return deStatus;
    }

    public void setDeStatus(StatusOrdemServico deStatus) {
        this.deStatus = deStatus;
    }

    public StatusOrdemServico getParaStatus() {
        return paraStatus;
    }

    public void setParaStatus(StatusOrdemServico paraStatus) {
        this.paraStatus = paraStatus;
    }

    public OffsetDateTime getOcorridoEm() {
        return ocorridoEm;
    }

    public void setOcorridoEm(OffsetDateTime ocorridoEm) {
        this.ocorridoEm = ocorridoEm;
    }
}
