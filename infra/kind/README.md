# Kubernetes local (Kind) via Terraform

Cumpre o enunciado da Fase 2 (**cluster Kubernetes local ou cloud**) com **Kind** (Kubernetes in Docker), reproduzivel por **Terraform** na maquina do desenvolvedor.

## Requisitos

- [Docker](https://docs.docker.com/get-docker/) em execucao
- [Terraform](https://developer.hashicorp.com/terraform/install) `>= 1.5.0`

## Uso

```bash
cd infra/kind
terraform init
terraform plan
terraform apply
```

Apos o `apply`, configure o kubectl (exemplo - ajuste ao output real):

```bash
terraform output -raw kubeconfig > "$HOME/.kube/oficina-kind-config"
export KUBECONFIG="$HOME/.kube/oficina-kind-config"
kubectl cluster-info
```

Em seguida siga [`../../k8s/README.md`](../../k8s/README.md) para aplicar os manifestos da aplicacao (ConfigMap, Secret com JDBC, Deployment, Service, HPA). A imagem da app pode ser construida localmente (`docker build`) e carregada no Kind com `kind load docker-image ... --name <nome-do-cluster>`.

## Destruir

```bash
terraform destroy
```

## CI

O workflow [`.github/workflows/ci.yml`](../../.github/workflows/ci.yml) executa `terraform fmt`, `init -backend=false` e `validate` nesta pasta (sem criar cluster; sem Docker no runner para este passo).
