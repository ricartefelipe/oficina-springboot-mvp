# Terraform e o enunciado da Fase 2

O **Tech Challenge Fase 2** pede scripts Terraform para **provisionamento do cluster Kubernetes**, **banco de dados** e documentação dos recursos.

## O que este repositório entrega hoje

- Módulo **`modules/network`** na raiz de `infra/`: **VPC**, subnets públicas em duas AZs, **Internet Gateway** e rota default.
- Variáveis, outputs e `terraform plan` / `apply` / `destroy` descritos em [`../README.md`](../README.md).

Isto cobre **base de rede** na AWS para futuramente associar subnets privadas, **EKS**, **RDS** ou endpoints geridos.

## O que normalmente falta para “fechar” o enunciao literal

| Recurso | Observação |
|---------|------------|
| **Cluster Kubernetes (EKS)** | Módulo `terraform-aws-modules/eks` ou equivalente; subnets **privadas** + **NAT Gateway** (custo recorrente) são o padrão. |
| **Banco PostgreSQL (RDS)** | `aws_db_instance` ou módulo RDS; **subnet group** em subnets privadas; **Security Group** restritivo. |
| **Custos** | NAT, EKS control plane e RDS geram cobrança; use `terraform destroy` e ambientes de dev com cautela. |

## Equivalente aceitável (alinhamento com docente)

Algumas turmas aceitam:

- **Rede + documentação** do desenho alvo (EKS + RDS) e limites de custo, **ou**
- Cluster **local** (kind, minikube, k3d) com **manifestos** em `/k8s` e Terraform apenas para **rede/EC2** de laboratório.

**Confirme** com o professor qual combinação atende ao critério de avaliação.

## Próximos passos técnicos (se for evoluir o código)

1. Adicionar subnets **privadas** e **NAT** ao módulo de rede (ou módulo separado).
2. Instanciar **EKS** (ou GKE/AKS em outro provider) com node groups mínimos.
3. Instanciar **RDS PostgreSQL** na VPC, acessível apenas a partir do SG do cluster ou da aplicação.
4. Opcional: **Helm** ou `kubectl` via `null_resource` / pipeline CI — automatiza “deploy no cluster” após imagem no registry.
