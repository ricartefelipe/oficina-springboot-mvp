package br.com.oficina.ordemservico.domain;

import br.com.oficina.catalogo.peca.domain.PecaInsumo;
import br.com.oficina.shared.domain.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public class OrdemServicoItemPeca {

    private UUID id;
    private PecaInsumo peca;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    private OrdemServicoItemPeca() {
    }

    private OrdemServicoItemPeca(PecaInsumo peca, Integer quantidade, BigDecimal precoUnitario) {
        this.peca = peca;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade)).setScale(2, RoundingMode.HALF_UP);
    }

    public static OrdemServicoItemPeca criar(OrdemServico ordemServico, PecaInsumo peca, int quantidade) {
        Objects.requireNonNull(ordemServico, "ordemServico nao pode ser null");
        Objects.requireNonNull(peca, "peca nao pode ser null");
        if (quantidade <= 0) {
            throw new ValidationException("quantidade deve ser > 0");
        }

        BigDecimal precoUnitario = peca.getPreco();
        if (precoUnitario == null) {
            throw new ValidationException("Peca/Insumo sem preco definido");
        }

        return new OrdemServicoItemPeca(peca, quantidade, precoUnitario);
    }

    public static OrdemServicoItemPeca restaurar(
            UUID id,
            PecaInsumo peca,
            Integer quantidade,
            BigDecimal precoUnitario,
            BigDecimal subtotal
    ) {
        Objects.requireNonNull(id, "id");
        OrdemServicoItemPeca i = new OrdemServicoItemPeca();
        i.id = id;
        i.peca = Objects.requireNonNull(peca, "peca");
        i.quantidade = quantidade;
        i.precoUnitario = precoUnitario;
        i.subtotal = subtotal;
        return i;
    }

    void definirId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public PecaInsumo getPeca() {
        return peca;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
