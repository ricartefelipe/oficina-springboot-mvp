package br.com.oficina.cadastros.veiculo.domain;

import br.com.oficina.cadastros.cliente.domain.Cliente;
import br.com.oficina.shared.domain.Strings;
import br.com.oficina.shared.domain.ValidationException;

import java.time.OffsetDateTime;
import java.time.Year;
import java.util.Objects;
import java.util.UUID;

public class Veiculo {

    private UUID id;
    private Placa placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private Cliente cliente;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private Veiculo() {
    }

    private Veiculo(Placa placa, String marca, String modelo, Integer ano, Cliente cliente) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cliente = cliente;
    }

    public static Veiculo novo(Placa placa, String marca, String modelo, Integer ano, Cliente cliente) {
        Objects.requireNonNull(placa, "placa nao pode ser null");
        Objects.requireNonNull(cliente, "cliente nao pode ser null");

        String normalizedMarca = Strings.requireNonBlank(marca, "marca");
        String normalizedModelo = Strings.requireNonBlank(modelo, "modelo");

        if (ano == null) {
            throw new ValidationException("ano nao pode ser null");
        }
        int currentYear = Year.now().getValue();
        if (ano < 1900 || ano > currentYear + 1) {
            throw new ValidationException("ano invalido");
        }

        return new Veiculo(placa, normalizedMarca, normalizedModelo, ano, cliente);
    }

    public static Veiculo restaurar(
            UUID id,
            Placa placa,
            String marca,
            String modelo,
            Integer ano,
            Cliente cliente,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        Objects.requireNonNull(id, "id");
        Veiculo v = new Veiculo();
        v.id = id;
        v.placa = Objects.requireNonNull(placa, "placa");
        v.marca = Strings.requireNonBlank(marca, "marca");
        v.modelo = Strings.requireNonBlank(modelo, "modelo");
        v.ano = Objects.requireNonNull(ano, "ano");
        v.cliente = Objects.requireNonNull(cliente, "cliente");
        v.createdAt = createdAt;
        v.updatedAt = updatedAt;
        return v;
    }

    public UUID getId() {
        return id;
    }

    public Placa getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void atualizarDados(String marca, String modelo, Integer ano) {
        String normalizedMarca = Strings.requireNonBlank(marca, "marca");
        String normalizedModelo = Strings.requireNonBlank(modelo, "modelo");

        if (ano == null) {
            throw new ValidationException("ano nao pode ser null");
        }
        int currentYear = Year.now().getValue();
        if (ano < 1900 || ano > currentYear + 1) {
            throw new ValidationException("ano invalido");
        }

        this.marca = normalizedMarca;
        this.modelo = normalizedModelo;
        this.ano = ano;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo veiculo)) return false;
        return id != null && Objects.equals(id, veiculo.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
