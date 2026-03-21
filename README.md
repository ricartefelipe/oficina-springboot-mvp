# Oficina Service — Tech Challenge (Fase 1) — MVP Back-end (Spring Boot)

MVP do **back-end monolítico** (arquitetura em camadas) do *Sistema Integrado de Atendimento e Execução de Serviços* para uma oficina mecânica.

Este projeto implementa:
- Gestão administrativa (CRUDs) de **clientes**, **veículos**, **serviços** e **peças/insumos** (com controle de estoque)
- **Ordens de Serviço (OS)** com fluxo completo: criação, orçamento automático, envio, aprovação pelo cliente, execução e entrega
- **Consulta pública** por `trackingCode` para acompanhamento do cliente: `GET /api/public/ordens-servico/{trackingCode}` retorna o **status atual** da OS no campo JSON `status` (valores alinhados a `StatusOrdemServico`), além de itens e histórico; **aprovação** de orçamento com validação adicional (CPF/CNPJ)
- **Métrica** de tempo médio de execução (EM_EXECUCAO → FINALIZADA)
- **Swagger/OpenAPI**
- **Autenticação JWT** para endpoints administrativos via **Keycloak**
- **Liquibase (YAML)** para migrations + seed mínima
- **Dockerfile + docker-compose** (ambiente completo)
- **Testes unitários e integração** + **JaCoCo** com cobertura mínima nos domínios críticos
- **Documentação DDD** (Event Storming, Diagramas, Linguagem Ubíqua) + roteiro do vídeo
- **Relatório de vulnerabilidades** (scan de dependências + container + filesystem) com evidências
- **Documento de submissão** (Markdown pronto para PDF)

> Requisitos e entregáveis conforme o enunciado do Tech Challenge Fase 1.

---

## Sumário
- [1. Stack e decisões técnicas](#1-stack-e-decisões-técnicas)
- [2. Arquitetura e DDD (monólito com bounded contexts)](#2-arquitetura-e-ddd-monólito-com-bounded-contexts)
- [3. Status da OS e regras principais](#3-status-da-os-e-regras-principais)
- [4. Como rodar localmente (1 comando)](#4-como-rodar-localmente-1-comando)
- [5. URLs importantes](#5-urls-importantes)
- [6. Autenticação (JWT/Keycloak) e role ADMIN](#6-autenticação-jwtkeycloak-e-role-admin)
- [7. Exemplos de uso (cURL)](#7-exemplos-de-uso-curl)
- [8. Testes e cobertura (JaCoCo >= 80% domínios críticos)](#8-testes-e-cobertura-jacoco--80-domínios-críticos)
- [9. Scans de vulnerabilidades (evidências)](#9-scans-de-vulnerabilidades-evidências)
- [10. Documentação DDD e roteiro do vídeo](#10-documentação-ddd-e-roteiro-do-vídeo)
- [11. Documento de submissão (PDF)](#11-documento-de-submissão-pdf)
- [12. Entregas por fase (Partes 1 a 7)](#12-entregas-por-fase-partes-1-a-7)
- [13. Observabilidade e logs](#13-observabilidade-e-logs)
- [14. Troubleshooting](#14-troubleshooting)

---

## 1. Stack e decisões técnicas

### Stack
- **Java 21**
- **Spring Boot 3** (REST, Validation, Security)
- **PostgreSQL**
- **Liquibase (YAML)**
- **OpenAPI/Swagger** (springdoc)
- **JWT** (Keycloak)
- **Testes**: JUnit 5 + Spring Boot Test + Testcontainers (PostgreSQL) + JaCoCo
- **Docker**: Dockerfile multi-stage + docker-compose

### Decisão: PostgreSQL (justificativa)
- ACID e consistência fortes (fluxos de OS, histórico, estoque)
- Constraints/índices maduros e ótima integração com JPA/Liquibase
- Fácil execução local via docker-compose e confiável para cenários transacionais

### Decisão: JWT via Keycloak (justificativa)
- Emissão e validação JWT padronizadas (OIDC), roles e expiração
- Reprodutível no docker-compose via import de realm
- Endpoints administrativos exigem JWT + role `ADMIN`
- Endpoints públicos do cliente ficam em `/api/public/**` e usam mecanismo seguro de acesso via `trackingCode` + validação adicional (CPF/CNPJ) para aprovação

---

## 2. Arquitetura e DDD (monólito com bounded contexts)

Mesmo sendo monólito, a organização de pacotes segue bounded contexts (DDD pragmático), reduzindo acoplamento e mantendo clareza do domínio.

### Bounded Contexts (pacotes)
- `br.com.oficina.cadastros`
  - Cliente (VO: CPF/CNPJ)
  - Veículo (VO: Placa)
- `br.com.oficina.catalogo`
  - Catálogo de Serviços (preço, tempo estimado)
  - Peças/Insumos (preço, estoque)
- `br.com.oficina.ordemservico`
  - `OrdemServico` como **Aggregate Root**
  - Itens de serviços e itens de peças
  - Transições de status (timestamps)
- `br.com.oficina.shared`
  - correlação (`X-Correlation-Id`), erros padronizados, validações, utilitários

---

## 3. Status da OS e regras principais

### Status obrigatórios
- `RECEBIDA`
- `EM_DIAGNOSTICO`
- `AGUARDANDO_APROVACAO`
- `EM_EXECUCAO`
- `FINALIZADA`
- `ENTREGUE`

### Regras (MVP)
- Criar OS:
  - identifica cliente por CPF/CNPJ (cria se não existir)
  - cadastra/associa veículo (placa, marca, modelo, ano)
  - adiciona serviços e peças/insumos
  - gera orçamento automaticamente (serviços + peças)
  - gera `trackingCode` para o cliente
- Ações administrativas:
  - iniciar diagnóstico → `EM_DIAGNOSTICO`
  - enviar orçamento → `AGUARDANDO_APROVACAO`
  - finalizar execução → `FINALIZADA`
  - registrar entrega → `ENTREGUE`
- Ação do cliente:
  - aprovar orçamento via endpoint público (exige CPF/CNPJ) → `EM_EXECUCAO`
  - ao entrar em `EM_EXECUCAO` decrementa estoque das peças usadas (falha se insuficiente)
- Métrica:
  - tempo médio execução = média(`FINALIZADA.at - EM_EXECUCAO.at`)

---

## 4. Como rodar localmente (1 comando)

### Pré-requisitos
- Docker + Docker Compose v2
- (Opcional) Maven + JDK 21 para rodar fora do container

### Subir tudo
```bash
docker compose up --build
