package br.com.oficina.ordemservico.domain;

import br.com.oficina.cadastros.cliente.domain.Cliente;
import br.com.oficina.cadastros.veiculo.domain.Veiculo;
import br.com.oficina.catalogo.peca.domain.PecaInsumo;
import br.com.oficina.catalogo.servico.domain.ServicoCatalogo;
import br.com.oficina.shared.domain.BusinessRuleException;
import br.com.oficina.shared.domain.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrdemServico {

    private UUID id;
    private String trackingCode;
    private Cliente cliente;
    private Veiculo veiculo;
    private StatusOrdemServico status;
    private BigDecimal orcamentoTotal;
    private OffsetDateTime orcamentoEnviadoAt;
    private OffsetDateTime aprovadoAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<OrdemServicoItemServico> itensServico = new ArrayList<>();
    private List<OrdemServicoItemPeca> itensPeca = new ArrayList<>();
    private List<OrdemServicoTransicaoStatus> transicoesStatus = new ArrayList<>();

    private OrdemServico() {
    }

    private OrdemServico(String trackingCode, Cliente cliente, Veiculo veiculo) {
        this.trackingCode = trackingCode;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.status = StatusOrdemServico.RECEBIDA;
        this.orcamentoTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        transicoesStatus.add(OrdemServicoTransicaoStatus.criar(null, StatusOrdemServico.RECEBIDA, OffsetDateTime.now()));
    }

    /**
     * Factory: cria OS com status inicial RECEBIDA.
     */
    public static OrdemServico receber(Cliente cliente, Veiculo veiculo, String trackingCode) {
        Objects.requireNonNull(cliente, "cliente nao pode ser null");
        Objects.requireNonNull(veiculo, "veiculo nao pode ser null");
        if (trackingCode == null || trackingCode.trim().isBlank()) {
            throw new ValidationException("trackingCode nao pode ser vazio");
        }
        String normalized = trackingCode.trim().toUpperCase();
        if (normalized.length() < 8 || normalized.length() > 16) {
            throw new ValidationException("trackingCode deve ter entre 8 e 16 caracteres");
        }
        return new OrdemServico(normalized, cliente, veiculo);
    }

    public static OrdemServico restaurar(
            UUID id,
            String trackingCode,
            Cliente cliente,
            Veiculo veiculo,
            StatusOrdemServico status,
            BigDecimal orcamentoTotal,
            OffsetDateTime orcamentoEnviadoAt,
            OffsetDateTime aprovadoAt,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            List<OrdemServicoItemServico> itensServico,
            List<OrdemServicoItemPeca> itensPeca,
            List<OrdemServicoTransicaoStatus> transicoesStatus
    ) {
        Objects.requireNonNull(id, "id");
        OrdemServico os = new OrdemServico();
        os.id = id;
        os.trackingCode = trackingCode;
        os.cliente = cliente;
        os.veiculo = veiculo;
        os.status = status;
        os.orcamentoTotal = orcamentoTotal;
        os.orcamentoEnviadoAt = orcamentoEnviadoAt;
        os.aprovadoAt = aprovadoAt;
        os.createdAt = createdAt;
        os.updatedAt = updatedAt;
        os.itensServico = new ArrayList<>(itensServico);
        os.itensPeca = new ArrayList<>(itensPeca);
        os.transicoesStatus = new ArrayList<>(transicoesStatus);
        return os;
    }

    void definirId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public StatusOrdemServico getStatus() {
        return status;
    }

    public BigDecimal getOrcamentoTotal() {
        return orcamentoTotal;
    }

    public OffsetDateTime getOrcamentoEnviadoAt() {
        return orcamentoEnviadoAt;
    }

    public OffsetDateTime getAprovadoAt() {
        return aprovadoAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<OrdemServicoItemServico> getItensServico() {
        return Collections.unmodifiableList(itensServico);
    }

    public List<OrdemServicoItemPeca> getItensPeca() {
        return Collections.unmodifiableList(itensPeca);
    }

    public List<OrdemServicoTransicaoStatus> getTransicoesStatus() {
        return Collections.unmodifiableList(transicoesStatus);
    }

    public void adicionarServico(ServicoCatalogo servico, int quantidade) {
        Objects.requireNonNull(servico, "servico nao pode ser null");
        if (quantidade <= 0) {
            throw new ValidationException("quantidade deve ser > 0");
        }

        OrdemServicoItemServico item = OrdemServicoItemServico.criar(this, servico, quantidade);
        itensServico.add(item);
        recalcularOrcamento();
    }

    public void adicionarPeca(PecaInsumo peca, int quantidade) {
        Objects.requireNonNull(peca, "peca nao pode ser null");
        if (quantidade <= 0) {
            throw new ValidationException("quantidade deve ser > 0");
        }

        OrdemServicoItemPeca item = OrdemServicoItemPeca.criar(this, peca, quantidade);
        itensPeca.add(item);
        recalcularOrcamento();
    }

    public void iniciarDiagnostico() {
        transicionarPara(StatusOrdemServico.EM_DIAGNOSTICO, OffsetDateTime.now());
    }

    public void enviarOrcamento() {
        this.orcamentoEnviadoAt = OffsetDateTime.now();
        transicionarPara(StatusOrdemServico.AGUARDANDO_APROVACAO, this.orcamentoEnviadoAt);
    }

    public void aprovarOrcamento() {
        this.aprovadoAt = OffsetDateTime.now();
        transicionarPara(StatusOrdemServico.EM_EXECUCAO, this.aprovadoAt);
    }

    public void recusarOrcamento() {
        transicionarPara(StatusOrdemServico.CANCELADA, OffsetDateTime.now());
    }

    public void finalizarExecucao() {
        transicionarPara(StatusOrdemServico.FINALIZADA, OffsetDateTime.now());
    }

    public void registrarEntrega() {
        transicionarPara(StatusOrdemServico.ENTREGUE, OffsetDateTime.now());
    }

    private void transicionarPara(StatusOrdemServico novoStatus, OffsetDateTime quando) {
        if (status == null) {
            throw new BusinessRuleException("Status atual nao pode ser null");
        }
        status.validarTransicaoPara(novoStatus);
        StatusOrdemServico antigo = this.status;
        this.status = novoStatus;
        registrarTransicao(antigo, novoStatus, quando);
    }

    private void registrarTransicao(StatusOrdemServico de, StatusOrdemServico para, OffsetDateTime quando) {
        OrdemServicoTransicaoStatus t = OrdemServicoTransicaoStatus.criar(de, para, quando);
        transicoesStatus.add(t);
    }

    public void recalcularOrcamento() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrdemServicoItemServico i : itensServico) {
            total = total.add(i.getSubtotal());
        }
        for (OrdemServicoItemPeca i : itensPeca) {
            total = total.add(i.getSubtotal());
        }
        this.orcamentoTotal = total.setScale(2, RoundingMode.HALF_UP);
    }

    public List<OrdemServicoItemPeca> pecasNecessarias() {
        return getItensPeca();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdemServico that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
