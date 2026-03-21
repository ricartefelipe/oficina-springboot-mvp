variable "aws_region" {
  type        = string
  description = "Região AWS (ex.: sa-east-1)."
  default     = "sa-east-1"
}

variable "project_name" {
  type        = string
  description = "Nome lógico usado em tags de recursos."
  default     = "oficina"
}

variable "environment" {
  type        = string
  description = "Ambiente (ex.: dev, staging)."
  default     = "dev"
}

variable "vpc_cidr" {
  type        = string
  description = "CIDR da VPC."
  default     = "10.42.0.0/16"
}
