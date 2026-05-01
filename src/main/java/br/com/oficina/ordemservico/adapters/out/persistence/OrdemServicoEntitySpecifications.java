package br.com.oficina.ordemservico.adapters.out.persistence;

import br.com.oficina.ordemservico.adapters.out.persistence.entity.OrdemServicoEntity;
import br.com.oficina.ordemservico.application.OrdemServicoListagemFiltro;
import br.com.oficina.ordemservico.application.OrdemServicoListagemOrdem;
import br.com.oficina.ordemservico.domain.StatusOrdemServico;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public final class OrdemServicoEntitySpecifications {

    private OrdemServicoEntitySpecifications() {
    }

    public static Specification<OrdemServicoEntity> from(OrdemServicoListagemFiltro f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (f.status() != null) {
                predicates.add(cb.equal(root.get("status"), f.status()));
            }

            if (f.excluirEncerradas()) {
                predicates.add(cb.not(root.get("status").in(
                        StatusOrdemServico.FINALIZADA,
                        StatusOrdemServico.ENTREGUE
                )));
            }

            if (f.placaNormalized() != null && !f.placaNormalized().isBlank()) {
                Join<Object, Object> veiculo = root.join("veiculo");
                predicates.add(cb.equal(veiculo.get("placa"), f.placaNormalized()));
            }

            if (f.cpfCnpjDigits() != null && !f.cpfCnpjDigits().isBlank()) {
                Join<Object, Object> cliente = root.join("cliente");
                predicates.add(cb.equal(cliente.get("cpfCnpj"), f.cpfCnpjDigits()));
            }

            if (f.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), f.from()));
            }

            if (f.to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), f.to()));
            }

            aplicarOrdenacao(root, query, cb, f.ordem());

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void aplicarOrdenacao(
            Root<OrdemServicoEntity> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            OrdemServicoListagemOrdem ordem
    ) {
        switch (ordem) {
            case PRIORIDADE_OPERACAO -> {
                Expression<Integer> prioridade = prioridadeOperacaoExpression(root, cb);
                query.orderBy(cb.asc(prioridade), cb.asc(root.get("createdAt")));
            }
            case CRIADA_MAIS_ANTIGA -> query.orderBy(cb.asc(root.get("createdAt")));
            case CRIADA_MAIS_RECENTE -> query.orderBy(cb.desc(root.get("createdAt")));
        }
    }

    private static Expression<Integer> prioridadeOperacaoExpression(Root<OrdemServicoEntity> root, CriteriaBuilder cb) {
        return cb.<Integer>selectCase()
                .when(cb.equal(root.get("status"), StatusOrdemServico.EM_EXECUCAO), 1)
                .when(cb.equal(root.get("status"), StatusOrdemServico.AGUARDANDO_APROVACAO), 2)
                .when(cb.equal(root.get("status"), StatusOrdemServico.EM_DIAGNOSTICO), 3)
                .when(cb.equal(root.get("status"), StatusOrdemServico.RECEBIDA), 4)
                .otherwise(99);
    }
}
