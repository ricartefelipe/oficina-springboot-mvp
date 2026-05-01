package br.com.oficina.ordemservico.adapters.out.persistence;

import br.com.oficina.ordemservico.adapters.out.persistence.entity.OsOrcamentoRespostaIdempotenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OsOrcamentoRespostaIdempotenciaRepository extends JpaRepository<OsOrcamentoRespostaIdempotenciaEntity, UUID> {

    Optional<OsOrcamentoRespostaIdempotenciaEntity> findByIdempotencyKey(String idempotencyKey);
}
