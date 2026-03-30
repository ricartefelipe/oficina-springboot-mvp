# Tech Challenge — Fase 3 — Documento para entrega no portal (PDF)

**Uso:** converte este ficheiro para PDF (ver secção [Como gerar o PDF](#como-gerar-o-pdf)) e faz upload no portal do aluno, conforme o enunciário da disciplina.

---

## 1. Identificação

| Campo | Valor |
|-------|--------|
| **Aluno** | *(preenche: nome completo)* |
| **Disciplina / turma** | *(preenche)* |
| **Modalidade** | Trabalho **individual** (sem equipa) |

---

## 2. Links dos quatro repositórios (Fase 3)

Todos **privados**, com acesso de leitura ao utilizador **`soat-architecture`** (GitHub).

| # | Repositório | Descrição breve |
|---|-------------|-----------------|
| 1 | https://github.com/ricartefelipe/oficina-auth-lambda | Função serverless (Python) — validação CPF e JWT |
| 2 | https://github.com/ricartefelipe/oficina-infra-database | Terraform — VPC, RDS opcional, CI |
| 3 | https://github.com/ricartefelipe/oficina-infra-kubernetes- | Terraform — cluster Kind (lab) + CI |
| 4 | https://github.com/ricartefelipe/oficina-app | Aplicação Spring Boot — container, K8s, CI/CD |

**Monólito / documentação central (evolução Fase 1–2 e referência técnica):**  
https://github.com/ricartefelipe/oficina-springboot-mvp (branch `develop`)

---

## 3. Confirmação — utilizador `soat-architecture`

- **O utilizador `soat-architecture` foi convidado** (ou equipa equivalente) em **todos** os repositórios listados na tabela acima, com permissão de **leitura** (ou conforme instrução do professor).
- **Como verificar:** em cada repositório → **Settings** → **Collaborators** / **Manage access** → confirmar presença de `soat-architecture`.

---

## 4. Componente vídeo (não aplicável nesta entrega)

O enunciado da Fase 3 prevê vídeo de **até 15 minutos**. **Nesta entrega opta-se por não incluir vídeo**, conforme orientação do aluno.  
**Evidências substitutas** para demonstração de funcionamento e infraestrutura encontram-se na **secção 6** (links de CI, Terraform e observabilidade).

*(Se o professor exigir vídeo obrigatório, grava um único ficheiro e cola o link aqui.)*

---

## 5. Links da documentação técnica

| Documento | URL (GitHub) |
|-----------|----------------|
| Índice Fase 3 (diagramas, backlog, execução) | `https://github.com/ricartefelipe/oficina-springboot-mvp/tree/develop/docs/fase3` |
| ADRs | `https://github.com/ricartefelipe/oficina-springboot-mvp/tree/develop/docs/adr` |
| RFC autenticação CPF / JWT | `https://github.com/ricartefelipe/oficina-springboot-mvp/blob/develop/docs/fase3/rfc/rfc-0001-autenticacao-cpf-jwt-serverless.md` |
| Padrões de arquitetura (ArchUnit, camadas) | `https://github.com/ricartefelipe/oficina-springboot-mvp/blob/develop/docs/development/architecture-standards.md` |
| Guia “terminar Fase 3” | `https://github.com/ricartefelipe/oficina-springboot-mvp/blob/develop/docs/fase3/TERMINAR-FASE3.md` |
| Snippet Terraform API Gateway + Lambda | `https://github.com/ricartefelipe/oficina-springboot-mvp/blob/develop/docs/fase3/terraform-snippet-api-gateway.tf` |

---

## 6. Demonstração da infraestrutura **sem vídeo** (modo individual)

Substituição aceitável: **prova documental** com links e, opcionalmente, **capturas de ecrã** anexadas ao PDF.

### 6.1 CI/CD (GitHub Actions)

| Repositório | O que demonstra | Evidência (link para run com sucesso) |
|-------------|-----------------|----------------------------------------|
| oficina-infra-database | Terraform validate / plan (AWS) | *(cola o URL do último run verde, ex.: Actions → Terraform AWS ou Terraform validate)* |
| oficina-app | Build, testes, imagem Docker | *(cola URL do run)* |
| oficina-auth-lambda | CI Python | *(cola URL do run)* |
| oficina-infra-kubernetes- | Terraform validate | *(cola URL do run)* |

### 6.2 Infraestrutura como código

- **Base de dados (RDS):** código Terraform em `oficina-infra-database`; execução de **`terraform plan`** (e opcionalmente `apply`) na região escolhida — indicar estado: *plan OK / apply aplicado* e **região** (ex.: `sa-east-1`).
- **Kubernetes:** manifestos no repositório da app (`k8s/`); cluster de laboratório **Kind** ou outro, conforme README do repositório de infra.
- **API Gateway:** snippet Terraform em `docs/fase3/terraform-snippet-api-gateway.tf` no monólito; deploy na AWS é **opcional** se o custo for limitante — descrever **estado** (ex.: *snippet integrado na stack / pendente apply*).

### 6.3 Observabilidade

- **Métricas Prometheus** e métricas de negócio documentadas em `docs/fase3/observabilidade-prometheus.md`.
- **Logs JSON** (perfil `k8s`) e **correlation id** — referência em `architecture-standards` e código.
- *(Opcional)* anexar **1–2 capturas** de ecrã: Grafana, Prometheus ou consola AWS, se existirem.

### 6.4 Autenticação CPF → Lambda → JWT → API

- Fluxo descrito na **RFC** e no código (`auth-lambda`, `ClienteSessaoController`, configuração JWT no Spring Boot).
- **Prova textual:** descrever **um** fluxo de teste manual (Postman/curl) ou **teste automatizado** referenciado no repositório.

---

## 7. Mapeamento rápido — requisitos Fase 3 (enunciado)

| Requisito | Onde está coberto |
|-----------|-------------------|
| 4 repositórios + CI/CD | Secção 2 + 6.1 |
| API Gateway + Lambda | Código + snippet Terraform; deploy AWS conforme secção 6 |
| BD gerenciado + K8s (Terraform) | Repositórios `oficina-infra-database` e `oficina-infra-kubernetes-` |
| Observabilidade | Métricas, logs, docs; secção 6.3 |
| Documentação (diagramas, RFC, ADR) | Secção 5 |
| `soat-architecture` | Secção 3 |

---

## Como gerar o PDF

1. **Abre este ficheiro** no GitHub (visualização) ou no VS Code / Cursor.
2. **Copia** para Word ou Google Docs, ajusta tabelas se necessário, **preenche** campos *(preenche)* e **cola** os links reais dos runs do GitHub Actions.
3. **Exporta** como PDF (**Ficheiro → Imprimir → Guardar como PDF** ou **Ficheiro → Transferir → PDF**).
4. Alternativa: instala [Pandoc](https://pandoc.org) e, na pasta do ficheiro:  
   `pandoc entrega-portal-fase3.md -o entrega-portal-fase3.pdf`

---

*Documento gerado para suportar a entrega da Fase 3 em modo individual, com evidências substitutas ao vídeo.*
