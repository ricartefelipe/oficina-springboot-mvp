# Tech Challenge - Fase 3 - Entrega no portal (PDF)

Documento de entrega da Fase 3 com links para os quatro repositórios, confirmação de acesso `soat-architecture`, documentação técnica e evidências de CI/CD.

## Repositórios (Fase 3)

1. https://github.com/ricartefelipe/oficina-auth-lambda
2. https://github.com/ricartefelipe/oficina-infra-database
3. https://github.com/ricartefelipe/oficina-infra-kubernetes-
4. https://github.com/ricartefelipe/oficina-app

## Acesso do avaliador

- Usuário `soat-architecture` com permissão de leitura em todos os repositórios acima.

## Documentação técnica

- Índice Fase 3: [`../fase3/README.md`](../fase3/README.md)
- ADRs: [`../adr/README.md`](../adr/README.md)
- RFC autenticação CPF/JWT: [`../fase3/rfc/rfc-0001-autenticacao-cpf-jwt-serverless.md`](../fase3/rfc/rfc-0001-autenticacao-cpf-jwt-serverless.md)
- Guia de execução Fase 3: [`../fase3/executar-fase3.md`](../fase3/executar-fase3.md)
- Observabilidade: [`../fase3/observabilidade-prometheus.md`](../fase3/observabilidade-prometheus.md)

## Evidências operacionais

- CI/CD dos quatro repositórios (runs verdes) em GitHub Actions.
- Branch `main` protegida e merge via Pull Request nos quatro repositórios.
- Deploy automatizado por branch (`develop` homologação, `main` produção) no repositório `oficina-app`.

## Vídeo

- Link do vídeo (YouTube/Vimeo, até 15 minutos): **preencher antes do envio no portal**.

## Geração de PDF

No Windows (Pandoc + Edge):

`.\scripts\delivery\md-to-pdf-edge.ps1 -InputMd "docs\delivery\entrega-portal-fase3.md"`
