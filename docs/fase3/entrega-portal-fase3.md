# Tech Challenge — Fase 3 — Documento para entrega no portal (PDF)

**Uso:** converte este ficheiro para PDF (ver secção [Como gerar o PDF](#como-gerar-o-pdf)) e faz upload no portal do aluno, conforme o enunciário da disciplina.

---

## 1. Identificação

| Campo | Valor |
|-------|--------|
| **Aluno** | *(preenche: nome completo no portal académico)* |
| **Conta GitHub (autor dos repositórios)** | `ricartefelipe` |
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

Links obtidos em **2026-03-30** (último run com `success` à data da conclusão do projeto). Se o Actions tiver runs mais recentes, podes atualizar na cópia do PDF.

| Repositório | O que demonstra | Evidência (link para run com sucesso) |
|-------------|-----------------|----------------------------------------|
| oficina-infra-database | Terraform AWS (plan) | https://github.com/ricartefelipe/oficina-infra-database/actions/runs/23729350036 |
| oficina-app | CI (build, testes, Docker) | https://github.com/ricartefelipe/oficina-app/actions/runs/23727418594 |
| oficina-auth-lambda | CI (Python) | https://github.com/ricartefelipe/oficina-auth-lambda/actions/runs/23727348096 |
| oficina-infra-kubernetes- | Terraform (Kind) | https://github.com/ricartefelipe/oficina-infra-kubernetes-/actions/runs/23727349224 |
| oficina-springboot-mvp | CI monólito (ArchUnit, testes) — referência | https://github.com/ricartefelipe/oficina-springboot-mvp/actions/runs/23729555436 |

### 6.2 Infraestrutura como código

- **Base de dados (RDS):** código Terraform em `oficina-infra-database`. O workflow **Terraform AWS** no GitHub Actions executa **`terraform plan`** com sucesso (evidência: link na tabela 6.1). **`apply`** na AWS é opcional por custo — se não aplicaste, mantém-se *plan validado em CI*.
- **Kubernetes:** Terraform **Kind** em `oficina-infra-kubernetes-` (run verde na 6.1); manifestos da app em `oficina-app` (`k8s/`).
- **API Gateway + Lambda:** código em `oficina-auth-lambda`; integração HTTP documentada — snippet em `docs/fase3/terraform-snippet-api-gateway.tf` no monólito. Deploy completo na conta AWS é **opcional** se o custo for limitante; o repositório e o CI cobrem a **definição** da infra e da função.

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

1. **Forma mais rápida:** no repositório existe **`entrega-portal-fase3.docx`** (gerado a partir deste `.md`). Abre no Word → **Ficheiro → Guardar como → PDF** (ou **Imprimir → Microsoft Print to PDF**).
2. **A partir do Markdown:** abre `entrega-portal-fase3.md` no GitHub ou no editor, **preenche** nome e turma na secção 1, depois cola no Word/Google Docs e exporta PDF.
3. **Pandoc para PDF** (requer motor PDF, ex. MiKTeX/pdflatex):  
   `pandoc entrega-portal-fase3.md -o entrega-portal-fase3.pdf`

---

*Documento gerado para suportar a entrega da Fase 3 em modo individual, com evidências substitutas ao vídeo.*
