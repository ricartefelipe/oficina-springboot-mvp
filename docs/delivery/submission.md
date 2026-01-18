# Documento de Entrega - Tech Challenge (Fase 1)

> Formato: Markdown (converter para PDF no final, ver instrucoes ao fim).

## Nome do grupo
- **Grupo:** <NOME_DO_GRUPO>

## Participantes
Preencher com nome e username no Discord.

- <NOME> - Discord: <USERNAME>
- <NOME> - Discord: <USERNAME>
- <NOME> - Discord: <USERNAME>

## Link do repositorio (privado)
- <COLE_AQUI_O_LINK_DO_REPO_PRIVADO>

> Observacao do enunciado: o repositorio deve ser privado e compartilhado com o usuario `soat-architecture`.

## Link da documentacao DDD (Miro ou equivalente)
- <COLE_AQUI_O_LINK_DO_MIRO_OU_EQUIVALENTE>

### Documentacao DDD local (no repositorio)
Para conveniencia, esta entrega inclui a documentacao DDD em Markdown:
- `docs/ddd/event-storming.md`
- `docs/ddd/diagramas.md`
- `docs/ddd/ubiquitous-language.md`

## Relatorio de vulnerabilidades
- Arquivo no repositorio: `docs/security/vulnerability-report.md`
- Evidencias geradas (apos execucao local do scan): `build/security/*`

### Resumo (preencher apos executar o scan)
- Achados criticos: <NUMERO>
- Achados altos: <NUMERO>
- Plano de mitigacao: <RESUMO>

---

# Como converter este Markdown para PDF (offline)
A conversao para PDF nao depende de internet, desde que a ferramenta ja esteja instalada localmente.

## Opcao A: Pandoc instalado no host
```bash
pandoc docs/delivery/submission.md -o docs/delivery/submission.pdf
```

## Opcao B: Docker (se voce ja tiver a imagem do pandoc localmente)
> Esta opcao pode exigir o download da imagem na primeira execucao.

```bash
docker run --rm \
  -v "$PWD":/data \
  -w /data \
  pandoc/core:3.1 \
  docs/delivery/submission.md -o docs/delivery/submission.pdf
```
