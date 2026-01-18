# Oficina Service (MVP - Fase 1) - Spring Boot

MVP back-end monolitico (arquitetura em camadas) do **Sistema Integrado de Atendimento e Execucao de Servicos** para uma oficina mecanica.

Este repositorio atende aos requisitos de:
- APIs REST documentadas (Swagger/OpenAPI)
- Persistencia em PostgreSQL
- Migrations via Liquibase (YAML)
- JWT para endpoints administrativos (Keycloak) — integracao ativada nas partes seguintes
- Dockerfile + docker-compose para execucao local simples
- Logs com correlation-id

## Stack
- Java 21
- Spring Boot 3
- PostgreSQL
- Liquibase (YAML)
- Swagger/OpenAPI (springdoc)
- Keycloak (para JWT/admin)

## Subir o ambiente local (Docker)
Requisito: Docker + Docker Compose v2

```bash
docker compose up --build
```

### URLs
- App (base): http://localhost:8080/api
- Health publico: http://localhost:8080/api/public/health
- Swagger UI: http://localhost:8080/api/swagger-ui
- OpenAPI JSON: http://localhost:8080/api/openapi
- Actuator health: http://localhost:8080/api/actuator/health
- Keycloak: http://localhost:8180

## Credenciais DEV
Estao documentadas em `/docs/assumptions.md`.

## Nota sobre JWT nesta Parte 1
Nesta Parte 1, a aplicacao ainda nao exige JWT (integracao efetiva entra na Parte 4).
O Keycloak e o realm ja sobem via docker-compose para garantir reprodutibilidade do setup.

## Como rodar testes (sem instalar Maven localmente)
Se voce quiser rodar testes usando apenas Docker:

```bash
docker run --rm -v "$PWD":/workspace -w /workspace maven:3.9.8-eclipse-temurin-21 mvn -q test
```

## Observabilidade (Correlation-Id)
- Header de entrada: `X-Correlation-Id` (opcional)
- Header de saida: `X-Correlation-Id`
- MDC: `correlationId`

## Proximas entregas (por partes)
- Parte 2: Dominio (DDD), VOs (CPF/CNPJ, Placa), regras, migrations reais e repositorios.
- Parte 3: Use cases + APIs (CRUDs + OS) + DTOs + validacao + swagger completo.
- Parte 4: Seguranca JWT (Keycloak), roles ADMIN e protecao dos endpoints /admin.
- Parte 5: Testes unitarios/integracao + cobertura >= 80% nos dominios criticos.
- Parte 6: Documentacao DDD completa e roteiro de video.
- Parte 7: Relatorio de vulnerabilidades + documento de entrega.

## Troubleshooting
- Se a porta 8080 ou 8180 estiver ocupada, ajuste o `docker-compose.yml`.
- Se quiser rodar o app fora do container (modo local), o Postgres do compose fica exposto em `localhost:5433`.
