package br.com.oficina.cadastros.veiculo.application;

import br.com.oficina.cadastros.cliente.application.ClienteService;
import br.com.oficina.cadastros.cliente.domain.Cliente;
import br.com.oficina.cadastros.cliente.infra.persistence.ClienteJpaRepository;
import br.com.oficina.cadastros.cliente.infra.persistence.entity.ClienteEntity;
import br.com.oficina.cadastros.veiculo.domain.Placa;
import br.com.oficina.cadastros.veiculo.domain.Veiculo;
import br.com.oficina.cadastros.veiculo.infra.persistence.VeiculoEntityMapper;
import br.com.oficina.cadastros.veiculo.infra.persistence.VeiculoJpaRepository;
import br.com.oficina.cadastros.veiculo.infra.persistence.entity.VeiculoEntity;
import br.com.oficina.shared.domain.BusinessRuleException;
import br.com.oficina.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VeiculoService {

    private final VeiculoJpaRepository veiculoRepository;
    private final ClienteJpaRepository clienteRepository;
    private final ClienteService clienteService;
    private final VeiculoEntityMapper mapper;

    public VeiculoService(
            VeiculoJpaRepository veiculoRepository,
            ClienteJpaRepository clienteRepository,
            ClienteService clienteService,
            VeiculoEntityMapper mapper
    ) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
        this.mapper = mapper;
    }

    @Transactional
    public Veiculo criar(UUID clienteId, String placaRaw, String marca, String modelo, Integer ano) {
        Cliente cliente = clienteService.obter(clienteId);
        Veiculo v = Veiculo.novo(Placa.of(placaRaw), marca, modelo, ano, cliente);
        ClienteEntity ce = clienteRepository.getReferenceById(clienteId);
        return mapper.toDomain(veiculoRepository.save(mapper.toNewEntity(v, ce)));
    }

    @Transactional(readOnly = true)
    public Veiculo obter(UUID id) {
        return veiculoRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("Veiculo nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Veiculo> listar() {
        return veiculoRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Transactional
    public Veiculo atualizar(UUID id, String marca, String modelo, Integer ano) {
        VeiculoEntity e = veiculoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Veiculo nao encontrado"));
        Veiculo v = mapper.toDomain(e);
        v.atualizarDados(marca, modelo, ano);
        mapper.aplicar(v, e);
        return mapper.toDomain(veiculoRepository.save(e));
    }

    @Transactional
    public void remover(UUID id) {
        VeiculoEntity e = veiculoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Veiculo nao encontrado"));
        veiculoRepository.delete(e);
    }

    @Transactional(readOnly = true)
    public Optional<Veiculo> buscarPorPlaca(String placaRaw) {
        String normalized = br.com.oficina.shared.domain.Strings.alnumUpper(placaRaw);
        return veiculoRepository.findByPlaca(normalized).map(mapper::toDomain);
    }

    @Transactional
    public Veiculo obterOuCriarPorPlaca(Cliente cliente, String placaRaw, String marca, String modelo, Integer ano) {
        Placa placa = Placa.of(placaRaw);
        Optional<VeiculoEntity> existing = veiculoRepository.findByPlaca(placa.value());
        if (existing.isPresent()) {
            Veiculo v = mapper.toDomain(existing.get());
            if (!v.getCliente().getId().equals(cliente.getId())) {
                throw new BusinessRuleException("Placa ja cadastrada para outro cliente");
            }
            return v;
        }
        Veiculo novo = Veiculo.novo(placa, marca, modelo, ano, cliente);
        ClienteEntity ce = clienteRepository.getReferenceById(cliente.getId());
        return mapper.toDomain(veiculoRepository.save(mapper.toNewEntity(novo, ce)));
    }
}
