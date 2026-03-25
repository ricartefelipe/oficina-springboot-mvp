# Tech Challenge - Fase 3 (SOAT)

Este diretorio concentra a **documentacao de arquitetura** e o **plano de execucao** para a Fase 3: operacao corporativa, multi-repositorio, API Gateway, autenticacao via CPF com funcao serverless, infraestrutura na nuvem e observabilidade.

## Fonte

Requisitos oficiais: documento **Tech Challenge - Fase 3** (disciplina SOAT).

## Indice

| Documento | Conteudo |
|-----------|----------|
| [visao-arquitetura-fase3.md](visao-arquitetura-fase3.md) | Diagrama de componentes (nuvem), fluxos e decisoes tecnicas resumidas |
| [diagrama-sequencia-auth-os.md](diagrama-sequencia-auth-os.md) | Sequencia: autenticacao com CPF e abertura de ordem de servico |
| [repositorios-planejados.md](repositorios-planejados.md) | Quatro repositorios, fronteiras e responsabilidades |
| [backlog-fase3.md](backlog-fase3.md) | Fases de entrega, dependencias e criterios de pronto |
| [../adr/README.md](../adr/README.md) | ADRs (decisoes arquiteturais permanentes) |
| [rfc/rfc-0001-autenticacao-cpf-jwt-serverless.md](rfc/rfc-0001-autenticacao-cpf-jwt-serverless.md) | RFC: fluxo de autenticacao e contratos |

## Relacao com o codigo atual

O repositorio **oficina-springboot-mvp** evolui para o **repositorio da aplicacao principal** (container no Kubernetes). Os outros tres repositorios sao **novos** (Lambda, Terraform K8s, Terraform BD). A divisao exata e o momento do extracao estao no [backlog](backlog-fase3.md).
