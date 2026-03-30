#!/usr/bin/env bash
# Executar na AWS CloudShell (consola AWS > icone CloudShell): ja tens credenciais AWS.
set -euo pipefail

GITHUB_OWNER="${GITHUB_OWNER:-ricartefelipe}"
ROLE_NAME="${ROLE_NAME:-GitHubActionsTerraformInfra}"
POLICY_ARN="${POLICY_ARN:-arn:aws:iam::aws:policy/PowerUserAccess}"
THUMBPRINT="${THUMBPRINT:-6938fd4d98bab03faadb97b34396831e3780aea1}"

GITHUB_REPOS=(
  "oficina-infra-database"
  "oficina-infra-kubernetes-"
)

ACCOUNT="$(aws sts get-caller-identity --query Account --output text)"
echo "Conta AWS: ${ACCOUNT}"

if ! aws iam list-open-id-connect-providers --output json | grep -q 'token.actions.githubusercontent.com'; then
  echo "A criar OIDC provider GitHub..."
  aws iam create-open-id-connect-provider \
    --url "https://token.actions.githubusercontent.com" \
    --client-id-list "sts.amazonaws.com" \
    --thumbprint-list "${THUMBPRINT}"
else
  echo "OIDC provider GitHub ja existe."
fi

OIDC_ARN="arn:aws:iam::${ACCOUNT}:oidc-provider/token.actions.githubusercontent.com"
echo "A garantir client IDs no OIDC (sts + URL do repo; corrige Incorrect token audience)..."
for cid in "sts.amazonaws.com"; do
  aws iam add-client-id-to-open-id-connect-provider \
    --open-id-connect-provider-arn "${OIDC_ARN}" \
    --client-id "${cid}" 2>/dev/null && echo "  OK: ${cid}" || true
done
for r in "${GITHUB_REPOS[@]}"; do
  cid="https://github.com/${GITHUB_OWNER}/${r}"
  aws iam add-client-id-to-open-id-connect-provider \
    --open-id-connect-provider-arn "${OIDC_ARN}" \
    --client-id "${cid}" 2>/dev/null && echo "  OK: ${cid}" || true
done

REPOS_FILE="$(mktemp)"
printf '%s\n' "${GITHUB_REPOS[@]}" > "${REPOS_FILE}"
TRUST_FILE="$(mktemp)"
trap 'rm -f "${REPOS_FILE}" "${TRUST_FILE}"' EXIT

python3 - "$ACCOUNT" "$GITHUB_OWNER" "$REPOS_FILE" > "${TRUST_FILE}" <<'PY'
import json, sys
account, owner, repos_path = sys.argv[1], sys.argv[2], sys.argv[3]
with open(repos_path, encoding="utf-8") as f:
    repos = [line.strip() for line in f if line.strip()]
fed = f"arn:aws:iam::{account}:oidc-provider/token.actions.githubusercontent.com"
stmts = []
n = 0
for r in repos:
    sub = f"repo:{owner}/{r}:*"
    for aud in ("sts.amazonaws.com", f"https://github.com/{owner}/{r}"):
        stmts.append({
            "Sid": f"GitHub{n}",
            "Effect": "Allow",
            "Principal": {"Federated": fed},
            "Action": "sts:AssumeRoleWithWebIdentity",
            "Condition": {
                "StringEquals": {"token.actions.githubusercontent.com:aud": aud},
                "StringLike": {"token.actions.githubusercontent.com:sub": sub},
            },
        })
        n += 1
print(json.dumps({"Version": "2012-10-17", "Statement": stmts}))
PY

if aws iam get-role --role-name "${ROLE_NAME}" >/dev/null 2>&1; then
  echo "A atualizar trust policy da role ${ROLE_NAME}..."
  aws iam update-assume-role-policy --role-name "${ROLE_NAME}" --policy-document "file://${TRUST_FILE}"
else
  echo "A criar role ${ROLE_NAME}..."
  aws iam create-role --role-name "${ROLE_NAME}" --assume-role-policy-document "file://${TRUST_FILE}"
fi

ATTACHED="$(aws iam list-attached-role-policies --role-name "${ROLE_NAME}" --output text)"
if ! echo "${ATTACHED}" | grep -q 'PowerUserAccess'; then
  echo "A anexar ${POLICY_ARN}..."
  aws iam attach-role-policy --role-name "${ROLE_NAME}" --policy-arn "${POLICY_ARN}"
fi

ROLE_ARN="arn:aws:iam::${ACCOUNT}:role/${ROLE_NAME}"
echo ""
echo "=== ARN (secret AWS_ROLE_ARN no GitHub) ==="
echo "${ROLE_ARN}"
echo ""
echo "No PC com gh:"
echo "  gh secret set AWS_ROLE_ARN -b \"${ROLE_ARN}\" -R ${GITHUB_OWNER}/oficina-infra-database"
echo ""
