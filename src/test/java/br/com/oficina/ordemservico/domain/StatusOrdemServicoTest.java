package br.com.oficina.ordemservico.domain;

import br.com.oficina.shared.domain.BusinessRuleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusOrdemServicoTest {

    @Test
    void devePermitirTransicoesSequenciaisObrigatorias() {
        assertDoesNotThrow(() -> StatusOrdemServico.RECEBIDA.validarTransicaoPara(StatusOrdemServico.EM_DIAGNOSTICO));
        assertDoesNotThrow(() -> StatusOrdemServico.EM_DIAGNOSTICO.validarTransicaoPara(StatusOrdemServico.AGUARDANDO_APROVACAO));
        assertDoesNotThrow(() -> StatusOrdemServico.AGUARDANDO_APROVACAO.validarTransicaoPara(StatusOrdemServico.EM_EXECUCAO));
        assertDoesNotThrow(() -> StatusOrdemServico.EM_EXECUCAO.validarTransicaoPara(StatusOrdemServico.FINALIZADA));
        assertDoesNotThrow(() -> StatusOrdemServico.FINALIZADA.validarTransicaoPara(StatusOrdemServico.ENTREGUE));
    }

    @Test
    void deveRejeitarTransicaoInvalida() {
        assertThrows(BusinessRuleException.class, () -> StatusOrdemServico.RECEBIDA.validarTransicaoPara(StatusOrdemServico.ENTREGUE));
        assertThrows(BusinessRuleException.class, () -> StatusOrdemServico.AGUARDANDO_APROVACAO.validarTransicaoPara(StatusOrdemServico.FINALIZADA));
        assertThrows(BusinessRuleException.class, () -> StatusOrdemServico.ENTREGUE.validarTransicaoPara(StatusOrdemServico.RECEBIDA));
    }

    @Test
    void deveRejeitarMesmaTransicao() {
        assertThrows(BusinessRuleException.class, () -> StatusOrdemServico.RECEBIDA.validarTransicaoPara(StatusOrdemServico.RECEBIDA));
    }

    @Test
    void deveRejeitarNovoStatusNulo() {
        assertThrows(BusinessRuleException.class, () -> StatusOrdemServico.RECEBIDA.validarTransicaoPara(null));
    }
}
