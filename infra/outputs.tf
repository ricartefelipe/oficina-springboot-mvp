output "vpc_id" {
  description = "ID da VPC criada pelo módulo de rede."
  value       = module.network.vpc_id
}

output "public_subnet_ids" {
  description = "IDs das subnets públicas (2 AZs)."
  value       = module.network.public_subnet_ids
}

output "availability_zones_used" {
  description = "AZs utilizadas pelas subnets públicas."
  value       = module.network.availability_zones_used
}

output "rds_endpoint" {
  description = "Endpoint RDS (se enable_rds = true)."
  value       = try(module.database[0].endpoint, null)
}

output "rds_jdbc_url" {
  description = "JDBC URL (sensivel)."
  value       = try(module.database[0].jdbc_url, null)
  sensitive   = true
}

output "rds_master_password" {
  description = "Password master RDS (sensivel). Guarde no Secret do K8s."
  value       = try(module.database[0].master_password, null)
  sensitive   = true
}
