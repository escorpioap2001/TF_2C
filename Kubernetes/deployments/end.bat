@echo off
echo Eliminando configuraciones de deployments de Kubernetes...

REM Eliminar configuraciones de deployments en la carpeta 'repositorios'
for %%f in (repositorios\*-deployment.yml) do (
    echo Eliminando %%f...
    kubectl delete -f %%f
    if %errorlevel% neq 0 (
        echo Error eliminando %%f
        exit /b %errorlevel%
    )
)

REM Eliminar configuraciones de deployments en la carpeta 'apis'
for %%f in (apis\*-deployment.yml) do (
    echo Eliminando %%f...
    kubectl delete -f %%f
    if %errorlevel% neq 0 (
        echo Error eliminando %%f
        exit /b %errorlevel%
    )
)

echo Configuraciones de deployments de Kubernetes eliminadas exitosamente.
pause
