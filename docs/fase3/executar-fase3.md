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
| `oficina-app` | Cópia do código da aplicação (exclui `.git`, `target`, `.terraform`) |

Opções úteis:

```powershell
# Só README na app (sem copiar o código inteiro)
.\scripts\fase3\bootstrap-repos.ps1 -SkipAppCopy

# Outro destino
.\scripts\fase3\bootstrap-repos.ps1 -DestinationRoot D:\repos\fase3
```

Depois, em **cada** pasta: `git add -A`, `git commit`, criar repositório vazio no GitHub e `git remote add` + `git push`.

## O que só tu (ou a equipa) podes fazer

### GitHub

1. Criar os quatro repositórios (vazios ou com README) na organização ou conta desejada.
2. Adicionar o utilizador **`soat-architecture`** com permissão de leitura em **todos** os quatro.
3. **Branch protection** na branch principal (ex.: `main` ou `master`):
   - Exigir pull request antes do merge.
   - Exigir aprovação de revisão (se aplicável à equipa).
   - Opcional: exigir que os checks de CI passem.

Se usares [GitHub CLI](https://cli.github.com/) (`gh`), podes aplicar regras semelhantes por API; o portal também permite configurar em **Settings → Branches → Branch protection rules**.

4. **Secrets** por repositório (exemplos):
   - Infra AWS: `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY` (ou OIDC com AWS, preferível em produção).
   - Deploy K8s / app: kubeconfig, registry, tokens conforme o pipeline.

### AWS

- Conta e permissões para VPC, RDS, Lambda, API Gateway, EKS (conforme o desenho da Fase 3).
- `terraform apply` e pipelines de deploy **consomem custo**; valida região e limites.

### Observabilidade

- Escolha de APM (ex.: integração com export OTLP já alinhada nas ADRs) e criação de dashboards/alertas na ferramenta escolhida — configuração típica em secrets e variáveis de ambiente no cluster.

### Entrega académica

- PDF único, vídeo (≤15 min) e links dos quatro repositórios no portal — upload manual.

## Ordem sugerida

1. Correr `bootstrap-repos.ps1` e rever os quatro diretórios.
2. Criar repos no GitHub e fazer o primeiro push.
3. Configurar branch protection e `soat-architecture`.
4. Adicionar secrets e testar CI (sem `apply` destrutivo em produção até validares o plano).

Para o mapa de responsabilidades e stacks, ver também [`repositorios-planejados.md`](repositorios-planejados.md).
