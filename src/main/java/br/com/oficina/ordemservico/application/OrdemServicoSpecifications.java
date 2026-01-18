package br.com.oficina.ordemservico.application;

import br.com.oficina.ordemservico.domain.OrdemServico;
import br.com.oficina.ordemservico.domain.StatusOrdemServico;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class OrdemServicoSpecifications {

    private OrdemServicoSpecifications() {
    }

    public static Specification<OrdemServico> filtrar(
            StatusOrdemServico status,
            String placaNormalized,
            String cpfCnpjDigits,
            OffsetDateTime from,
            OffsetDateTime to
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (placaNormalized != null && !placaNormalized.isBlank()) {
                Join<Object, Object> veiculo = root.join("veiculo");
                // Veiculo.placa.value (embeddable)
                predicates.add(cb.equal(veiculo.get("placa").get("value"), placaNormalized));
            }

            if (cpfCnpjDigits != null && !cpfCnpjDigits.isBlank()) {
                Join<Object, Object> cliente = root.join("cliente");
                // Cliente.cpfCnpj.value (embeddable)
                predicates.add(cb.equal(cliente.get("cpfCnpj").get("value"), cpfCnpjDigits));
            }

            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
            }

            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), to));
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
