package br.com.oficina.cadastros.veiculo.infra.persistence;

import br.com.oficina.cadastros.cliente.domain.Cliente;
import br.com.oficina.cadastros.cliente.infra.persistence.ClienteEntityMapper;
import br.com.oficina.cadastros.cliente.infra.persistence.entity.ClienteEntity;
import br.com.oficina.cadastros.veiculo.domain.Placa;
import br.com.oficina.cadastros.veiculo.domain.Veiculo;
import br.com.oficina.cadastros.veiculo.infra.persistence.entity.VeiculoEntity;
import org.springframework.stereotype.Component;

@Component
public class VeiculoEntityMapper {

    private final ClienteEntityMapper clienteMapper;

    public VeiculoEntityMapper(ClienteEntityMapper clienteMapper) {
        this.clienteMapper = clienteMapper;
    }

    public Veiculo toDomain(VeiculoEntity e) {
        if (e == null) {
            return null;
        }
        Cliente cliente = clienteMapper.toDomain(e.getCliente());
        return Veiculo.restaurar(
                e.getId(),
                Placa.ofStored(e.getPlaca()),
                e.getMarca(),
                e.getModelo(),
                e.getAno(),
                cliente,
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public VeiculoEntity toNewEntity(Veiculo v, ClienteEntity clienteEntity) {
        VeiculoEntity e = new VeiculoEntity();
        e.setCliente(clienteEntity);
        aplicar(v, e);
        return e;
    }

    public void aplicar(Veiculo v, VeiculoEntity e) {
        e.setPlaca(v.getPlaca().value());
        e.setMarca(v.getMarca());
        e.setModelo(v.getModelo());
        e.setAno(v.getAno());
    }
}
