@echo off
chcp 65001 > nul
echo ===================================================
echo     INICIANDO ADOTAMATCH (BACKEND + FRONTEND)
echo ===================================================
echo.

echo [1/2] Iniciando Backend Spring Boot (Porta 8080)...
start "AdotaMatch - Backend" cmd /k "cd backend && java -jar target\adotamatch-backend-1.0.0-SNAPSHOT.jar"

echo [2/2] Iniciando Frontend Vue 3 (Porta 5173)...
start "AdotaMatch - Frontend" cmd /k "cd frontend && npm run dev"

echo.
echo ===================================================
echo   Sistema iniciado!
echo   Frontend: http://localhost:5173
echo   Swagger:  http://localhost:8080/swagger-ui.html
echo ===================================================
pause
