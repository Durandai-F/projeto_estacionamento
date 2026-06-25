@echo off
echo ==========================================
echo   SISTEMA DE ESTACIONAMENTO - INICIANDO
echo ==========================================
echo.

:: Verifica se a pasta target existe (onde o Maven compila o código)
if not exist "target\classes" (
    echo [ERRO] Pasta 'target\classes' nao encontrada.
    echo Por favor, compile o projeto no VS Code ou use 'mvn compile' primeiro.
    pause
    exit
)

:: Executa o programa apontando para a pasta correta do Maven
java -cp "target/classes;lib/*" com.estacionamento.app.Main

if %errorlevel% neq 0 (
    echo.
    echo [ERRO] O programa fechou com erros.
)
pause
