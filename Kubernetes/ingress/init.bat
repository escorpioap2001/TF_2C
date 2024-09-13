@echo off
echo Iniciando la aplicación de configuraciones de Ingress de Kubernetes...

REM Aplicar la configuración de Ingress
kubectl apply -f ingress-nginx.yml
if %errorlevel% neq 0 (
    echo Error aplicando ingress-nginx.yml
    pause
    exit /b %errorlevel%
)

REM Esperar a que el Ingress esté disponible
echo Esperando a que el Ingress esté disponible...
timeout /t 30

REM Obtener el puerto del Ingress
echo Obteniendo el puerto del Ingress...
for /f "tokens=*" %%i in ('kubectl get service ingress-nginx-controller -n ingress-nginx -o jsonpath^="{.spec.ports[?(@.port==80)].nodePort}"') do set INGRESS_PORT=%%i

if "%INGRESS_PORT%"=="" (
    echo No se pudo obtener el puerto del Ingress.
    pause
    exit /b 1
)

echo El puerto del Ingress es: %INGRESS_PORT%

echo Configuración de Ingress de Kubernetes aplicada exitosamente.
pause
