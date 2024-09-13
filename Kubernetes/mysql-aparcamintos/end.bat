@echo off
echo Eliminando configuraciones de Kubernetes...

kubectl delete -f aparcamientos-mysql.yml
if %errorlevel% neq 0 (
    echo Error eliminando aparcamientos-mysql.yml
    exit /b %errorlevel%
)

kubectl delete -f aparcamientos-mysql-init-script.yml
if %errorlevel% neq 0 (
    echo Error eliminando aparcamientos-mysql-init-script.yml
    exit /b %errorlevel%
)

kubectl delete -f aparcamientos-mysql-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error eliminando aparcamientos-mysql-volume-claim.yml
    exit /b %errorlevel%
)

kubectl delete -f aparcamientos-mysql-volume.yml
if %errorlevel% neq 0 (
    echo Error eliminando aparcamientos-mysql-volume.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes eliminadas exitosamente.
pause