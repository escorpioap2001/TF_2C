@echo off

REM Aplicar el archivo YAML usando kubectl apply
kubectl apply -f servicio-cronjob.yml --namespace trabajo-final --overwrite=true

echo Inicialización completada.
pause
