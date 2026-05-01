package br.com.oficina.catalogo.servico.infra.persistence;

import br.com.oficina.catalogo.servico.infra.persistence.entity.ServicoCatalogoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServicoCatalogoJpaRepository extends JpaRepository<ServicoCatalogoEntity, UUID> {
}
