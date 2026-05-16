# Fluxo de branches (Git Flow aplicado a este repositório)

## Ramos permanentes

| Ramo | Função |
|------|--------|
| **`main`** | Código estável entregue ao avaliador / releases. recebe merges apenas via **pull request** (normalmente a partir de ramos `release/*` ou hotfix). |
| **`develop`** | Integração contínua: aqui convergem **features** e correções antes da linha de release. |

Não há ramo **`master`** separado: **`main`** cumpre esse papel (nome adotado pelo GitHub por defeito).

## Ramos temporários

| Padrão | Origem | Destino via PR |
|--------|--------|----------------|
| **`feature/*`** ou **`fix/*`** | criados a partir de `develop` | `develop` → após merge, **apagar** o ramo remoto |
| **`release/*`** | criados a partir de `develop` quando se prepara entrega | **`main`** → após merge, tag de versão; depois **retrocesso** do mesmo commit para `develop` se necessário |

Fluxo típico de entrega:

1. Trabalho diário em `feature/...` → **PR para `develop`** → revisão → merge → apagar branch.
2. Quando for lançar versão na linha estável: criar **`release/x.y`** a partir de `develop` → **PR para `main`** → merge → criar **tag** no GitHub Releases → opcionalmente merge de retorno em `develop`.

## Limpeza

- Após merge bem-sucedido, **eliminar** o ramo remoto da feature ou release (GitHub: botão na própria página do PR ou `git push origin --delete nome`).
- Manter apenas **`main`** e **`develop`** como ramos de longa duração.

## Atualização de dependências

As alterações versionadas fazem parte de PRs criados pela equipa (por exemplo contra `develop` ou contra o ramo de integração atual). Actualizações **major** (ex.: Spring Boot 4) exigem ramo dedicado e validação com `mvn -Pci verify` antes de integrar.
