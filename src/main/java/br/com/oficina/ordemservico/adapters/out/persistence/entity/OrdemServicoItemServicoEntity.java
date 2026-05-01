package br.com.oficina.ordemservico.adapters.out.persistence.entity;

import br.com.oficina.catalogo.servico.infra.persistence.entity.ServicoCatalogoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "os_itens_servico")
public class OrdemServicoItemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServicoEntity ordemServico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoCatalogoEntity servico;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "tempo_estimado_min", nullable = false)
    private Integer tempoEstimadoMin;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    public OrdemServicoItemServicoEntity() {
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

    public ServicoCatalogoEntity getServico() {
        return servico;
    }

    public void setServico(ServicoCatalogoEntity servico) {
        this.servico = servico;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getTempoEstimadoMin() {
        return tempoEstimadoMin;
    }

    public void setTempoEstimadoMin(Integer tempoEstimadoMin) {
        this.tempoEstimadoMin = tempoEstimadoMin;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
