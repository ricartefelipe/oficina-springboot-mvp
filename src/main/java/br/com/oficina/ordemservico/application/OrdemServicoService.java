package br.com.oficina.ordemservico.application;

import br.com.oficina.cadastros.cliente.application.ClienteService;
import br.com.oficina.cadastros.cliente.domain.Cliente;
import br.com.oficina.cadastros.cliente.domain.CpfCnpj;
import br.com.oficina.cadastros.veiculo.application.VeiculoService;
import br.com.oficina.cadastros.veiculo.domain.Veiculo;
import br.com.oficina.catalogo.peca.domain.PecaInsumo;
import br.com.oficina.catalogo.peca.infra.persistence.PecaInsumoJpaRepository;
import br.com.oficina.catalogo.servico.application.ServicoCatalogoService;
import br.com.oficina.catalogo.servico.domain.ServicoCatalogo;
import br.com.oficina.ordemservico.domain.OrdemServico;
import br.com.oficina.ordemservico.domain.TrackingCodeGenerator;
import br.com.oficina.ordemservico.infra.persistence.OrdemServicoJpaRepository;
import br.com.oficina.shared.domain.BusinessRuleException;
import br.com.oficina.shared.domain.NotFoundException;
import br.com.oficina.shared.domain.Strings;
import br.com.oficina.shared.domain.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrdemServicoService {

    private static final Logger log = LoggerFactory.getLogger(OrdemServicoService.class);

    private final OrdemServicoJpaRepository osRepository;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;
    private final ServicoCatalogoService servicoCatalogoService;
    private final PecaInsumoJpaRepository pecaRepository;

    public OrdemServicoService(
            OrdemServicoJpaRepository osRepository,
            ClienteService clienteService,
            VeiculoService veiculoService,
            ServicoCatalogoService servicoCatalogoService,
            PecaInsumoJpaRepository pecaRepository
    ) {
        this.osRepository = osRepository;
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        this.servicoCatalogoService = servicoCatalogoService;
        this.pecaRepository = pecaRepository;
    }

    @Transactional
    public OrdemServico criarOrdemServico(
            String clienteNome,
            String clienteCpfCnpj,
            String placa,
            String marca,
            String modelo,
            Integer ano,
            List<ItemServico> servicos,
            List<ItemPeca> pecas
    ) {
        if (servicos == null || servicos.isEmpty()) {
            throw new ValidationException("Deve informar ao menos 1 servico");
        }

        Cliente cliente = clienteService.obterOuCriarPorCpfCnpj(clienteNome, clienteCpfCnpj);
        Veiculo veiculo = veiculoService.obterOuCriarPorPlaca(cliente, placa, marca, modelo, ano);

        String trackingCode = TrackingCodeGenerator.generate(12);
        OrdemServico os = OrdemServico.receber(cliente, veiculo, trackingCode);

        for (ItemServico i : servicos) {
            Objects.requireNonNull(i, "itemServico nao pode ser null");
            ServicoCatalogo s = servicoCatalogoService.obter(i.servicoId());
            os.adicionarServico(s, i.quantidade());
        }

        if (pecas != null) {
            for (ItemPeca i : pecas) {
                Objects.requireNonNull(i, "itemPeca nao pode ser null");
                PecaInsumo p = pecaRepository.findById(i.pecaId())
                        .orElseThrow(() -> new NotFoundException("Peca/Insumo nao encontrado"));
                os.adicionarPeca(p, i.quantidade());
            }
        }

        OrdemServico saved = osRepository.save(os);
        log.info("os_criada osId={} trackingCode={} status={} orcamentoTotal={}", saved.getId(), saved.getTrackingCode(), saved.getStatus(), saved.getOrcamentoTotal());
        return saved;
    }

    @Transactional(readOnly = true)
    public OrdemServico obterDetalhe(UUID id) {
        return osRepository.findDetailedById(id)
                .orElseThrow(() -> new NotFoundException("Ordem de Servico nao encontrada"));
    }

    @Transactional(readOnly = true)
    public OrdemServico obterPorTrackingCode(String trackingCode) {
        String tc = Strings.requireNonBlank(trackingCode, "trackingCode").trim().toUpperCase();
        return osRepository.findDetailedByTrackingCode(tc)
                .orElseThrow(() -> new NotFoundException("Ordem de Servico nao encontrada"));
    }

    @Transactional(readOnly = true)
    public List<OrdemServico> listar(
            br.com.oficina.ordemservico.domain.StatusOrdemServico status,
            String placa,
            String cpfCnpj,
            OffsetDateTime from,
            OffsetDateTime to
    ) {
        String placaNormalized = (placa == null || placa.isBlank()) ? null : Strings.alnumUpper(placa);
        String cpfDigits = (cpfCnpj == null || cpfCnpj.isBlank()) ? null : Strings.onlyDigits(cpfCnpj);

        Specification<OrdemServico> spec = OrdemServicoSpecifications.filtrar(status, placaNormalized, cpfDigits, from, to);
        return osRepository.findAll(spec);
    }

    @Transactional
    public OrdemServico iniciarDiagnostico(UUID osId) {
        OrdemServico os = obterDetalhe(osId);
        os.iniciarDiagnostico();
        OrdemServico saved = osRepository.save(os);
        log.info("os_status_alterado osId={} status={}", saved.getId(), saved.getStatus());
        return saved;
    }

    @Transactional
    public OrdemServico enviarOrcamento(UUID osId) {
        OrdemServico os = obterDetalhe(osId);
        os.enviarOrcamento();
        OrdemServico saved = osRepository.save(os);

        // Simulacao de envio no MVP
        log.info("orcamento_enviado osId={} trackingCode={} orcamentoTotal={}", saved.getId(), saved.getTrackingCode(), saved.getOrcamentoTotal());
        return saved;
    }

    @Transactional
    public OrdemServico finalizarExecucao(UUID osId) {
        OrdemServico os = obterDetalhe(osId);
        os.finalizarExecucao();
        OrdemServico saved = osRepository.save(os);
        log.info("os_status_alterado osId={} status={}", saved.getId(), saved.getStatus());
        return saved;
    }

    @Transactional
    public OrdemServico registrarEntrega(UUID osId) {
        OrdemServico os = obterDetalhe(osId);
        os.registrarEntrega();
        OrdemServico saved = osRepository.save(os);
        log.info("os_status_alterado osId={} status={}", saved.getId(), saved.getStatus());
        return saved;
    }

    /**
     * Aprovacao pelo cliente (public). Regra: ao entrar EM_EXECUCAO, decrementar estoque das pecas.
     */
    @Transactional
    public OrdemServico aprovarOrcamentoPublico(String trackingCode, String cpfCnpjRaw) {
        OrdemServico os = obterPorTrackingCode(trackingCode);

        // Validacao adicional: CPF/CNPJ deve casar com o cliente da OS
        CpfCnpj informado = CpfCnpj.of(cpfCnpjRaw);
        String esperado = os.getCliente().getCpfCnpj().value();
        if (!esperado.equals(informado.value())) {
            throw new BusinessRuleException("CPF/CNPJ nao confere para esta Ordem de Servico");
        }

        // Decremento de estoque: agrupa por peca para evitar double-decrement em duplicidades
        Map<UUID, Integer> qtyByPeca = new HashMap<>();
        for (var item : os.getItensPeca()) {
            UUID pecaId = item.getPeca().getId();
            if (pecaId == null) {
                throw new BusinessRuleException("Item de peca sem identificador");
            }
            qtyByPeca.merge(pecaId, item.getQuantidade(), Integer::sum);
        }

        // Lock pessimista para evitar corrida em estoque
        for (Map.Entry<UUID, Integer> e : qtyByPeca.entrySet()) {
            UUID pecaId = e.getKey();
            int qtd = e.getValue();
            PecaInsumo peca = pecaRepository.findByIdForUpdate(pecaId)
                    .orElseThrow(() -> new NotFoundException("Peca/Insumo nao encontrado"));
            peca.decrementarEstoque(qtd);
        }

        os.aprovarOrcamento();
        OrdemServico saved = osRepository.save(os);
        log.info("orcamento_aprovado osId={} trackingCode={} status={}", saved.getId(), saved.getTrackingCode(), saved.getStatus());
        return saved;
    }

    public record ItemServico(UUID servicoId, int quantidade) {
    }

    public record ItemPeca(UUID pecaId, int quantidade) {
    }
}
