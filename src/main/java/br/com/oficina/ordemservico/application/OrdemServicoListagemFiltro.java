package br.com.oficina.ordemservico.application;

import br.com.oficina.ordemservico.domain.StatusOrdemServico;

import java.time.OffsetDateTime;

/**
 * Critérios de listagem sem expor {@code Specification} JPA na camada de aplicação.
 */
public record OrdemServicoListagemFiltro(
        StatusOrdemServico status,
        String placaNormalized,
        String cpfCnpjDigits,
        OffsetDateTime from,
        OffsetDateTime to,
        boolean excluirEncerradas,
        OrdemServicoListagemOrdem ordem
) {
}
