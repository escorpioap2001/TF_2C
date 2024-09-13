@echo off
echo Iniciando la aplicación de configuraciones de Kubernetes...

kubectl apply -f aparcamientos-mongo-volume.yml
if %errorlevel% neq 0 (
    echo Error aplicando aparcamientos-mongo-volume.yml
    exit /b %errorlevel%
)

kubectl apply -f aparcamientos-mongo-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error aplicando aparcamientos-mongo-volume-claim.yml
    exit /b %errorlevel%
)

kubectl apply -f aparcamientos-mongo-init-script.yml
if %errorlevel% neq 0 (
    echo Error aplicando aparcamientos-mongo-init-script.yml
    exit /b %errorlevel%
)

kubectl apply -f aparcamientos-mongo.yml
if %errorlevel% neq 0 (
    echo Error aplicando aparcamientos-mongo.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes aplicadas exitosamente.
pause
