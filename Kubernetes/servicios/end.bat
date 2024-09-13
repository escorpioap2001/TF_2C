@echo off
echo Eliminando configuraciones de Kubernetes...

REM Eliminar configuraciones en la carpeta 'repositorios'
for %%f in (repositorios\*.yml) do (
    echo Eliminando %%f...
    kubectl delete -f %%f
    if %errorlevel% neq 0 (
        echo Error eliminando %%f
        exit /b %errorlevel%
    )
)

REM Eliminar configuraciones en la carpeta 'apis'
for %%f in (apis\*.yml) do (
    echo Eliminando %%f...
    kubectl delete -f %%f
    if %errorlevel% neq 0 (
        echo Error eliminando %%f
        exit /b %errorlevel%
    )
)

echo Configuraciones de Kubernetes eliminadas exitosamente.
pause
