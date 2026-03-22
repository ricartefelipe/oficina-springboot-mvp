output "endpoint" {
  description = "Endpoint DNS da instancia RDS."
  value       = aws_db_instance.this.address
}

output "port" {
  value = aws_db_instance.this.port
}

output "database_name" {
  value = aws_db_instance.this.db_name
}

output "username" {
  value = aws_db_instance.this.username
}

output "master_password" {
  description = "Password master (sensivel)."
  value       = random_password.master.result
  sensitive   = true
}

output "jdbc_url" {
  description = "JDBC URL para PostgreSQL."
  value       = "jdbc:postgresql://${aws_db_instance.this.address}:${aws_db_instance.this.port}/${aws_db_instance.this.db_name}"
  sensitive   = true
}
