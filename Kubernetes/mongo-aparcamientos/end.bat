@echo off
echo Eliminando configuraciones de Kubernetes...

kubectl delete -f aparcamientos-mongo.yml
if %errorlevel% neq 0 (
    echo Error eliminando aparcamientos-mongo.yml
    exit /b %errorlevel%
)

kubectl delete -f aparcamientos-mongo-init-script.yml
if %errorlevel% neq 0 (
    echo Error eliminando aparcamientos-mongo-init-script.yml
    exit /b %errorlevel%
)

kubectl delete -f aparcamientos-mongo-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error eliminando aparcamientos-mongo-volume-claim.yml
    exit /b %errorlevel%
)

kubectl delete -f aparcamientos-mongo-volume.yml
if %errorlevel% neq 0 (
    echo Error eliminando aparcamientos-mongo-volume.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes eliminadas exitosamente.
pause