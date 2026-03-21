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
