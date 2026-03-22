# Terraform e o enunciado da Fase 2

O **Tech Challenge Fase 2** pede scripts Terraform para **provisionamento do cluster Kubernetes**, **banco de dados** e documentação dos recursos.

## O que este repositório entrega hoje

| Entrega | Detalhe |
|---------|---------|
| **Rede** | Módulo `modules/network`: VPC, subnets públicas (2 AZs), IGW, rota default. |
| **Base de dados (AWS)** | Módulo opcional `modules/database`: **RDS PostgreSQL 16** com `enable_rds = true` (custo; cenário laboratorial em subnets públicas). |
| **CI** | `terraform fmt` / `validate` no push (sem credenciais). |
| **CD infra** | Workflow manual **Terraform AWS** (`plan` / `apply`) com secrets AWS. |
| **Documentação** | [`../README.md`](../README.md), este ficheiro, `terraform.tfvars.example`. |

## O que ainda difere do enunciado “literal”

| Recurso | Observação |
|---------|------------|
| **Cluster Kubernetes (EKS)** | Não provisionado em Terraform neste repo; manifestos em `/k8s` aplicam-se a qualquer cluster (EKS, GKE, k3s, kind). Evolução típica: módulo EKS + subnets **privadas** + **NAT** (custo). |
| **RDS “produção”** | O módulo atual usa subnets **públicas** e `publicly_accessible` para simplificar laboratório; em produção usa-se subnets privadas e SG apenas a partir do cluster. |
| **Custos** | RDS, NAT e EKS geram cobrança; use `terraform destroy` e `enable_rds = false` quando não precisar. |

## Equivalente aceitável (alinhamento com docente)

- **RDS via Terraform** + **K8s** com YAML versionado + **pipeline** com build, testes, imagem e deploy opcional costuma demonstrar o espírito do enunciado.
- Confirme com o professor se **EKS obrigatório** ou se cluster gerido externo / local com vídeo + documentação é suficiente.

## Próximos passos técnicos (evolução)

1. Subnets **privadas** + **NAT Gateway** + mover RDS para subnet group privado.
2. Módulo **EKS** (ou outro Kubernetes gerido) e associar security groups.
3. Backend Terraform remoto (S3 + lock) para equipa.
