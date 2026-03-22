# Infraestrutura (Terraform)

Stack reproduzível com **módulo de rede** em AWS: VPC, duas **subnets públicas** em AZs distintas, **Internet Gateway** e rota `0.0.0.0/0` — base para evoluir (ALB, EKS, RDS, etc.).

## Pré-requisitos

- [Terraform](https://developer.hashicorp.com/terraform/install) `>= 1.5.0`
- Conta **AWS** e credenciais configuradas (por exemplo `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION` ou perfil `~/.aws/credentials`)
- Permissões para criar VPC, subnets, IGW e route tables na região escolhida

## Estado (backend local)

Por defeito o estado fica em ficheiros locais (`terraform.tfstate`). **Não commite** o estado nem segredos — estão no `.gitignore` na raiz do repositório.

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

## Outputs

Após `apply`, o Terraform mostra `vpc_id`, `public_subnet_ids` e `availability_zones_used` — úteis para ligar outros módulos ou manifestos Kubernetes (subnets para ALB/EKS, etc.).

## Custos

VPC, subnets e IGW **não têm custo** por si; tráfego e outros serviços (NAT Gateway, RDS, etc.) sim. Esta stack **não** cria NAT Gateway.

## Estrutura

| Caminho | Descrição |
|---------|-----------|
| `main.tf` | Instancia o módulo `network` |
| `variables.tf` / `outputs.tf` | Variáveis e outputs da raiz |
| `modules/network/` | VPC, subnets públicas, IGW, rota default |
| `docs/terraform-vs-enunciado.md` | Alinhamento com o enunciado (EKS, RDS, custos) |
