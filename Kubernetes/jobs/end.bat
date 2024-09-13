@echo off

REM Coloca aquí cualquier operación de limpieza o finalización que necesites realizar.
kubectl delete cronjob servicio-cronjob -n trabajo-final

echo Finalización completada.
pause
