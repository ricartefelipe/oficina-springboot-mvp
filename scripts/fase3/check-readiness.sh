#!/usr/bin/env bash
set -euo pipefail

repos=(
  "ricartefelipe/oficina-auth-lambda"
  "ricartefelipe/oficina-infra-database"
  "ricartefelipe/oficina-infra-kubernetes-"
  "ricartefelipe/oficina-app"
)

echo "== Fase 3 readiness =="
for repo in "${repos[@]}"; do
  echo
  echo "## $repo"
  echo "- Branch protection (main):"
  if gh api "repos/$repo/branches/main/protection" >/dev/null 2>&1; then
    echo "  OK"
  else
    echo "  MISSING"
  fi

  echo "- soat-architecture access:"
  if gh api "repos/$repo/collaborators/soat-architecture/permission" --jq .permission >/dev/null 2>&1; then
    perm="$(gh api "repos/$repo/collaborators/soat-architecture/permission" --jq .permission)"
    echo "  $perm"
  else
    echo "  MISSING"
  fi

  echo "- Secrets:"
  gh secret list -R "$repo" || true
done

echo
echo "Checklist completo em docs/fase3/checklist-secrets-fase3.md"
