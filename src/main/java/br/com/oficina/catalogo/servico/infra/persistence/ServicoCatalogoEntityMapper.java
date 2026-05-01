package br.com.oficina.catalogo.servico.infra.persistence;

import br.com.oficina.catalogo.servico.domain.ServicoCatalogo;
import br.com.oficina.catalogo.servico.infra.persistence.entity.ServicoCatalogoEntity;
import org.springframework.stereotype.Component;

@Component
public class ServicoCatalogoEntityMapper {

    public ServicoCatalogo toDomain(ServicoCatalogoEntity e) {
        if (e == null) {
            return null;
        }
        return ServicoCatalogo.restaurar(
                e.getId(),
                e.getNome(),
                e.getDescricao(),
                e.getPreco(),
                e.getTempoEstimadoMin(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public ServicoCatalogoEntity toNewEntity(ServicoCatalogo s) {
        ServicoCatalogoEntity e = new ServicoCatalogoEntity();
        aplicar(s, e);
        return e;
    }

    public void aplicar(ServicoCatalogo s, ServicoCatalogoEntity e) {
        e.setNome(s.getNome());
        e.setDescricao(s.getDescricao());
        e.setPreco(s.getPreco());
        e.setTempoEstimadoMin(s.getTempoEstimadoMin());
    }
}
