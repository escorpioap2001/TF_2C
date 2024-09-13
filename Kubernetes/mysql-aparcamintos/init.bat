@echo off
echo Iniciando la aplicación de configuraciones de Kubernetes...

kubectl apply -f aparcamientos-mysql-volume.yml
if %errorlevel% neq 0 (
    echo Error aplicando aparcamientos-mysql-volume.yml
    exit /b %errorlevel%
)

kubectl apply -f aparcamientos-mysql-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error aplicando aparcamientos-mysql-volume-claim.yml
    exit /b %errorlevel%
)

kubectl apply -f aparcamientos-mysql-init-script.yml
if %errorlevel% neq 0 (
    echo Error aplicando aparcamientos-mysql-init-script.yml
    exit /b %errorlevel%
)

kubectl apply -f aparcamientos-mysql.yml
if %errorlevel% neq 0 (
    echo Error aplicando aparcamientos-mysql.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes aplicadas exitosamente.
pause
