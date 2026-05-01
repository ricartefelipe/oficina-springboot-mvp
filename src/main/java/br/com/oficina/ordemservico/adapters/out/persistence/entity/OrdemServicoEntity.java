package br.com.oficina.ordemservico.adapters.out.persistence.entity;

import br.com.oficina.cadastros.cliente.infra.persistence.entity.ClienteEntity;
import br.com.oficina.cadastros.veiculo.infra.persistence.entity.VeiculoEntity;
import br.com.oficina.ordemservico.domain.StatusOrdemServico;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "ordens_servico",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_os_tracking_code", columnNames = {"tracking_code"})
        }
)
public class OrdemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tracking_code", nullable = false, length = 16)
    private String trackingCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private VeiculoEntity veiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private StatusOrdemServico status;

    @Column(name = "orcamento_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal orcamentoTotal;

    @Column(name = "orcamento_enviado_at")
    private OffsetDateTime orcamentoEnviadoAt;

    @Column(name = "aprovado_at")
    private OffsetDateTime aprovadoAt;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServicoItemServicoEntity> itensServico = new ArrayList<>();

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServicoItemPecaEntity> itensPeca = new ArrayList<>();

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServicoTransicaoStatusEntity> transicoesStatus = new ArrayList<>();

    public OrdemServicoEntity() {
    }

    public UUID getId() {
        return id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public VeiculoEntity getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(VeiculoEntity veiculo) {
        this.veiculo = veiculo;
    }

    public StatusOrdemServico getStatus() {
        return status;
    }

    public void setStatus(StatusOrdemServico status) {
        this.status = status;
    }

    public BigDecimal getOrcamentoTotal() {
        return orcamentoTotal;
    }

    public void setOrcamentoTotal(BigDecimal orcamentoTotal) {
        this.orcamentoTotal = orcamentoTotal;
    }

    public OffsetDateTime getOrcamentoEnviadoAt() {
        return orcamentoEnviadoAt;
    }

    public void setOrcamentoEnviadoAt(OffsetDateTime orcamentoEnviadoAt) {
        this.orcamentoEnviadoAt = orcamentoEnviadoAt;
    }

    public OffsetDateTime getAprovadoAt() {
        return aprovadoAt;
    }

    public void setAprovadoAt(OffsetDateTime aprovadoAt) {
        this.aprovadoAt = aprovadoAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<OrdemServicoItemServicoEntity> getItensServico() {
        return itensServico;
    }

    public List<OrdemServicoItemPecaEntity> getItensPeca() {
        return itensPeca;
    }

    public List<OrdemServicoTransicaoStatusEntity> getTransicoesStatus() {
        return transicoesStatus;
    }
}
