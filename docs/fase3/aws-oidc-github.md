# AWS + GitHub com OIDC (sem chaves no GitHub)

## O que precisas no GitHub

1. **Settings** → **Secrets and variables** → **Actions** → **New repository secret**
2. Nome: **`AWS_ROLE_ARN`**
3. Valor: o ARN do papel IAM que criaste na AWS, por exemplo:  
   `arn:aws:iam::123456789012:role/GitHubActionsTerraform`

Se **`AWS_ROLE_ARN`** estiver definido, o workflow **Terraform AWS** usa OIDC e **não** precisa de `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY`.

Se **não** definires `AWS_ROLE_ARN`, o workflow continua a aceitar o par de chaves (compatibilidade antiga).

## O que já está no código

- O job tem `permissions: id-token: write` (obrigatório para o GitHub emitir o token que a AWS troca por credenciais temporárias).
- O passo `aws-actions/configure-aws-credentials` usa `role-to-assume: ${{ secrets.AWS_ROLE_ARN }}`.

## O que precisas na AWS (resumo)

- **Identity provider** OIDC: `https://token.actions.githubusercontent.com`
- **IAM role** com **trust policy** a permitir o repositório certo, por exemplo condição em `StringLike` com  
  `token.actions.githubusercontent.com:sub` = `repo:ORG/REPO:*` (ou mais restrito, ex. só branch `main`).
- Permissões na role para o que o Terraform faz (VPC, RDS, etc.).

Se o `AssumeRole` falhar no workflow, rever a trust policy (repo/organização errados, ou audience `sts.amazonaws.com`).

## Região

A região continua a vir do input do workflow (`aws_region`), por defeito `sa-east-1`.
