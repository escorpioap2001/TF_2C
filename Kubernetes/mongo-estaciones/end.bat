@echo off
echo Eliminando configuraciones de Kubernetes...

kubectl delete -f estaciones-mongo.yml
if %errorlevel% neq 0 (
    echo Error eliminando estaciones-mongo.yml
    exit /b %errorlevel%
)

kubectl delete -f estaciones-mongo-init-script.yml
if %errorlevel% neq 0 (
    echo Error eliminando estaciones-mongo-init-script.yml
    exit /b %errorlevel%
)

kubectl delete -f estaciones-mongo-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error eliminando estaciones-mongo-volume-claim.yml
    exit /b %errorlevel%
)

kubectl delete -f estaciones-mongo-volume.yml
if %errorlevel% neq 0 (
    echo Error eliminando estaciones-mongo-volume.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes eliminadas exitosamente.
pause