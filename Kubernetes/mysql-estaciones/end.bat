@echo off
echo Eliminando configuraciones de Kubernetes...

kubectl delete -f estaciones-mysql.yml
if %errorlevel% neq 0 (
    echo Error eliminando estaciones-mysql.yml
    exit /b %errorlevel%
)

kubectl delete -f estaciones-mysql-init-script.yml
if %errorlevel% neq 0 (
    echo Error eliminando estaciones-mysql-init-script.yml
    exit /b %errorlevel%
)

kubectl delete -f estaciones-mysql-volume-claim.yml
if %errorlevel% neq 0 (
    echo Error eliminando estaciones-mysql-volume-claim.yml
    exit /b %errorlevel%
)

kubectl delete -f estaciones-mysql-volume.yml
if %errorlevel% neq 0 (
    echo Error eliminando estaciones-mysql-volume.yml
    exit /b %errorlevel%
)

echo Configuraciones de Kubernetes eliminadas exitosamente.
pause