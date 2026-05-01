package br.com.oficina.catalogo.peca.infra.persistence;

import br.com.oficina.catalogo.peca.domain.PecaInsumo;
import br.com.oficina.catalogo.peca.infra.persistence.entity.PecaInsumoEntity;
import org.springframework.stereotype.Component;

@Component
public class PecaInsumoEntityMapper {

    public PecaInsumo toDomain(PecaInsumoEntity e) {
        if (e == null) {
            return null;
        }
        return PecaInsumo.restaurar(
                e.getId(),
                e.getNome(),
                e.getDescricao(),
                e.getPreco(),
                e.getEstoqueAtual(),
                e.getEstoqueMinimo(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public PecaInsumoEntity toNewEntity(PecaInsumo p) {
        PecaInsumoEntity e = new PecaInsumoEntity();
        aplicar(p, e);
        return e;
    }

    public void aplicar(PecaInsumo p, PecaInsumoEntity e) {
        e.setNome(p.getNome());
        e.setDescricao(p.getDescricao());
        e.setPreco(p.getPreco());
        e.setEstoqueAtual(p.getEstoqueAtual());
        e.setEstoqueMinimo(p.getEstoqueMinimo());
    }
}
