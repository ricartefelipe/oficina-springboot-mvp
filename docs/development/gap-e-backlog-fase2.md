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
- **Infra**: Dockerfile e docker-compose existentes; faltam manifestos Kubernetes dedicados, Terraform em `/infra` e pipeline CI/CD completa conforme enunciado

## Lacunas em relação à Fase 2

| Área | Lacuna |
|------|--------|
| Arquitetura | Organização ainda em camadas por contexto; é necessário evoluir para hexagonal explícita (domain, application, adapters, ports) sem quebrar comportamento |
| APIs | Listagem administrativa com ordenação por prioridade de status e exclusão lógica de finalizadas e entregues; endpoint de notificação externa de aprovação ou recusa com idempotência e segurança |
| Notificação | Adapter de e-mail desacoplado do domínio; MailHog ou equivalente no compose local |
| Testes | Ampliar integração de API, erros, transições inválidas, estoque e autenticação onde aplicável |
| DDD | Domain Storytelling visual, Event Storming visual final, diagrama de agregados, dicionário ubíquo reforçado com sinônimos rejeitados |
| Entrega | `/k8s`, `/infra` (Terraform), GitHub Actions com build, testes, imagem, deploy e smoke test; README alinhado à Fase 2 |

## Backlog fatiado (ordem sugerida)

1. **gitflow-bootstrap**: convenções de branch, template de PR, referência no repositório (esta entrega)
2. **hexagonal-structure**: reorganização incremental para domain, application, adapters e ports preservando testes
3. **os-abertura-contract-tests**: revisão de abertura de OS, identificador único e testes de contrato
4. **os-status-consulta**: consulta de status alinhada à máquina de estados e testes
5. **orcamento-notificacao-externa**: endpoint idempotente para aprovação ou recusa externa, histórico e validação de transição
6. **os-listagem-priorizada**: ordenação EM_EXECUCAO > AGUARDANDO_APROVACAO > EM_DIAGNOSTICO > RECEBIDA; mais antigas primeiro; excluir FINALIZADA e ENTREGUE
7. **notificacao-email-mailhog**: porta de notificação, adapter SMTP, eventos em transições relevantes, compose atualizado
8. **testes-ampliados**: happy path, erros, estoque, endpoints principais, idempotência
9. **kubernetes**: namespace, deployment, service, ConfigMap, Secret, probes, recursos, HPA, documentação de apply e rollback
10. **terraform**: módulos ou stacks reproduzíveis, documentação de apply e destroy
11. **cicd**: pipeline com jobs separados, cache, imagem, deploy e smoke
12. **ddd-visual-artifacts**: drawio, PlantUML ou Mermaid com export SVG ou PNG em `docs/ddd/`
13. **documentacao-readme-fase2**: README, arquitetura, execução local, deploy e links exigidos pelo enunciado

## Riscos e mitigação

- **Refatoração ampla**: fatiar por contexto ou por camada e manter testes verdes a cada passo
- **Pipeline e cluster**: quando não houver cluster remoto, documentar kind ou equivalente e limites do que a pipeline valida de ponta a ponta
