package br.com.oficina.ordemservico.domain;

import br.com.oficina.catalogo.servico.domain.ServicoCatalogo;
import br.com.oficina.shared.domain.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public class OrdemServicoItemServico {

    private UUID id;
    private ServicoCatalogo servico;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private Integer tempoEstimadoMin;
    private BigDecimal subtotal;

    private OrdemServicoItemServico() {
    }

    private OrdemServicoItemServico(ServicoCatalogo servico, Integer quantidade, BigDecimal precoUnitario, Integer tempoEstimadoMin) {
        this.servico = servico;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.tempoEstimadoMin = tempoEstimadoMin;
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade)).setScale(2, RoundingMode.HALF_UP);
    }

    public static OrdemServicoItemServico criar(OrdemServico ordemServico, ServicoCatalogo servico, int quantidade) {
        Objects.requireNonNull(ordemServico, "ordemServico nao pode ser null");
        Objects.requireNonNull(servico, "servico nao pode ser null");
        if (quantidade <= 0) {
            throw new ValidationException("quantidade deve ser > 0");
        }
        BigDecimal precoUnitario = servico.getPreco();
        if (precoUnitario == null) {
            throw new ValidationException("Servico sem preco definido");
        }
        Integer tempoEstimadoMin = servico.getTempoEstimadoMin();
        if (tempoEstimadoMin == null || tempoEstimadoMin <= 0) {
            throw new ValidationException("Servico sem tempo estimado valido");
        }
        return new OrdemServicoItemServico(servico, quantidade, precoUnitario, tempoEstimadoMin);
    }

    public static OrdemServicoItemServico restaurar(
            UUID id,
            ServicoCatalogo servico,
            Integer quantidade,
            BigDecimal precoUnitario,
            Integer tempoEstimadoMin,
            BigDecimal subtotal
    ) {
        Objects.requireNonNull(id, "id");
        OrdemServicoItemServico i = new OrdemServicoItemServico();
        i.id = id;
        i.servico = Objects.requireNonNull(servico, "servico");
        i.quantidade = quantidade;
        i.precoUnitario = precoUnitario;
        i.tempoEstimadoMin = tempoEstimadoMin;
        i.subtotal = subtotal;
        return i;
    }

    void definirId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public ServicoCatalogo getServico() {
        return servico;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public Integer getTempoEstimadoMin() {
        return tempoEstimadoMin;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
