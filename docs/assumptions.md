# Assumptions (MVP - Fase 1)

Este projeto e o MVP back-end monolitico do sistema de oficina.

## Credenciais e ambiente local (somente DEV)
Para garantir execucao local simples via docker-compose, usamos credenciais fixas no compose:

- Postgres (app):
  - db: `oficina`
  - user: `oficina`
  - password: `oficina`
  - porta no host: `5433`

- Keycloak:
  - Console Admin (para acessar /admin do Keycloak):
    - user: `kcadmin`
    - password: `kcadmin`
  - Realm: `oficina`
  - Usuario do realm (para obter JWT e chamar endpoints admin futuramente):
    - user: `admin`
    - password: `admin`
    - role (realm): `ADMIN`

> Seguranca: essas credenciais sao apenas para execucao local e demonstracao do MVP. Nao sao adequadas para producao.

## Base path /api
Todos os endpoints REST do sistema estao sob `/api` (via `server.servlet.context-path`).

## JWT / Seguranca
- Nesta Parte 1, a aplicacao ainda nao exige JWT (integracao efetiva entra na Parte 4).
- O Keycloak e o realm ja sobem via docker-compose para garantir reprodutibilidade.

## Observabilidade minima
- Implementado `X-Correlation-Id` (gerado ou propagado) e inserido no MDC para logs.
- Em `prod` (docker) logs em JSON; em `dev` logs em texto.
