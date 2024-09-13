@echo off
echo Iniciando la aplicación de configuraciones de Kubernetes...

kubectl apply -f estaciones-mysql-volume.yml
if %errorlevel% neq 0 (
    echo Error aplicando estaciones-mysql-volume.yml
    exit /b %errorlevel%
)

kubectl apply -f estaciones-mysql-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error aplicando estaciones-mysql-volume-claim.yml
    exit /b %errorlevel%
)

kubectl apply -f estaciones-mysql-init-script.yml
if %errorlevel% neq 0 (
    echo Error aplicando estaciones-mysql-init-script.yml
    exit /b %errorlevel%
)

kubectl apply -f estaciones-mysql.yml
if %errorlevel% neq 0 (
    echo Error aplicando estaciones-mysql.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes aplicadas exitosamente.
pause
