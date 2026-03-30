# Executar a Fase 3 (o que é automático vs. manual)

Este documento separa o que o **repositório pode preparar** (ficheiros e script) do que **depende da tua conta** (AWS, GitHub, convites).

## O que já podes gerar localmente

Na raiz do monorepo, executa (PowerShell):

```powershell
Set-Location c:\wks\oficina-springboot-mvp
.\scripts\fase3\bootstrap-repos.ps1
```

Por defeito cria a pasta `c:\wks\oficina-fase3-repos\` (ao lado do monorepo, **não** dentro dele) com quatro pastas:

> **Importante:** se `DestinationRoot` estiver **dentro** do monorepo, o robocopy pode copiar a própria pasta de saída e gerar uma cópia gigante. Use o caminho por defeito ou outro diretório **fora** de `oficina-springboot-mvp`.

| Pasta | Conteúdo |
|-------|----------|
| `oficina-auth-lambda` | Código copiado de `auth-lambda/`, CI na raiz |
| `oficina-infra-database` | Stack Terraform AWS (VPC + RDS opcional), sem `kind/` |
| `oficina-infra-kubernetes` | Stack Terraform Kind (laboratório); roadmap EKS no README |
| `oficina-app` | Cópia do código da aplicação (exclui `.git`, `target`, `.terraform`) + CI + **deploy K8s por branch** |

Opções úteis:

```powershell
# Só README na app (sem copiar o código inteiro)
.\scripts\fase3\bootstrap-repos.ps1 -SkipAppCopy

# Outro destino
.\scripts\fase3\bootstrap-repos.ps1 -DestinationRoot D:\repos\fase3
```

O bootstrap inicializa Git em cada pasta com branch **`main`** (primeiro commit local).

### Publicar os quatro repositórios no GitHub (CLI)

Com [GitHub CLI](https://cli.github.com/) instalado e sessão iniciada (`gh auth login`):

```powershell
.\scripts\fase3\publish-fase3-repos.ps1
```

- Por defeito cria repositórios **privados** e faz push da `main`. Use `-Public` se quiseres repositórios públicos.
- `-Owner minha-org` se os repositórios forem na organização (requer permissões na org).
- Se o repositório remoto **já existir** no GitHub, o script associa `origin` e faz push em vez de falhar.

Alternativa manual: criar os quatro repositórios vazios no site e, em cada pasta, `git remote add origin git@github.com:OWNER/NOME.git` e `git push -u origin main`.

## Branches `develop` e `master` (CI e deploy)

Política suportada pelos workflows copiados pelo bootstrap e pelo monólito:

| Branch | Papel | CI | Deploy automático (app com `deploy-k8s-branch.yml`) |
|--------|--------|-----|------------------------------------------------------|
| `develop` | integração | sim + imagem `ghcr.io/...:develop` | sim — ambiente **homologacao** |
| `master` | espelho estável (atualizado a partir de `develop`) | sim + imagem `ghcr.io/...:master` | sim — ambiente **producao** |

1. Trabalho diário em `develop`; após PR/revisão, fazer merge de `develop` → `master` para manter `master` alinhada.
2. No repositório da app (ou monólito), em **Settings → Environments**, cria **homologacao** e **producao**.
3. Em cada ambiente, define o secret **`KUBE_CONFIG_B64`** (kubeconfig em Base64) do cluster correspondente.
4. O workflow `deploy-k8s-branch.yml` corre **depois** do `CI` concluir com sucesso no mesmo push: aplica `k8s/*.yaml` e faz `kubectl set image` para `ghcr.io/<repo>:develop` ou `:master` conforme a branch.

Repositórios **Lambda** e **Terraform** apenas correm **validação CI** nestas branches (sem deploy K8s neste modelo).

## O que só tu (ou a equipa) podes fazer

### GitHub

1. Criar os quatro repositórios (vazios ou com README) na organização ou conta desejada — ou usar `publish-fase3-repos.ps1`.
2. Adicionar o utilizador **`soat-architecture`** com permissão de leitura em **todos** os quatro.
3. **Branch protection** na branch principal (`master`):
   - Exigir pull request antes do merge.
   - Exigir aprovação de revisão (se aplicável à equipa).
   - Opcional: exigir que os checks de CI passem.

Se usares [GitHub CLI](https://cli.github.com/) (`gh`), podes aplicar regras semelhantes por API; o portal também permite configurar em **Settings → Branches → Branch protection rules**.

4. **Secrets** por repositório (exemplos):
   - Infra AWS com **OIDC** (recomendado): um único secret **`AWS_ROLE_ARN`** com o ARN do papel IAM (ver [`aws-oidc-github.md`](aws-oidc-github.md)).
   - Alternativa: `AWS_ACCESS_KEY_ID` + `AWS_SECRET_ACCESS_KEY` (sem `AWS_ROLE_ARN`).
   - Deploy K8s / app: `KUBE_CONFIG_B64` por **environment** (`homologacao` / `producao`) ou ao nível do repositório para testes.

### AWS

- Conta e permissões para VPC, RDS, Lambda, API Gateway, EKS (conforme o desenho da Fase 3).
- `terraform apply` e pipelines de deploy **consomem custo**; valida região e limites.

### Observabilidade

- Escolha de APM (ex.: integração com export OTLP já alinhada nas ADRs) e criação de dashboards/alertas na ferramenta escolhida — configuração típica em secrets e variáveis de ambiente no cluster.

### Entrega académica

- PDF único, vídeo (≤15 min) e links dos quatro repositórios no portal — upload manual.

## Ordem sugerida

1. Correr `bootstrap-repos.ps1` e rever os quatro diretórios.
2. Correr `publish-fase3-repos.ps1` (ou criar repos e fazer push manualmente).
3. Configurar branch protection e `soat-architecture`.
4. Configurar environments com `KUBE_CONFIG_B64` quando o cluster existir (deploy a partir de `develop` / `master`).
5. Adicionar secrets AWS e testar CI (sem `apply` destrutivo em produção até validares o plano).

Para o mapa de responsabilidades e stacks, ver também [`repositorios-planejados.md`](repositorios-planejados.md).
