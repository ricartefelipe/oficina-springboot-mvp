output "cluster_name" {
  description = "Nome do cluster Kind."
  value       = kind_cluster.this.name
}

output "endpoint" {
  description = "Endpoint da API do Kubernetes."
  value       = kind_cluster.this.endpoint
}

output "kubeconfig" {
  description = "Conteudo kubeconfig para kubectl (tratar como segredo fora do laboratorio)."
  value       = kind_cluster.this.kubeconfig
  sensitive   = true
}
