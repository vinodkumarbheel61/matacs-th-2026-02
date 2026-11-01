# Run Java Prototype
Write-Host "Compiling ProposalTriageEngine.java..." -ForegroundColor Cyan
$javac = (Get-Command javac -ErrorAction SilentlyContinue).Source
if (-not $javac) {
    if (Test-Path "D:\indra\env\software\Java\jdk8u192-b12\bin\javac.exe") {
        $javac = "D:\indra\env\software\Java\jdk8u192-b12\bin\javac.exe"
        $java = "D:\indra\env\software\Java\jdk8u192-b12\bin\java.exe"
    } else {
        Write-Error "javac not found on system PATH."
        exit 1
    }
} else {
    $java = "java"
}

& $javac -d . prototype/ProposalTriageEngine.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Running ProposalTriageEngine..." -ForegroundColor Green
    & $java prototype.ProposalTriageEngine
} else {
    Write-Error "Compilation failed."
}
