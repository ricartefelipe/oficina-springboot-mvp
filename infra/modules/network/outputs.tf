output "vpc_id" {
  value = aws_vpc.this.id
}

output "public_subnet_ids" {
  value = aws_subnet.public[*].id
}

output "availability_zones_used" {
  value = aws_subnet.public[*].availability_zone
}
