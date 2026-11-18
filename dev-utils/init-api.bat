@echo off
setlocal EnableExtensions

rem Arranca la API Engram Decks (Spring Boot) en local.
rem Uso: doble clic, o desde cualquier consola:  dev-utils\init-api.bat

cd /d "%~dp0.."
if errorlevel 1 (
  echo [ERROR] No se pudo llegar a la raiz del repo desde %~dp0
  exit /b 1
)

set "REPO_ROOT=%CD%"
set "MVNW=%REPO_ROOT%\mvnw.cmd"

echo.
echo ========================================
echo  Engram Decks API - arranque local
echo ========================================
echo  Raiz: %REPO_ROOT%
echo ========================================
echo.

if not exist "%MVNW%" (
  echo [ERROR] No se encontro el Maven wrapper: %MVNW%
  exit /b 1
)

where java >nul 2>&1
if errorlevel 1 (
  echo [ERROR] Java no esta en PATH. Instala JDK 21 y vuelve a intentarlo.
  exit /b 1
)

echo [INFO] Comprobando MongoDB en localhost:27017 ...
netstat -ano | findstr "LISTENING" | findstr /R /C:"[ :]27017 " >nul
if errorlevel 1 (
  echo [WARN] MongoDB no parece estar escuchando en el puerto 27017.
  echo        Arranca MongoDB antes, o la API no podra conectar.
  echo.
)

echo [INFO] Entorno Maven:
echo.
call "%MVNW%" -version
if errorlevel 1 (
  echo [ERROR] No se pudo leer el entorno Maven.
  exit /b 1
)

echo.
echo [INFO] Arrancando Spring Boot. Ctrl+C para detener.
echo        El aviso de que esta en marcha aparece en el log cuando el servidor ya acepta trafico.
echo.
call "%MVNW%" spring-boot:run
set "EXIT_CODE=%ERRORLEVEL%"

echo.
if not "%EXIT_CODE%"=="0" (
  echo [ERROR] La API termino con codigo %EXIT_CODE%.
) else (
  echo [INFO] API detenida.
)

exit /b %EXIT_CODE%
