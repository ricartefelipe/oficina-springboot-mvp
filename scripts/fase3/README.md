# Scripts Fase 3

| Ficheiro | Descrição |
|----------|-----------|
| [`bootstrap-repos.ps1`](bootstrap-repos.ps1) | Gera quatro pastas-gêmeas do monorepo (repos separados) sob `oficina-fase3-repos` por defeito; branch inicial `main`; na app inclui CI + `deploy-k8s-branch.yml`. |
| [`publish-fase3-repos.ps1`](publish-fase3-repos.ps1) | Cria os quatro repositórios no GitHub com `gh repo create` e faz o primeiro push (requer GitHub CLI). |
| [`setup-github-oidc-aws.ps1`](setup-github-oidc-aws.ps1) | **OIDC no PC:** IAM provider + role; trust em varios repos com `-GitHubReposExtra`; imprime o ARN para `AWS_ROLE_ARN` (requer `aws configure` com chave IAM). |
| [`cloudshell-github-oidc.sh`](cloudshell-github-oidc.sh) | **OIDC sem AWS no PC:** corre na **AWS CloudShell** (ja autenticado); cria provider + role `GitHubActionsTerraformInfra` para `oficina-infra-database` e `oficina-infra-kubernetes-`. |
| [`cloudshell-create-iam-user-github.sh`](cloudshell-create-iam-user-github.sh) | **Chaves IAM para GitHub:** na CloudShell cria utilizador `github-terraform-ci`, `PowerUserAccess` e **access key** (colar nos secrets; usar workflow com `use_oidc=false`). |
| [`templates/`](templates/) | Modelos de CI, Terraform e deploy (`deploy-k8s-branch.yml` para `hml`/`prd`). |

Documentação do fluxo completo: [`../../docs/fase3/executar-fase3.md`](../../docs/fase3/executar-fase3.md).
