param([string]$SigningDirectory = (Join-Path $env:USERPROFILE '.android\signing\sumi-memo'))
$ErrorActionPreference = 'Stop'
$secret = Get-Content -LiteralPath (Join-Path $SigningDirectory 'upload-password.dpapi') | ConvertTo-SecureString
$pointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secret)
try {
    $env:SUMI_STORE_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($pointer)
    $env:SUMI_KEY_PASSWORD = $env:SUMI_STORE_PASSWORD
    $env:SUMI_STORE_FILE = Join-Path $SigningDirectory 'upload.jks'
    $env:SUMI_KEY_ALIAS = 'sumi-upload'
    Push-Location (Split-Path $PSScriptRoot -Parent)
    try {
        & .\gradlew.bat playBundle
        if ($LASTEXITCODE -ne 0) { throw 'Release build failed.' }
    } finally { Pop-Location }
} finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($pointer)
    Remove-Item Env:\SUMI_STORE_PASSWORD, Env:\SUMI_KEY_PASSWORD, Env:\SUMI_STORE_FILE, Env:\SUMI_KEY_ALIAS -ErrorAction SilentlyContinue
}
