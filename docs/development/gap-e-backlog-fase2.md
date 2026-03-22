# Diagnóstico de lacunas e backlog Fase 2

## Fontes

- Enunciado oficial da Fase 2 (requisitos de APIs, infraestrutura, entregáveis e critérios de avaliação)
- Feedback acadêmico da Fase 1 (reforço de DDD visual, testes e clareza de artefatos)
- Estado atual do repositório `oficina-springboot-mvp` (Java 21, Spring Boot, PostgreSQL, Liquibase, Keycloak, Docker)

## Situação atual resumida

- **Domínio e fluxo**: bounded contexts (`cadastros`, `catalogo`, `ordemservico`, `shared`); OS com máquina de estados, histórico, orçamento automático, tracking code, baixa de estoque na execução, métricas
- **Segurança**: JWT com Keycloak para admin; rotas públicas para consulta e aprovação com validações
- **Persistência**: JPA + Liquibase; repositórios por contexto
- **Testes**: testes de domínio e integração presentes; cobertura ainda abaixo do desejado para Fase 2 em endpoints e erros
- **Documentação DDD**: linguagem ubíqua e event storming em Markdown; faltam artefatos visuais versionáveis finais (SVG ou PNG) e Domain Storytelling visual
- **Infra**: Dockerfile e docker-compose; `/k8s`; Terraform **rede + RDS opcional**; CI Maven + Terraform validate + GHCR; **workflows manuais** Deploy Kubernetes e Terraform AWS (RDS com `enable_rds`); EKS não versionado
- **README**: alinhado aos entregáveis da Fase 2 (objetivos, arquitetura, fluxo de deploy, links Swagger/OpenAPI; vídeo a publicar)

## Lacunas em relação à Fase 2

| Área | Lacuna / próximo passo |
|------|-------------------------|
| Arquitetura | Hexagonal em evolução nos bounded contexts; continuar extraindo portas e adaptadores onde fizer sentido |
| Testes | Ampliar integração de API, erros, estoque e autenticação onde o enunciado exigir reforço |
| DDD | Artefatos **visuais** versionáveis (SVG/PNG) em `docs/ddd/` — ver item 12 |
| CI/CD (enunciado) | **Apply** YAML + rollout + smoke no workflow Deploy Kubernetes; **RDS** via workflow Terraform AWS ou CLI (não no push automático); **EKS** fora do repositório |
| Entrega académica | PDF no portal, vídeo (≤15 min), partilha do repo com **soat-architecture**, diagrama no PDF |

## Backlog fatiado (ordem sugerida)

1. **gitflow-bootstrap**: convenções de branch, template de PR, referência no repositório (esta entrega) — feito
2. **hexagonal-structure**: reorganização incremental para domain, application, adapters e ports preservando testes — feito
3. **os-abertura-contract-tests**: revisão de abertura de OS, identificador único e testes de contrato — feito
4. **os-status-consulta**: consulta de status alinhada à máquina de estados e testes — feito
5. **orcamento-notificacao-externa**: endpoint idempotente para aprovação ou recusa externa, histórico e validação de transição — feito (`POST /admin/ordens-servico/{id}/orcamento/resposta-externa`, status `CANCELADA` na recusa)
6. **os-listagem-priorizada**: ordenação EM_EXECUCAO > AGUARDANDO_APROVACAO > EM_DIAGNOSTICO > RECEBIDA; mais antigas primeiro; excluir FINALIZADA e ENTREGUE — feito (exclui também `CANCELADA`; parâmetro `incluirEncerradas`)
7. **notificacao-email-mailhog**: porta de notificação, adapter SMTP, eventos em transições relevantes, compose atualizado — feito (`NotificacaoOrdemServicoPort`, SMTP/MailHog, `NOTIFICATION_ENABLED`, `docker-compose` com MailHog)
8. **testes-ampliados**: happy path, erros, estoque, endpoints principais, idempotência — em progresso (HTTP 404/409 público; validações admin; **JWT 401/403/200** em `AdminJwtSecurityWebMvcTest` no perfil `ci`; integração Docker/Testcontainers opcional local)
9. **kubernetes**: namespace, deployment, service, ConfigMap, Secret, probes, recursos, HPA, documentação de apply e rollback — feito (`k8s/`, `k8s/README.md`)
10. **terraform**: módulos ou stacks reproduzíveis, documentação de apply e destroy — feito (`infra/`: rede AWS + **RDS opcional** `enable_rds`, `infra/README.md`, `infra/docs/terraform-vs-enunciado.md`)
11. **cicd**: pipeline com jobs separados, cache, imagem, deploy e smoke — GitHub Actions: `mvn -Pci verify` + **Terraform** `fmt`/`validate` + imagem **GHCR**; **Deploy Kubernetes** (rollout + smoke); **Terraform AWS** (`plan`/`apply`, RDS opcional); EKS/cluster gerido fora do repo
12. **ddd-visual-artifacts**: drawio, PlantUML ou Mermaid com export SVG ou PNG em `docs/ddd/` — feito SVG (`docs/ddd/diagrams/*.svg`); Mermaid mantido em `diagramas.md`
13. **documentacao-readme-fase2**: README, arquitetura, execução local, deploy e links exigidos pelo enunciado — feito (README raiz); **link do vídeo** a preencher após publicação

## Riscos e mitigação

- **Refatoração ampla**: fatiar por contexto ou por camada e manter testes verdes a cada passo
- **Pipeline e cluster**: quando não houver cluster remoto, documentar kind ou equivalente e limites do que a pipeline valida de ponta a ponta
