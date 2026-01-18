# Oficina Service (MVP - Fase 1) - Spring Boot

MVP back-end monolitico (arquitetura em camadas) do **Sistema Integrado de Atendimento e Execucao de Servicos** para uma oficina mecanica.

Este repositorio atende aos requisitos de:
- APIs REST documentadas (Swagger/OpenAPI)
- Persistencia em PostgreSQL
- Migrations via Liquibase (YAML)
- CRUDs administrativos: clientes, veiculos, servicos, pecas/insumos (com estoque)
- Gestao de Ordens de Servico (OS): criacao, acompanhamento por status, orcamento automatico, envio para aprovacao, aprovacao do cliente, finalizacao e entrega
- Consulta publica do cliente por trackingCode
- Metricas: tempo medio de execucao (EM_EXECUCAO -> FINALIZADA)
- Tratamento de erros padronizado (Problem Details) com correlation-id
- Dockerfile + docker-compose para execucao local simples
- Logs com correlation-id
- **Seguranca: JWT (Keycloak) para endpoints administrativos**

## Stack
- Java 21
- Spring Boot 3
- Spring MVC + Jackson
- Spring Security (OAuth2 Resource Server / JWT)
- PostgreSQL
- Liquibase (YAML)
- Swagger/OpenAPI (springdoc)
- Keycloak (emissor JWT/admin)

## Subir o ambiente local (Docker)
Requisito: Docker + Docker Compose v2

```bash
docker compose up --build
```

### Seed minima
Ao subir pela primeira vez, o Liquibase cria o schema e aplica seed minima de **servicos** e **pecas/insumos** (catalogos) para demonstracao.

IDs seed (fixos) estao em `/docs/assumptions.md`.

### URLs
- App (base): http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui
- OpenAPI JSON: http://localhost:8080/api/openapi
- Actuator health: http://localhost:8080/api/actuator/health
- Keycloak: http://localhost:8180

## Seguranca (JWT) - Admin

### Regras
- **Publico (cliente):** `/api/public/**` (nao exige JWT)
- **Administrativo:** `/api/admin/**` exige **JWT valido** e role **ADMIN**
- **Negar por padrao:** qualquer outra rota nao listada acima e negada (HTTP 403/401 conforme o caso)

### Credenciais DEV (Keycloak)
Estao em `/docs/assumptions.md`.

### Obter token ADMIN (script)
1) Garanta que o `docker compose up` esteja rodando.
2) Em outro terminal:

```bash
./scripts/get-admin-token.sh
```

Para salvar em variavel:

```bash
export TOKEN="$(./scripts/get-admin-token.sh)"
```

### Obter token ADMIN (curl manual)
```bash
curl -sS -X POST "http://localhost:8180/realms/oficina/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=oficina-api" \
  -d "username=admin" \
  -d "password=admin"
```

### Chamar endpoint admin com JWT
```bash
export TOKEN="$(./scripts/get-admin-token.sh)"

curl -sS "http://localhost:8080/api/admin/clientes" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Accept: application/json"
```

> Observacao sobre issuer (Keycloak): dependendo de como o token foi obtido, o `iss` pode variar (ex.: `http://localhost:8180/...` vs `http://keycloak:8080/...`).
> Para manter o ambiente reprodutivel no docker-compose, o recurso-server valida contra uma **lista de issuers permitidos** (configuravel via `JWT_ALLOWED_ISSUERS`).

## Endpoints (Fase 1)
Base path: `/api`

### Admin (protegido por JWT/role ADMIN)
- Clientes:
  - `POST /admin/clientes`
  - `GET /admin/clientes`
  - `GET /admin/clientes/{id}`
  - `PUT /admin/clientes/{id}`
  - `DELETE /admin/clientes/{id}`

- Veiculos:
  - `POST /admin/veiculos`
  - `GET /admin/veiculos`
  - `GET /admin/veiculos/{id}`
  - `PUT /admin/veiculos/{id}`
  - `DELETE /admin/veiculos/{id}`

- Servicos (catalogo):
  - `POST /admin/servicos`
  - `GET /admin/servicos`
  - `GET /admin/servicos/{id}`
  - `PUT /admin/servicos/{id}`
  - `DELETE /admin/servicos/{id}`

- Pecas/Insumos (com estoque):
  - `POST /admin/pecas`
  - `GET /admin/pecas`
  - `GET /admin/pecas/{id}`
  - `PUT /admin/pecas/{id}`
  - `DELETE /admin/pecas/{id}`

- Ordens de Servico:
  - `POST /admin/ordens-servico` (cria OS completa: cliente + veiculo + itens)
  - `GET /admin/ordens-servico` (filtros: `status`, `placa`, `cpfCnpj`, `from`, `to`)
  - `GET /admin/ordens-servico/{id}`
  - `POST /admin/ordens-servico/{id}/diagnostico/iniciar`
  - `POST /admin/ordens-servico/{id}/orcamento/enviar`
  - `POST /admin/ordens-servico/{id}/execucao/finalizar`
  - `POST /admin/ordens-servico/{id}/entrega/registrar`

- Metricas:
  - `GET /admin/metricas/tempo-medio-execucao?from=&to=`

### Public (cliente)
- `GET /public/ordens-servico/{trackingCode}`
- `POST /public/ordens-servico/{trackingCode}/aprovar` (body: `{ "cpfCnpj": "..." }`)

## Exemplos de curl

### Criar OS (admin) com JWT
Usando seeds do catalogo (IDs em `/docs/assumptions.md`):

```bash
export TOKEN="$(./scripts/get-admin-token.sh)"

curl -sS -X POST "http://localhost:8080/api/admin/ordens-servico" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": {"nome": "Joao da Silva", "cpfCnpj": "39053344705"},
    "veiculo": {"placa": "ABC1D23", "marca": "VW", "modelo": "Gol", "ano": 2018},
    "servicos": [
      {"servicoId": "11111111-1111-1111-1111-111111111111", "quantidade": 1}
    ],
    "pecas": [
      {"pecaId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", "quantidade": 4}
    ]
  }'
```

### Fluxo de status (admin) com JWT
```bash
export TOKEN="$(./scripts/get-admin-token.sh)"

# iniciar diagnostico
curl -sS -X POST "http://localhost:8080/api/admin/ordens-servico/<OS_ID>/diagnostico/iniciar" \
  -H "Authorization: Bearer ${TOKEN}"

# enviar orcamento
curl -sS -X POST "http://localhost:8080/api/admin/ordens-servico/<OS_ID>/orcamento/enviar" \
  -H "Authorization: Bearer ${TOKEN}"

# finalizar execucao
curl -sS -X POST "http://localhost:8080/api/admin/ordens-servico/<OS_ID>/execucao/finalizar" \
  -H "Authorization: Bearer ${TOKEN}"

# registrar entrega
curl -sS -X POST "http://localhost:8080/api/admin/ordens-servico/<OS_ID>/entrega/registrar" \
  -H "Authorization: Bearer ${TOKEN}"
```

### Consulta publica (cliente)
```bash
curl -sS "http://localhost:8080/api/public/ordens-servico/<TRACKING_CODE>"
```

### Aprovar orcamento (cliente)
Ao aprovar, o sistema entra em `EM_EXECUCAO` e decrementa o estoque das pecas da OS (falha se insuficiente).

```bash
curl -sS -X POST "http://localhost:8080/api/public/ordens-servico/<TRACKING_CODE>/aprovar" \
  -H "Content-Type: application/json" \
  -d '{"cpfCnpj":"39053344705"}'
```

## Tratamento de erros
O sistema retorna erros no formato **Problem Details** (Spring `ProblemDetail`), incluindo:
- `correlationId` (para rastreabilidade)
- `path`
- detalhes de validacao quando aplicavel

Para autenticacao/autorizacao (401/403), o projeto retorna `application/problem+json` com `correlationId`.

## Como rodar testes (sem instalar Maven localmente)
### Pre-requisitos
- Para **testes unitarios e integracao**, o projeto usa **Testcontainers (PostgreSQL)**.
- Portanto, os testes exigem um **Docker daemon** acessivel (Docker Desktop ou Docker Engine).

### Rodar testes
```bash
mvn -q test
```

### Rodar com cobertura (JaCoCo) e validar minimo >= 80% nos dominios criticos
```bash
mvn -q verify
```

Relatorio JaCoCo (HTML): `target/site/jacoco/index.html`.

#### Regra de cobertura aplicada
A regra de cobertura minima (80%) e aplicada **somente** aos pacotes de dominio (`br/com/oficina/**/domain/**`).
Isso evita que camadas de API/infrastrutura (controllers, mapeamentos, configs) distorcam a medicao do core.

### Rodar Maven via Docker (opcional)
Se voce preferir rodar Maven dentro de um container, e necessario montar o socket do Docker para que o Testcontainers funcione:

```bash
docker run --rm \
  -v "$PWD":/workspace -w /workspace \
  -v /var/run/docker.sock:/var/run/docker.sock \
  maven:3.9.8-eclipse-temurin-21 mvn -q test
```

## Observabilidade (Correlation-Id)
- Header de entrada: `X-Correlation-Id` (opcional)
- Header de saida: `X-Correlation-Id`
- MDC: `correlationId`

## Relatorio de vulnerabilidades (scan)
Este repositorio inclui um fluxo reprodutivel para gerar o relatorio de vulnerabilidades exigido na Fase 1.

1) Executar:
```bash
./scripts/security/run-security-scans.sh
```

2) Evidencias geradas em:
- `build/security/dependency-check/*`
- `build/security/trivy-fs.*`
- `build/security/trivy-image.*`

3) Atualize o resumo em:
- `docs/security/vulnerability-report.md`

> Pre-requisito: Docker no host. Na primeira execucao, as ferramentas podem baixar bases/imagens.

## Documento de entrega (PDF)
O arquivo de submissao esta em `docs/delivery/submission.md`.

Para converter para PDF (Pandoc):
```bash
pandoc docs/delivery/submission.md -o docs/delivery/submission.pdf
```

(ou ver instrucoes completas no proprio arquivo).

## Entregas por partes
- Parte 1-4: Base do projeto, dominios, APIs e seguranca JWT (Keycloak)
- Parte 5: Testes unitarios/integracao + cobertura (JaCoCo) >= 80% nos dominios criticos
- Parte 6: Documentacao DDD completa em `/docs/ddd` e roteiro em `/docs/video-script.md`
- Parte 7: Relatorio de vulnerabilidades + documento de entrega (PDF)


## Troubleshooting
- Se a porta 8080 ou 8180 estiver ocupada, ajuste o `docker-compose.yml`.
- Se quiser rodar o app fora do container (modo local), o Postgres do compose fica exposto em `localhost:5433`.

## Banco e migrations
- PostgreSQL via docker-compose
- Liquibase (YAML) aplicado no startup
- Seed minima de catalogo (servicos/pecas) via migration (ver /docs/assumptions.md)
