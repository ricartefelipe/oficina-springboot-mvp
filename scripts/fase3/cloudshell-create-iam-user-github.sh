#!/usr/bin/env bash
# Corre na AWS CloudShell (ja autenticado). Cria utilizador IAM + PowerUserAccess + access key para GitHub Actions.
# Copia o AccessKeyId e o SecretAccessKey para os secrets AWS_ACCESS_KEY_ID e AWS_SECRET_ACCESS_KEY.
# As chaves secretas so aparecem UMA vez — guarda-as ja.
set -euo pipefail

USER_NAME="${USER_NAME:-github-terraform-ci}"
POLICY_ARN="arn:aws:iam::aws:policy/PowerUserAccess"

if aws iam get-user --user-name "$USER_NAME" &>/dev/null; then
  echo "Utilizador ${USER_NAME} ja existe."
else
  echo "A criar utilizador ${USER_NAME}..."
  aws iam create-user --user-name "$USER_NAME"
fi

echo "A anexar PowerUserAccess..."
aws iam attach-user-policy --user-name "$USER_NAME" --policy-arn "$POLICY_ARN" 2>/dev/null || echo "(policy ja anexada ou erro ignorado)"

KEYS=$(aws iam list-access-keys --user-name "$USER_NAME" --query 'AccessKeyMetadata | length(@)' --output text)
if [ "${KEYS:-0}" -ge 2 ]; then
  echo "ERRO: Este utilizador ja tem 2 access keys. Apaga uma em IAM > Users > Security credentials e volta a correr."
  exit 1
fi

echo ""
echo "A criar nova access key (copia ja para o GitHub)..."
OUT=$(aws iam create-access-key --user-name "$USER_NAME" --output json)
echo "$OUT" | python3 -c "import json,sys; d=json.load(sys.stdin)['AccessKey']; print('AWS_ACCESS_KEY_ID=', d['AccessKeyId']); print('AWS_SECRET_ACCESS_KEY=', d['SecretAccessKey'])"
echo ""
echo "No GitHub: oficina-infra-database > Settings > Secrets > Actions"
echo "  AWS_ACCESS_KEY_ID  = valor de AccessKeyId acima"
echo "  AWS_SECRET_ACCESS_KEY = valor de SecretAccessKey acima"
echo "Depois corre Terraform AWS com use_oidc DESLIGADO (false)."
