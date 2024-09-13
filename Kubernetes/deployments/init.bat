@echo off
echo Iniciando la aplicación de configuraciones de deployments de Kubernetes...

REM Aplicar configuraciones de deployments en la carpeta 'apis'
for %%f in (apis\*-deployment.yml) do (
    echo Aplicando %%f...
    kubectl apply -f %%f
    if %errorlevel% neq 0 (
        echo Error aplicando %%f
        exit /b %errorlevel%
    )
)

REM Aplicar configuraciones de deployments en la carpeta 'repositorios'
for %%f in (repositorios\*-deployment.yml) do (
    echo Aplicando %%f...
    kubectl apply -f %%f
    if %errorlevel% neq 0 (
        echo Error aplicando %%f
        exit /b %errorlevel%
    )
)

echo Configuraciones de deployments de Kubernetes aplicadas exitosamente.
pause
