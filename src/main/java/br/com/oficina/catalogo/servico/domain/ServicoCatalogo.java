package br.com.oficina.catalogo.servico.domain;

import br.com.oficina.shared.domain.Strings;
import br.com.oficina.shared.domain.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class ServicoCatalogo {

    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer tempoEstimadoMin;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private ServicoCatalogo() {
    }

    private ServicoCatalogo(String nome, String descricao, BigDecimal preco, Integer tempoEstimadoMin) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.tempoEstimadoMin = tempoEstimadoMin;
    }

    public static ServicoCatalogo novo(String nome, String descricao, BigDecimal preco, Integer tempoEstimadoMin) {
        String normalizedNome = Strings.requireNonBlank(nome, "nome");

        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("preco deve ser >= 0");
        }
        BigDecimal normalizedPreco = preco.setScale(2, RoundingMode.HALF_UP);

        if (tempoEstimadoMin == null || tempoEstimadoMin <= 0) {
            throw new ValidationException("tempoEstimadoMin deve ser > 0");
        }

        String normalizedDescricao = (descricao == null || descricao.trim().isBlank()) ? null : descricao.trim();

        return new ServicoCatalogo(normalizedNome, normalizedDescricao, normalizedPreco, tempoEstimadoMin);
    }

    public static ServicoCatalogo restaurar(
            UUID id,
            String nome,
            String descricao,
            BigDecimal preco,
            Integer tempoEstimadoMin,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        Objects.requireNonNull(id, "id");
        ServicoCatalogo s = new ServicoCatalogo();
        s.id = id;
        s.nome = Strings.requireNonBlank(nome, "nome");
        s.descricao = descricao;
        s.preco = Objects.requireNonNull(preco, "preco");
        s.tempoEstimadoMin = Objects.requireNonNull(tempoEstimadoMin, "tempoEstimadoMin");
        s.createdAt = createdAt;
        s.updatedAt = updatedAt;
        return s;
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

    public Integer getTempoEstimadoMin() {
        return tempoEstimadoMin;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void atualizar(String nome, String descricao, BigDecimal preco, Integer tempoEstimadoMin) {
        String normalizedNome = Strings.requireNonBlank(nome, "nome");
        String normalizedDescricao = (descricao == null || descricao.trim().isBlank()) ? null : descricao.trim();

        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("preco deve ser >= 0");
        }
        BigDecimal normalizedPreco = preco.setScale(2, RoundingMode.HALF_UP);

        if (tempoEstimadoMin == null || tempoEstimadoMin <= 0) {
            throw new ValidationException("tempoEstimadoMin deve ser > 0");
        }

        this.nome = normalizedNome;
        this.descricao = normalizedDescricao;
        this.preco = normalizedPreco;
        this.tempoEstimadoMin = tempoEstimadoMin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicoCatalogo that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
