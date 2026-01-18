package br.com.oficina.cadastros.cliente.domain;

import br.com.oficina.shared.domain.Strings;
import jakarta.persistence.Embedded;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "clientes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_clientes_cpf_cnpj", columnNames = {"cpf_cnpj"})
        }
)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Embedded
    private CpfCnpj cpfCnpj;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    protected Cliente() {
        // JPA
    }

    private Cliente(String nome, CpfCnpj cpfCnpj) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
    }

    public static Cliente novo(String nome, CpfCnpj cpfCnpj) {
        String normalizedNome = Strings.requireNonBlank(nome, "nome");
        Objects.requireNonNull(cpfCnpj, "cpfCnpj nao pode ser null");
        return new Cliente(normalizedNome, cpfCnpj);
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public CpfCnpj getCpfCnpj() {
        return cpfCnpj;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void renomear(String novoNome) {
        this.nome = Strings.requireNonBlank(novoNome, "nome");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente cliente)) return false;
        return id != null && Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
