module "network" {
  source = "./modules/network"

  vpc_cidr = var.vpc_cidr
}

module "database" {
  count  = var.enable_rds ? 1 : 0
  source = "./modules/database"

  vpc_id         = module.network.vpc_id
  subnet_ids     = module.network.public_subnet_ids
  allowed_cidr   = var.vpc_cidr
  name_prefix    = "${var.project_name}-${var.environment}"
  db_name        = var.db_name
  db_username    = var.db_username
  instance_class = var.rds_instance_class
}
