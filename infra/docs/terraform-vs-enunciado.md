# Terraform e o enunciado da Fase 2

O **Tech Challenge Fase 2** pede scripts Terraform para **provisionamento do cluster Kubernetes**, **banco de dados** e documentação dos recursos.

## O que este repositório entrega hoje

| Entrega | Detalhe |
|---------|---------|
| **Rede** | Módulo `modules/network`: VPC, subnets públicas (2 AZs), IGW, rota default. |
| **Base de dados (AWS)** | Módulo opcional `modules/database`: **RDS PostgreSQL 16** com `enable_rds = true` (custo; cenário laboratorial em subnets públicas). |
| **Cluster Kubernetes (local)** | Stack separado em [`../kind`](../kind): Terraform + **Kind** (Kubernetes in Docker), alinhado ao enunciário de *cluster local ou cloud*. |
| **CI** | `terraform fmt` / `validate` em `infra/` e `infra/kind` no push (sem credenciais; sem criar cluster no runner). |
| **CD infra** | Workflow manual **Terraform AWS** (`plan` / `apply`) com secrets AWS. |
| **Documentação** | [`../README.md`](../README.md), [`../kind/README.md`](../kind/README.md), este ficheiro, `terraform.tfvars.example`. |

## O que ainda difere do enunciado “literal”

| Recurso | Observação |
|---------|------------|
| **Cluster Kubernetes gerido na AWS (EKS)** | Não está no Terraform da pasta `infra/` (apenas VPC/RDS). Para laboratório, o cluster **local** está em `infra/kind`. Evolução típica em cloud: módulo EKS + subnets **privadas** + **NAT** (custo). |
| **RDS “produção”** | O módulo atual usa subnets **públicas** e `publicly_accessible` para simplificar laboratório; em produção usa-se subnets privadas e SG apenas a partir do cluster. |
| **Custos** | RDS, NAT e EKS geram cobrança; Kind + `enable_rds = false` evita custo de cloud. |

## Equivalente aceitável (alinhamento com docente)

- **RDS via Terraform (AWS)** + **cluster Kind via Terraform (local)** + manifestos em `/k8s` + pipeline com build, testes, imagem e deploy opcional cobre o espírito do enunciário (local + cloud).
- Confirme com o professor se **EKS obrigatório** ou se **Kind** documentado + vídeo é suficiente para o critério de cluster.

## Próximos passos técnicos (evolução)

1. Subnets **privadas** + **NAT Gateway** + mover RDS para subnet group privado.
2. Módulo **EKS** (ou outro Kubernetes gerido) e associar security groups.
3. Backend Terraform remoto (S3 + lock) para equipa.
