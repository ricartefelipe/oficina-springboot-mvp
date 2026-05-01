package br.com.oficina.cadastros.cliente.infra.persistence;

import br.com.oficina.cadastros.cliente.domain.Cliente;
import br.com.oficina.cadastros.cliente.domain.CpfCnpj;
import br.com.oficina.cadastros.cliente.infra.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteEntityMapper {

    public Cliente toDomain(ClienteEntity e) {
        if (e == null) {
            return null;
        }
        return Cliente.restaurar(
                e.getId(),
                e.getNome(),
                CpfCnpj.ofDigits(e.getCpfCnpj()),
                e.getStatus(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public ClienteEntity toNewEntity(Cliente cliente) {
        ClienteEntity e = new ClienteEntity();
        aplicar(cliente, e);
        return e;
    }

    public void aplicar(Cliente cliente, ClienteEntity e) {
        e.setNome(cliente.getNome());
        e.setCpfCnpj(cliente.getCpfCnpj().value());
        e.setStatus(cliente.getStatus());
    }
}
