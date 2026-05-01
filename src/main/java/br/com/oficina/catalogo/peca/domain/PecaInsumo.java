package br.com.oficina.catalogo.peca.domain;

import br.com.oficina.shared.domain.BusinessRuleException;
import br.com.oficina.shared.domain.Strings;
import br.com.oficina.shared.domain.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class PecaInsumo {

    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoqueAtual;
    private Integer estoqueMinimo;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private PecaInsumo() {
    }

    private PecaInsumo(String nome, String descricao, BigDecimal preco, Integer estoqueAtual, Integer estoqueMinimo) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoqueAtual = estoqueAtual;
        this.estoqueMinimo = estoqueMinimo;
    }

    public static PecaInsumo nova(String nome, String descricao, BigDecimal preco, Integer estoqueAtual, Integer estoqueMinimo) {
        String normalizedNome = Strings.requireNonBlank(nome, "nome");
        String normalizedDescricao = (descricao == null || descricao.trim().isBlank()) ? null : descricao.trim();

        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("preco deve ser >= 0");
        }
        BigDecimal normalizedPreco = preco.setScale(2, RoundingMode.HALF_UP);

        if (estoqueAtual == null || estoqueAtual < 0) {
            throw new ValidationException("estoqueAtual deve ser >= 0");
        }
        if (estoqueMinimo == null || estoqueMinimo < 0) {
            throw new ValidationException("estoqueMinimo deve ser >= 0");
        }

        return new PecaInsumo(normalizedNome, normalizedDescricao, normalizedPreco, estoqueAtual, estoqueMinimo);
    }

    public static PecaInsumo restaurar(
            UUID id,
            String nome,
            String descricao,
            BigDecimal preco,
            Integer estoqueAtual,
            Integer estoqueMinimo,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        Objects.requireNonNull(id, "id");
        PecaInsumo p = new PecaInsumo();
        p.id = id;
        p.nome = Strings.requireNonBlank(nome, "nome");
        p.descricao = descricao;
        p.preco = Objects.requireNonNull(preco, "preco");
        p.estoqueAtual = Objects.requireNonNull(estoqueAtual, "estoqueAtual");
        p.estoqueMinimo = Objects.requireNonNull(estoqueMinimo, "estoqueMinimo");
        p.createdAt = createdAt;
        p.updatedAt = updatedAt;
        return p;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getEstoqueAtual() {
        return estoqueAtual;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void atualizar(String nome, String descricao, BigDecimal preco, Integer estoqueAtual, Integer estoqueMinimo) {
        String normalizedNome = Strings.requireNonBlank(nome, "nome");
        String normalizedDescricao = (descricao == null || descricao.trim().isBlank()) ? null : descricao.trim();

        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("preco deve ser >= 0");
        }
        BigDecimal normalizedPreco = preco.setScale(2, RoundingMode.HALF_UP);

        if (estoqueAtual == null || estoqueAtual < 0) {
            throw new ValidationException("estoqueAtual deve ser >= 0");
        }
        if (estoqueMinimo == null || estoqueMinimo < 0) {
            throw new ValidationException("estoqueMinimo deve ser >= 0");
        }

        this.nome = normalizedNome;
        this.descricao = normalizedDescricao;
        this.preco = normalizedPreco;
        this.estoqueAtual = estoqueAtual;
        this.estoqueMinimo = estoqueMinimo;
    }

    public void decrementarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new ValidationException("quantidade deve ser > 0");
        }
        if (estoqueAtual < quantidade) {
            throw new BusinessRuleException("Estoque insuficiente para peca/insumo: " + nome);
        }
        estoqueAtual = estoqueAtual - quantidade;
    }

    public void incrementarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new ValidationException("quantidade deve ser > 0");
        }
        estoqueAtual = estoqueAtual + quantidade;
    }

    public boolean abaixoDoMinimo() {
        return estoqueAtual != null && estoqueMinimo != null && estoqueAtual < estoqueMinimo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PecaInsumo that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
