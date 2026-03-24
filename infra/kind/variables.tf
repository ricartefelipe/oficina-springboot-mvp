variable "cluster_name" {
  type        = string
  description = "Nome do cluster Kind local (Kubernetes in Docker)."
  default     = "oficina-local"
}

variable "node_image" {
  type        = string
  description = "Imagem kindest/node alinhada a uma versao suportada do Kubernetes."
  default     = "kindest/node:v1.30.0"
}
