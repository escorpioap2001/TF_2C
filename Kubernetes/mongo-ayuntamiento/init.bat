@echo off
echo Iniciando la aplicación de configuraciones de Kubernetes...

kubectl apply -f ayuntamiento-mongo-volume.yml
if %errorlevel% neq 0 (
    echo Error aplicando ayuntamiento-mongo-volume.yml
    exit /b %errorlevel%
)

kubectl apply -f ayuntamiento-mongo-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error aplicando ayuntamiento-mongo-volume-claim.yml
    exit /b %errorlevel%
)

kubectl apply -f ayuntamiento-mongo-init-script.yml
if %errorlevel% neq 0 (
    echo Error aplicando ayuntamiento-mongo-init-script.yml
    exit /b %errorlevel%
)

kubectl apply -f ayuntamiento-mongo.yml
if %errorlevel% neq 0 (
    echo Error aplicando ayuntamiento-mongo.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes aplicadas exitosamente.
pause
