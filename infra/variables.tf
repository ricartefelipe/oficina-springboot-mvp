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

variable "enable_rds" {
  type        = bool
  description = "Se true, cria PostgreSQL RDS (custo AWS; apenas dev/demo)."
  default     = false
}

variable "db_name" {
  type        = string
  description = "Nome da base de dados no RDS."
  default     = "oficina"
}

variable "db_username" {
  type        = string
  description = "Utilizador master do RDS."
  default     = "oficina"
}

variable "rds_instance_class" {
  type        = string
  description = "Classe da instancia RDS (ex.: db.t3.micro)."
  default     = "db.t3.micro"
}
