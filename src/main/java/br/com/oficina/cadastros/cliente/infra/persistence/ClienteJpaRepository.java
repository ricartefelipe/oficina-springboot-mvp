package br.com.oficina.cadastros.cliente.infra.persistence;

import br.com.oficina.cadastros.cliente.infra.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, UUID> {

    Optional<ClienteEntity> findByCpfCnpj(String cpfCnpj);
}
