@echo off
setlocal EnableExtensions

rem Download OpenAPI JSON into this folder.
rem Usage:  dev-utils\api-docs-json\download-api-docs.bat
rem Requires the API running at http://localhost:8080

cd /d "%~dp0"
if errorlevel 1 (
  echo [ERROR] Could not reach api-docs-json from %~dp0
  exit /b 1
)

set "OUT_DIR=%CD%"
set "OUT_FILE=%OUT_DIR%\openapi.json"
set "API_DOCS_URL=http://localhost:8080/v3/api-docs"

echo.
echo ========================================
echo  Engram Decks - download OpenAPI JSON
echo ========================================
echo  Source: %API_DOCS_URL%
echo  Output: %OUT_FILE%
echo ========================================
echo.

echo [INFO] Checking API at localhost:8080 ...
powershell -NoProfile -Command ^
  "try { $c = New-Object System.Net.Sockets.TcpClient; $c.Connect('127.0.0.1', 8080); $c.Close(); exit 0 } catch { exit 1 }"
if errorlevel 1 (
  echo [ERROR] API is not listening on port 8080.
  echo        Start it first with:  dev-utils\init-api.bat
  exit /b 1
)

echo [INFO] Downloading OpenAPI JSON ...
powershell -NoProfile -Command ^
  "try { Invoke-WebRequest -Uri '%API_DOCS_URL%' -OutFile '%OUT_FILE%' -UseBasicParsing; exit 0 } catch { Write-Host $_.Exception.Message; exit 1 }"
if errorlevel 1 (
  echo [ERROR] Download failed.
  exit /b 1
)

if not exist "%OUT_FILE%" (
  echo [ERROR] File was not created: %OUT_FILE%
  exit /b 1
)

echo [INFO] Saved: %OUT_FILE%
exit /b 0
