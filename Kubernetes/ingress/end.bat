@echo off
echo Eliminando configuraciones de Ingress de Kubernetes...

REM Eliminar la configuración de Ingress
kubectl delete -f ingress-nginx.yml
if %errorlevel% neq 0 (
    echo Error eliminando ingress-nginx.yml
    exit /b %errorlevel%
)

echo Configuración de Ingress de Kubernetes eliminada exitosamente.
pause

