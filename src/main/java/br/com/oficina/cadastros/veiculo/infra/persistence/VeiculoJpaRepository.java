package br.com.oficina.cadastros.veiculo.infra.persistence;

import br.com.oficina.cadastros.veiculo.infra.persistence.entity.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VeiculoJpaRepository extends JpaRepository<VeiculoEntity, UUID> {

    Optional<VeiculoEntity> findByPlaca(String placa);
}
