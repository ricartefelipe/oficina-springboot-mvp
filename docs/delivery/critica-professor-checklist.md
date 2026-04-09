# Checklist de atendimento à crítica do professor

Matriz objetiva para validar se os pontos do feedback da Fase 1 estão cobertos no repositório e o que ainda depende de ação manual para a entrega.

## Síntese do feedback

O feedback acadêmico reforçou, principalmente:

- necessidade de **diagramas visuais DDD** (não apenas texto),
- explicitação de **atores, comandos, eventos, políticas, modelos de leitura e fluxos alternativos**,
- inclusão do **diagrama de agregados** na versão final,
- melhoria de **clareza dos artefatos** para avaliação.

## Matriz crítica -> evidência

| Crítica / expectativa | Evidência no repositório | Status |
|---|---|---|
| Diagramas visuais DDD | `docs/ddd/diagrams/event-storming-contextos.svg`, `docs/ddd/diagrams/event-storming-lousa-elementos.svg`, `docs/ddd/diagrams/ordem-servico-agregado.svg` | Atendido |
| Event Storming com C/A/E/P/R + fluxos alternativos | `docs/ddd/event-storming.md` | Atendido |
| Domain Storytelling para narrativa de domínio | `docs/ddd/domain-storytelling.md` | Atendido |
| Dicionário de linguagem ubíqua (tabela) | `docs/ddd/dicionario-linguagem-ubiqua.md` | Atendido |
| Índice único de artefatos DDD | `docs/ddd/README.md` | Atendido |
| Evidência de testes e cobertura | Pipeline `mvn -B -Pci verify` em `.github/workflows/ci.yml` + JaCoCo | Atendido |
| Material pronto para submissão no portal | `docs/delivery/entrega-portal-fase2.md` e `docs/delivery/submission.md` | Parcial (depende de publicação/upload) |

## Pendências manuais (não automatizáveis no repositório)

- Publicar o **vídeo (<= 15 min)** e atualizar o URL no `README.md` e em `docs/delivery/entrega-portal-fase2.md`.
- Confirmar convite ao avaliador **`soat-architecture`** no repositório.
- Gerar/atualizar PDF final e subir no portal.

## Como usar esta checklist na entrega

1. Validar rapidamente cada arquivo listado na tabela.
2. Fechar as pendências manuais.
3. Anexar no PDF de entrega os links/evidências dos artefatos DDD e da execução técnica.
