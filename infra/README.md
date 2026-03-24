# Infraestrutura (Terraform)

Dois stacks Terraform no repositório:

| Pasta | Finalidade |
|-------|------------|
| **`infra/`** (raiz) | **AWS**: VPC, subnets públicas, IGW; **RDS PostgreSQL opcional** (`enable_rds`). |
| **[`kind/`](kind/)** | **Kubernetes local** com [Kind](https://kind.sigs.k8s.io/) (Docker) - cumpre o enunciário de *provisionamento do cluster Kubernetes (local ou cloud)* em ambiente de laboratório. |

## Stack AWS (`infra/`)

- **Módulo `network`**: VPC, duas **subnets públicas** em AZs distintas, **Internet Gateway** e rota `0.0.0.0/0`.
- **Módulo `database` (opcional)**: PostgreSQL **RDS** quando `enable_rds = true` - ver secção abaixo.

## Pré-requisitos

- [Terraform](https://developer.hashicorp.com/terraform/install) `>= 1.5.0`
- Conta **AWS** e credenciais configuradas (por exemplo `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION` ou perfil `~/.aws/credentials`)
- Permissões para criar VPC, subnets, IGW, route tables e, se ativar RDS, instância RDS + security groups

## Estado (backend local)

Por defeito o estado fica em ficheiros locais (`terraform.tfstate`). **Não commite** o estado nem segredos - estão no `.gitignore` na raiz do repositório.

Para equipa/produção, use backend remoto (S3 + DynamoDB lock, Terraform Cloud, etc.) e documente no vosso processo.

## Configuração

```bash
cd infra
cp terraform.tfvars.example terraform.tfvars
# Edite terraform.tfvars se necessário (região, CIDR, tags via variáveis em variables.tf)
```

## Ciclo de vida

Inicializar (descarrega providers, prepara módulos):

```bash
terraform init
```

O primeiro `init` gera `.terraform.lock.hcl` com as versões exatas dos providers; em projetos de equipa convém **versionar** esse ficheiro (não está ignorado no `.gitignore`). O repositório inclui o lock com checksums para **linux_amd64** e **windows_amd64**.

No **GitHub Actions**, o workflow de CI executa `terraform fmt -check`, `init -backend=false` e `validate` na pasta `infra/` (sem credenciais AWS e sem `plan`/`apply`).

Planejar alterações:

```bash
terraform plan
```

Aplicar:

```bash
terraform apply
```

Confirmar com `yes` ou usar `terraform apply -auto-approve` em pipelines (com cuidado).

Destruir recursos criados por esta stack:

```bash
terraform destroy
```

## Módulo RDS PostgreSQL (opcional)

| Variável | Descrição |
|----------|-----------|
| `enable_rds` | `false` por defeito. `true` cria RDS (custo mensal). |
| `db_name` / `db_username` | Nome da BD e utilizador master. |
| `rds_instance_class` | Por defeito `db.t3.micro`. |

A instância fica nas **subnets públicas** com `publicly_accessible = true` (cenário de laboratório; **não** é padrão de produção). O **Security Group** permite Postgres (5432) apenas a partir do **CIDR da VPC** (`vpc_cidr`).

Após `apply` com RDS, use os outputs **`rds_jdbc_url`** e **`rds_master_password`** (sensíveis) para preencher o Secret Kubernetes (`DB_URL`, `DB_USER`, `DB_PASS`) - ver [`../k8s/README.md`](../k8s/README.md).

## Outputs

Após `apply`: `vpc_id`, `public_subnet_ids`, `availability_zones_used`. Com RDS: `rds_endpoint` e outputs sensíveis `rds_jdbc_url`, `rds_master_password`.

## GitHub Actions - Terraform na AWS

O workflow [`.github/workflows/terraform-aws.yml`](../.github/workflows/terraform-aws.yml) (manual) executa `terraform plan` ou `apply` na pasta `infra/`. Requer secrets **`AWS_ACCESS_KEY_ID`** e **`AWS_SECRET_ACCESS_KEY`**. A região pode ser escolhida no formulário do workflow (por defeito `sa-east-1`). O campo **enable_rds** corresponde a `-var enable_rds=...`.

## Custos

VPC, subnets e IGW **não têm custo** por si. **RDS** e tráfego geram cobrança. Esta stack **não** cria NAT Gateway.

## Estrutura

| Caminho | Descrição |
|---------|-----------|
| `main.tf` | Instancia os módulos `network` e `database` (este último se `enable_rds`) |
| `variables.tf` / `outputs.tf` | Variáveis e outputs da raiz |
| `modules/network/` | VPC, subnets públicas, IGW, rota default |
| `modules/database/` | RDS PostgreSQL 16 (opcional) |
| `kind/` | Terraform para cluster **Kind** local (ver [`kind/README.md`](kind/README.md)) |
| `docs/terraform-vs-enunciado.md` | Alinhamento com o enunciário (EKS, custos, próximos passos) |
