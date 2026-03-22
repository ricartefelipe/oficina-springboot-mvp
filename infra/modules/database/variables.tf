variable "vpc_id" {
  type        = string
  description = "ID da VPC."
}

variable "subnet_ids" {
  type        = list(string)
  description = "IDs das subnets do DB subnet group (minimo 2 AZs)."
}

variable "allowed_cidr" {
  type        = string
  description = "CIDR autorizado a aceder ao Postgres (ex.: CIDR da VPC)."
}

variable "name_prefix" {
  type        = string
  description = "Prefixo para nomes de recursos (ex.: oficina-dev)."
}

variable "db_name" {
  type        = string
  description = "Nome logico da base de dados."
}

variable "db_username" {
  type        = string
  description = "Utilizador master."
}

variable "instance_class" {
  type        = string
  description = "Classe da instancia RDS."
  default     = "db.t3.micro"
}
