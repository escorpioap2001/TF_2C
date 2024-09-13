@echo off
echo Eliminando configuraciones de Kubernetes...

kubectl delete -f ayuntamiento-mongo.yml
if %errorlevel% neq 0 (
    echo Error eliminando ayuntamiento-mongo.yml
    exit /b %errorlevel%
)

kubectl delete -f ayuntamiento-mongo-init-script.yml
if %errorlevel% neq 0 (
    echo Error eliminando ayuntamiento-mongo-init-script.yml
    exit /b %errorlevel%
)

kubectl delete -f ayuntamiento-mongo-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error eliminando ayuntamiento-mongo-volume-claim.yml
    exit /b %errorlevel%
)

kubectl delete -f ayuntamiento-mongo-volume.yml
if %errorlevel% neq 0 (
    echo Error eliminando ayuntamiento-mongo-volume.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes eliminadas exitosamente.
pause