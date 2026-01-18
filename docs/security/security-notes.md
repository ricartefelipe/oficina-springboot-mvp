# Security Notes (MVP - Fase 1)

## Objetivo
Definir decisoes de seguranca e trade-offs do MVP, alinhado ao enunciado.

## Autenticacao e Autorizacao
- Estrategia: JWT emitido por Keycloak (OpenID Connect).
- Papel (role) principal: `ADMIN` (realm role).
- Endpoints administrativos: `/api/admin/**` (serao protegidos na Parte 4).
- Endpoints publicos do cliente: `/api/public/**` (consulta de OS por trackingCode e validacao adicional sera implementada nas partes seguintes).

## Principios adotados
- Negar por padrao para rotas administrativas.
- Nao vazar stacktrace em producao (tratamento padronizado de erros sera feito nas proximas partes).
- Logs com correlation-id; evitar dados sensiveis em logs (CPF/CNPJ completos, tokens, etc).

## Trade-offs do MVP
- Keycloak adiciona peso ao docker-compose, mas garante emissao/roles realistas.
- Credenciais default foram fixadas para execucao local simples; isso nao e producao.
