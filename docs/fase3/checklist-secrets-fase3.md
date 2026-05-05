# Checklist de Secrets e Pre-Deploy (Fase 3)

Este documento deixa o ambiente pronto para o deploy assim que os segredos reais estiverem disponiveis.

## Repositorios e segredos minimos

### 1) `oficina-auth-lambda`

Necessarios para deploy AWS da Lambda de autenticacao:

- `AWS_ROLE_ARN` (OIDC recomendado)
- `AUTH_LAMBDA_PG_HOST`
- `AUTH_LAMBDA_PG_USER`
- `AUTH_LAMBDA_PG_PASSWORD`
- `AUTH_LAMBDA_JWT_SECRET`

### 2) `oficina-infra-database`

Necessarios para workflow `terraform-aws.yml`:

- `AWS_ROLE_ARN` (OIDC) **ou**
- `AWS_ACCESS_KEY_ID` + `AWS_SECRET_ACCESS_KEY`

### 3) `oficina-infra-kubernetes-`

Para validacao de Terraform local/Kind, normalmente nao exige segredos.
Se houver deploy em nuvem neste repo, usar os mesmos segredos AWS do item 2.

### 4) `oficina-app`

Necessarios para deploy da app em Kubernetes e integracao auth:

- `KUBE_CONFIG_B64`
- `AUTH_LAMBDA_JWT_SECRET` (mesmo valor da lambda)
- `AWS_ROLE_ARN` (se usar workflows AWS neste repo)
- `AWS_ACCESS_KEY_ID` + `AWS_SECRET_ACCESS_KEY` (se nao usar OIDC)

## Comandos de verificacao (local, via GitHub CLI)

```bash
gh secret list -R ricartefelipe/oficina-auth-lambda
gh secret list -R ricartefelipe/oficina-infra-database
gh secret list -R ricartefelipe/oficina-infra-kubernetes-
gh secret list -R ricartefelipe/oficina-app
```

## Ordem sugerida para disparar deploy

1. `oficina-infra-database` -> workflow `Terraform AWS` (`plan`, depois `apply` quando validado).
2. `oficina-auth-lambda` -> workflow `Deploy auth Lambda AWS`.
3. `oficina-app` -> workflow `CI`, depois `Deploy Kubernetes (develop / main)`.
4. Validar healthchecks, logs, metricas e links de evidencias para o PDF.

## Evidencia no PDF do portal

Registrar no `docs/delivery/entrega-portal-fase3.md`:

- links dos 4 repositorios;
- links de runs verdes de CI/CD;
- confirmacao de `soat-architecture`;
- link do video (quando publicado).
