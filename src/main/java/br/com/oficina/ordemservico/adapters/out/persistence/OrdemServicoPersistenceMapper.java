package br.com.oficina.ordemservico.adapters.out.persistence;

import br.com.oficina.cadastros.cliente.domain.Cliente;
import br.com.oficina.cadastros.cliente.infra.persistence.ClienteEntityMapper;
import br.com.oficina.cadastros.cliente.infra.persistence.entity.ClienteEntity;
import br.com.oficina.cadastros.veiculo.domain.Veiculo;
import br.com.oficina.cadastros.veiculo.infra.persistence.VeiculoEntityMapper;
import br.com.oficina.cadastros.veiculo.infra.persistence.entity.VeiculoEntity;
import br.com.oficina.catalogo.peca.domain.PecaInsumo;
import br.com.oficina.catalogo.peca.infra.persistence.PecaInsumoEntityMapper;
import br.com.oficina.catalogo.peca.infra.persistence.entity.PecaInsumoEntity;
import br.com.oficina.catalogo.servico.domain.ServicoCatalogo;
import br.com.oficina.catalogo.servico.infra.persistence.ServicoCatalogoEntityMapper;
import br.com.oficina.catalogo.servico.infra.persistence.entity.ServicoCatalogoEntity;
import br.com.oficina.ordemservico.adapters.out.persistence.entity.OrdemServicoEntity;
import br.com.oficina.ordemservico.adapters.out.persistence.entity.OrdemServicoItemPecaEntity;
import br.com.oficina.ordemservico.adapters.out.persistence.entity.OrdemServicoItemServicoEntity;
import br.com.oficina.ordemservico.adapters.out.persistence.entity.OrdemServicoTransicaoStatusEntity;
import br.com.oficina.ordemservico.domain.OrdemServico;
import br.com.oficina.ordemservico.domain.OrdemServicoItemPeca;
import br.com.oficina.ordemservico.domain.OrdemServicoItemServico;
import br.com.oficina.ordemservico.domain.OrdemServicoTransicaoStatus;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrdemServicoPersistenceMapper {

    private final ClienteEntityMapper clienteMapper;
    private final VeiculoEntityMapper veiculoMapper;
    private final ServicoCatalogoEntityMapper servicoCatalogoMapper;
    private final PecaInsumoEntityMapper pecaInsumoMapper;
    private final EntityManager entityManager;

    public OrdemServicoPersistenceMapper(
            ClienteEntityMapper clienteMapper,
            VeiculoEntityMapper veiculoMapper,
            ServicoCatalogoEntityMapper servicoCatalogoMapper,
            PecaInsumoEntityMapper pecaInsumoMapper,
            EntityManager entityManager
    ) {
        this.clienteMapper = clienteMapper;
        this.veiculoMapper = veiculoMapper;
        this.servicoCatalogoMapper = servicoCatalogoMapper;
        this.pecaInsumoMapper = pecaInsumoMapper;
        this.entityManager = entityManager;
    }

    public OrdemServico toDomain(OrdemServicoEntity e) {
        Cliente cliente = clienteMapper.toDomain(e.getCliente());
        Veiculo veiculo = veiculoMapper.toDomain(e.getVeiculo());

        List<OrdemServicoItemServico> itensServico = new ArrayList<>();
        for (OrdemServicoItemServicoEntity ie : e.getItensServico()) {
            ServicoCatalogo s = servicoCatalogoMapper.toDomain(ie.getServico());
            itensServico.add(OrdemServicoItemServico.restaurar(
                    ie.getId(),
                    s,
                    ie.getQuantidade(),
                    ie.getPrecoUnitario(),
                    ie.getTempoEstimadoMin(),
                    ie.getSubtotal()
            ));
        }

        List<OrdemServicoItemPeca> itensPeca = new ArrayList<>();
        for (OrdemServicoItemPecaEntity ie : e.getItensPeca()) {
            PecaInsumo p = pecaInsumoMapper.toDomain(ie.getPeca());
            itensPeca.add(OrdemServicoItemPeca.restaurar(
                    ie.getId(),
                    p,
                    ie.getQuantidade(),
                    ie.getPrecoUnitario(),
                    ie.getSubtotal()
            ));
        }

        List<OrdemServicoTransicaoStatus> transicoes = new ArrayList<>();
        for (OrdemServicoTransicaoStatusEntity te : e.getTransicoesStatus()) {
            transicoes.add(OrdemServicoTransicaoStatus.restaurar(
                    te.getId(),
                    te.getDeStatus(),
                    te.getParaStatus(),
                    te.getOcorridoEm()
            ));
        }

        return OrdemServico.restaurar(
                e.getId(),
                e.getTrackingCode(),
                cliente,
                veiculo,
                e.getStatus(),
                e.getOrcamentoTotal(),
                e.getOrcamentoEnviadoAt(),
                e.getAprovadoAt(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                itensServico,
                itensPeca,
                transicoes
        );
    }

    /**
     * Copia o estado do domínio para a entidade JPA gerenciada (criação ou atualização).
     */
    public void mergeDomainIntoEntity(OrdemServico domain, OrdemServicoEntity entity) {
        entity.setTrackingCode(domain.getTrackingCode());
        entity.setStatus(domain.getStatus());
        entity.setOrcamentoTotal(domain.getOrcamentoTotal());
        entity.setOrcamentoEnviadoAt(domain.getOrcamentoEnviadoAt());
        entity.setAprovadoAt(domain.getAprovadoAt());

        ClienteEntity clienteRef = entityManager.getReference(ClienteEntity.class, domain.getCliente().getId());
        VeiculoEntity veiculoRef = entityManager.getReference(VeiculoEntity.class, domain.getVeiculo().getId());
        entity.setCliente(clienteRef);
        entity.setVeiculo(veiculoRef);

        entity.getItensServico().clear();
        for (OrdemServicoItemServico i : domain.getItensServico()) {
            OrdemServicoItemServicoEntity ie = new OrdemServicoItemServicoEntity();
            ie.setOrdemServico(entity);
            ServicoCatalogoEntity se = entityManager.getReference(ServicoCatalogoEntity.class, i.getServico().getId());
            ie.setServico(se);
            ie.setQuantidade(i.getQuantidade());
            ie.setPrecoUnitario(i.getPrecoUnitario());
            ie.setTempoEstimadoMin(i.getTempoEstimadoMin());
            ie.setSubtotal(i.getSubtotal());
            entity.getItensServico().add(ie);
        }

        entity.getItensPeca().clear();
        for (OrdemServicoItemPeca i : domain.getItensPeca()) {
            OrdemServicoItemPecaEntity ie = new OrdemServicoItemPecaEntity();
            ie.setOrdemServico(entity);
            PecaInsumoEntity pe = entityManager.getReference(PecaInsumoEntity.class, i.getPeca().getId());
            ie.setPeca(pe);
            ie.setQuantidade(i.getQuantidade());
            ie.setPrecoUnitario(i.getPrecoUnitario());
            ie.setSubtotal(i.getSubtotal());
            entity.getItensPeca().add(ie);
        }

        entity.getTransicoesStatus().clear();
        for (OrdemServicoTransicaoStatus t : domain.getTransicoesStatus()) {
            OrdemServicoTransicaoStatusEntity te = new OrdemServicoTransicaoStatusEntity();
            te.setOrdemServico(entity);
            te.setDeStatus(t.getDeStatus());
            te.setParaStatus(t.getParaStatus());
            te.setOcorridoEm(t.getOcorridoEm());
            entity.getTransicoesStatus().add(te);
        }
    }
}
