package br.com.oficina.cadastros.cliente.domain;

import br.com.oficina.shared.domain.Strings;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Cliente {

    private UUID id;
    private String nome;
    private CpfCnpj cpfCnpj;
    private ClienteStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private Cliente() {
    }

    private Cliente(String nome, CpfCnpj cpfCnpj, ClienteStatus status) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.status = status != null ? status : ClienteStatus.ATIVO;
    }

    public static Cliente novo(String nome, CpfCnpj cpfCnpj) {
        String normalizedNome = Strings.requireNonBlank(nome, "nome");
        Objects.requireNonNull(cpfCnpj, "cpfCnpj nao pode ser null");
        return new Cliente(normalizedNome, cpfCnpj, ClienteStatus.ATIVO);
    }

    public static Cliente restaurar(
            UUID id,
            String nome,
            CpfCnpj cpfCnpj,
            ClienteStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        Objects.requireNonNull(id, "id");
        Cliente c = new Cliente();
        c.id = id;
        c.nome = Strings.requireNonBlank(nome, "nome");
        c.cpfCnpj = Objects.requireNonNull(cpfCnpj, "cpfCnpj");
        c.status = status != null ? status : ClienteStatus.ATIVO;
        c.createdAt = createdAt;
        c.updatedAt = updatedAt;
        return c;
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

    public ClienteStatus getStatus() {
        return status;
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
