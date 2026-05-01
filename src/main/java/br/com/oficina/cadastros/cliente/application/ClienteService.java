package br.com.oficina.cadastros.cliente.application;

import br.com.oficina.cadastros.cliente.domain.Cliente;
import br.com.oficina.cadastros.cliente.domain.CpfCnpj;
import br.com.oficina.cadastros.cliente.infra.persistence.ClienteEntityMapper;
import br.com.oficina.cadastros.cliente.infra.persistence.ClienteJpaRepository;
import br.com.oficina.cadastros.cliente.infra.persistence.entity.ClienteEntity;
import br.com.oficina.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteJpaRepository clienteRepository;
    private final ClienteEntityMapper mapper;

    public ClienteService(ClienteJpaRepository clienteRepository, ClienteEntityMapper mapper) {
        this.clienteRepository = clienteRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Cliente criar(String nome, String cpfCnpjRaw) {
        CpfCnpj cpfCnpj = CpfCnpj.of(cpfCnpjRaw);
        Cliente cliente = Cliente.novo(nome, cpfCnpj);
        ClienteEntity saved = clienteRepository.save(mapper.toNewEntity(cliente));
        return mapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    public Cliente obter(UUID id) {
        return clienteRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Transactional
    public Cliente atualizar(UUID id, String nome) {
        ClienteEntity e = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado"));
        Cliente d = mapper.toDomain(e);
        d.renomear(nome);
        mapper.aplicar(d, e);
        return mapper.toDomain(clienteRepository.save(e));
    }

    @Transactional
    public void remover(UUID id) {
        ClienteEntity e = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado"));
        clienteRepository.delete(e);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpjRaw) {
        String digits = br.com.oficina.shared.domain.Strings.onlyDigits(cpfCnpjRaw);
        return clienteRepository.findByCpfCnpj(digits).map(mapper::toDomain);
    }

    @Transactional
    public Cliente obterOuCriarPorCpfCnpj(String nomeSeNovo, String cpfCnpjRaw) {
        CpfCnpj cpfCnpj = CpfCnpj.of(cpfCnpjRaw);
        return clienteRepository.findByCpfCnpj(cpfCnpj.value())
                .map(mapper::toDomain)
                .orElseGet(() -> mapper.toDomain(clienteRepository.save(mapper.toNewEntity(Cliente.novo(nomeSeNovo, cpfCnpj)))));
    }
}
