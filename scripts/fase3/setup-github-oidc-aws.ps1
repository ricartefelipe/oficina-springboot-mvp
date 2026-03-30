<#
.SYNOPSIS
    Configure GitHub OIDC provider and IAM role in AWS.
.DESCRIPTION
    Requires AWS CLI configured locally (aws configure or env vars).
    Prints IAM role ARN to use as GitHub secret AWS_ROLE_ARN.
    Podes confiar em varios repos na mesma role com -GitHubReposExtra (uma role, varios statements).
.PARAMETER GitHubOwner
    GitHub user or org (example: ricartefelipe).
.PARAMETER GitHubRepo
    Primeiro repositorio GitHub (example: oficina-infra-database).
.PARAMETER GitHubReposExtra
    Repos adicionais com a mesma trust (ex.: oficina-infra-kubernetes-).
.PARAMETER RoleName
    IAM role name to create/update.
.PARAMETER PolicyArn
    Managed policy ARN attached to the role.
#>
param(
    [Parameter(Mandatory = $true)]
    [string] $GitHubOwner,
    [Parameter(Mandatory = $true)]
    [string] $GitHubRepo,
    [string[]] $GitHubReposExtra = @(),
    [string] $RoleName = "GitHubActionsTerraformInfra",
    [string] $PolicyArn = "arn:aws:iam::aws:policy/PowerUserAccess"
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command aws -ErrorAction SilentlyContinue)) {
    Write-Error "AWS CLI not found. Install: https://aws.amazon.com/cli/"
}

$account = aws sts get-caller-identity --query Account --output text 2>$null
if (-not $account -or $account -match "error|None") {
    Write-Error "Unable to read AWS account. Run aws configure first."
}

Write-Host "AWS account: $account" -ForegroundColor Cyan

$providers = aws iam list-open-id-connect-providers --output json 2>$null
$hasGithub = $false
if ($providers) {
    $hasGithub = $providers -match "token\.actions\.githubusercontent\.com"
}

if (-not $hasGithub) {
    Write-Host "Creating GitHub OIDC provider..." -ForegroundColor Cyan
    $thumb = "6938fd4d98bab03faadb97b34396831e3780aea1"
    aws iam create-open-id-connect-provider `
        --url "https://token.actions.githubusercontent.com" `
        --client-id-list "sts.amazonaws.com" `
        --thumbprint-list $thumb | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Failed to create OIDC provider."
    }
} else {
    Write-Host "GitHub OIDC provider already exists." -ForegroundColor Green
}

$oidcArn = "arn:aws:iam::${account}:oidc-provider/token.actions.githubusercontent.com"
$allRepos = @($GitHubRepo) + $GitHubReposExtra
Write-Host "Registando client IDs no OIDC (sts + URL do repo)..." -ForegroundColor Cyan
aws iam add-client-id-to-open-id-connect-provider --open-id-connect-provider-arn $oidcArn --client-id "sts.amazonaws.com" 2>$null | Out-Null
foreach ($r in $allRepos) {
    $cid = "https://github.com/${GitHubOwner}/${r}"
    aws iam add-client-id-to-open-id-connect-provider --open-id-connect-provider-arn $oidcArn --client-id $cid 2>$null | Out-Null
}

$federatedArn = $oidcArn
$statements = @()
$n = 0
foreach ($r in $allRepos) {
    $sub = "repo:${GitHubOwner}/${r}:*"
    foreach ($aud in @("sts.amazonaws.com", "https://github.com/${GitHubOwner}/${r}")) {
        $sid = "GitHub$n"
        $statements += @{
            Sid       = $sid
            Effect    = "Allow"
            Principal = @{ Federated = $federatedArn }
            Action    = "sts:AssumeRoleWithWebIdentity"
            Condition = @{
                StringEquals = @{ "token.actions.githubusercontent.com:aud" = $aud }
                StringLike   = @{ "token.actions.githubusercontent.com:sub" = $sub }
            }
        }
        $n++
    }
}

$trust = @{
    Version   = "2012-10-17"
    Statement = $statements
}

$trustJson = $trust | ConvertTo-Json -Depth 10 -Compress
$trustFile = Join-Path ([System.IO.Path]::GetTempPath()) ("github-oidc-trust-" + [Guid]::NewGuid().ToString("N") + ".json")
[System.IO.File]::WriteAllText($trustFile, $trustJson, [System.Text.UTF8Encoding]::new($false))
$trustUri = "file://$($trustFile -replace '\\', '/')"

aws iam get-role --role-name $RoleName --output json 2>$null | Out-Null
$roleExists = ($LASTEXITCODE -eq 0)

if ($roleExists) {
    Write-Host "Updating trust policy on role $RoleName..." -ForegroundColor Yellow
    aws iam update-assume-role-policy --role-name $RoleName --policy-document $trustUri | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Failed to update trust policy."
    }
} else {
    Write-Host "Creating role $RoleName..." -ForegroundColor Cyan
    aws iam create-role --role-name $RoleName --assume-role-policy-document $trustUri | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Failed to create role."
    }
}

$attached = aws iam list-attached-role-policies --role-name $RoleName --output json | ConvertFrom-Json
$alreadyAttached = $attached.AttachedPolicies | Where-Object { $_.PolicyArn -eq $PolicyArn }
if (-not $alreadyAttached) {
    Write-Host "Attaching policy $PolicyArn..." -ForegroundColor Cyan
    aws iam attach-role-policy --role-name $RoleName --policy-arn $PolicyArn | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Failed to attach policy."
    }
} else {
    Write-Host "Policy already attached." -ForegroundColor Green
}

Remove-Item -LiteralPath $trustFile -Force -ErrorAction SilentlyContinue

$roleArn = "arn:aws:iam::${account}:role/${RoleName}"
Write-Host ""
Write-Host "Secret no GitHub (Actions):" -ForegroundColor Green
Write-Host "  Nome: AWS_ROLE_ARN"
Write-Host "  Valor: $roleArn"
Write-Host ""
Write-Host "gh (PowerShell):" -ForegroundColor Green
Write-Host "  gh secret set AWS_ROLE_ARN -b `"$roleArn`" -R ${GitHubOwner}/${GitHubRepo}"
Write-Host ""
