@echo off
echo Iniciando la aplicación de configuraciones de Kubernetes...

kubectl apply -f estaciones-mongo-volume.yml
if %errorlevel% neq 0 (
    echo Error aplicando estaciones-mongo-volume.yml
    exit /b %errorlevel%
)

kubectl apply -f estaciones-mongo-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error aplicando estaciones-mongo-volume-claim.yml
    exit /b %errorlevel%
)

kubectl apply -f estaciones-mongo-init-script.yml
if %errorlevel% neq 0 (
    echo Error aplicando estaciones-mongo-init-script.yml
    exit /b %errorlevel%
)

kubectl apply -f estaciones-mongo.yml
if %errorlevel% neq 0 (
    echo Error aplicando estaciones-mongo.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes aplicadas exitosamente.
pause
