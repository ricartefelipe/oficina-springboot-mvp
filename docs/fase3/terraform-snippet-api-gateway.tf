# Snippet Terraform (AWS) — API Gateway HTTP API + integracao Lambda
# Colar numa stack Terraform (ex.: repo oficina-infra-database ou stack propria)
# depois de a funcao Lambda existir na mesma conta/regiao.
#
# Preenche:
#   - var.lambda_function_name  (ex.: oficina-auth-cpf)
#   - aws_lambda_function.auth  (data source ou recurso que ja criaste)
#
# Nota: criar a Lambda e o codigo (SAM em auth-lambda/template.yaml) e' passo
# anterior a este ficheiro.

variable "lambda_function_name" {
  type        = string
  description = "Nome da funcao Lambda de autenticacao CPF (ja deployada)."
}

data "aws_lambda_function" "auth" {
  function_name = var.lambda_function_name
}

resource "aws_apigatewayv2_api" "auth_http" {
  name          = "oficina-auth-cpf"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_integration" "lambda" {
  api_id                 = aws_apigatewayv2_api.auth_http.id
  integration_type       = "AWS_PROXY"
  integration_uri        = data.aws_lambda_function.auth.invoke_arn
  integration_method     = "POST"
  payload_format_version = "2.0"
}

resource "aws_apigatewayv2_route" "post_token" {
  api_id    = aws_apigatewayv2_api.auth_http.id
  route_key = "POST /token"
  target    = "integrations/${aws_apigatewayv2_integration.lambda.id}"
}

resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.auth_http.id
  name        = "$default"
  auto_deploy = true
}

resource "aws_lambda_permission" "apigw_invoke" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = data.aws_lambda_function.auth.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.auth_http.execution_arn}/*/*"
}

output "api_gateway_invoke_url" {
  description = "URL base para POST /token (API Gateway HTTP)"
  value       = aws_apigatewayv2_api.auth_http.api_endpoint
}
