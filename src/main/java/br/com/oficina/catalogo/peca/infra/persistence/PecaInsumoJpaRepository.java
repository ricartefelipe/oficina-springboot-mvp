package br.com.oficina.catalogo.peca.infra.persistence;

import br.com.oficina.catalogo.peca.infra.persistence.entity.PecaInsumoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

public interface PecaInsumoJpaRepository extends JpaRepository<PecaInsumoEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PecaInsumoEntity p where p.id = :id")
    Optional<PecaInsumoEntity> findByIdForUpdate(@Param("id") UUID id);
}
