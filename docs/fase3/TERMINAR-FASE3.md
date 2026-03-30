# Terminar a Fase 3 (guia simples)

Fases **1** e **2** já cumpriram o MVP, a arquitetura hexagonal, K8s, Terraform e CI no monólito.  
A **Fase 3** pede **mais quatro coisas** em cima disso: **4 repositórios**, **cloud “de serviço”** (Gateway + Lambda + BD + cluster), **observabilidade** e **entrega** (docs + vídeo + PDF).

---

## 1) O que o PDF exige (lista curta)

| # | O que é | Estado típico no teu projeto |
|---|---------|------------------------------|
| A | **4 repos** (Lambda, Terraform K8s, Terraform BD, App) com CI/CD | Repós existem; CI a correr; Terraform BD com **plan** OK (chaves IAM). |
| B | **API Gateway** + **Lambda** (CPF → JWT) + rotas protegidas na app | Lambda em `auth-lambda/`; **Gateway na AWS** = falta **criar na conta** (ver snippet Terraform em [`terraform-snippet-api-gateway.tf`](terraform-snippet-api-gateway.tf)). |
| C | **BD gerenciado** + **Kubernetes** (Terraform) | Código em `infra/`; **RDS/cluster reais** = `terraform apply` + custos. |
| D | **Observabilidade** (métricas, logs JSON, dashboards, **alertas** OS) | Prometheus + logs no app; **dashboards/alertas na ferramenta** = Grafana/console + regras (ver [`observabilidade-prometheus.md`](observabilidade-prometheus.md)). |
| E | **Documentação** (diagramas, RFC, ADR, ER BD) | Grande parte em `docs/`; **revisar** e juntar no PDF. |
| F | **Vídeo ≤15 min** + **PDF único** no portal (4 links + vídeo + **soat-architecture**) | **Só tu**: gravar, submeter. |

---

## 2) Ordem prática (o que fazer agora)

1. **Confirmar** `soat-architecture` em **todos** os 4 repositórios GitHub.  
2. **Terraform BD**: se ainda só fizeste **plan**, decide com o grupo se fazes **`apply`** (RDS custa dinheiro).  
3. **API Gateway**: deploy da Lambda na AWS (SAM ou zip) → depois **API HTTP** a apontar para a Lambda (ficheiro de exemplo no repo).  
4. **App no K8s**: imagem no GHCR + manifestos `k8s/` + cluster (Kind local ou EKS na AWS).  
5. **Observabilidade**: abrir Grafana/Prometheus (ou consola) e **capturar** para o vídeo: métrica de OS, log com correlation id.  
6. **Vídeo**: CPF → token → chamada API protegida; pipeline a verde; deploy; dashboard; logs.  
7. **PDF único**: links dos 4 repos + vídeo + link da documentação principal (README `docs/fase3/`).

---

## 3) O que não dá para “acabar” só com código

- **Vídeo** e **upload no portal** (obrigatório no enunciado).  
- **Decisão de custo** na AWS (`apply`).  
- **Dashboards** como prova visual (precisam de um ecrã gravado).

---

## 4) Onde está a documentação técnica

| Tema | Ficheiro |
|------|----------|
| Plano de execução e scripts | [`executar-fase3.md`](executar-fase3.md) |
| Backlog e prioridades | [`backlog-fase3.md`](backlog-fase3.md) |
| Visão e diagramas | [`visao-arquitetura-fase3.md`](visao-arquitetura-fase3.md) |
| OIDC / secrets | [`aws-oidc-github.md`](aws-oidc-github.md) |
| Prometheus / Grafana | [`observabilidade-prometheus.md`](observabilidade-prometheus.md) |
| Snippet API Gateway (Terraform) | [`terraform-snippet-api-gateway.tf`](terraform-snippet-api-gateway.tf) |

---

**Resumo:** “Terminar a Fase 3” = **fechar infra na nuvem** (Gateway, Lambda deployada, BD/cluster se aplicável) + **observabilidade visível** + **vídeo** + **PDF**. O código e os repositórios já estão a maior parte preparados; falta sobretudo **execução na AWS**, **demonstração** e **entrega académica**.
