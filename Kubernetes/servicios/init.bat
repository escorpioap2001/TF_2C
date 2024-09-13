@echo off
echo Iniciando la aplicación de configuraciones de Kubernetes...

REM Aplicar configuraciones en la carpeta 'apis'
for %%f in (apis\*.yml) do (
    echo Aplicando %%f...
    kubectl apply -f %%f
    if %errorlevel% neq 0 (
        echo Error aplicando %%f
        exit /b %errorlevel%
    )
)

REM Aplicar configuraciones en la carpeta 'repositorios'
for %%f in (repositorios\*.yml) do (
    echo Aplicando %%f...
    kubectl apply -f %%f
    if %errorlevel% neq 0 (
        echo Error aplicando %%f
        exit /b %errorlevel%
    )
)

echo Configuraciones de Kubernetes aplicadas exitosamente.
pause
