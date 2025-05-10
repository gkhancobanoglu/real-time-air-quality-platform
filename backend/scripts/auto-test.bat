@echo off
setlocal EnableDelayedExpansion

:: Parametreleri al
set "DURATION=%~1"
if "%DURATION%"=="" set DURATION=30

set "RATE=%~2"
if "%RATE%"=="" set RATE=2

set "ANOMALY_CHANCE=%~3"
if "%ANOMALY_CHANCE%"=="" set ANOMALY_CHANCE=30

:: Limit kontrolleri
if %DURATION% GTR 600 set DURATION=600
if %ANOMALY_CHANCE% LSS 0 set ANOMALY_CHANCE=30
if %ANOMALY_CHANCE% GTR 100 set ANOMALY_CHANCE=30

echo [INFO] Auto-test started: %DURATION%s, %RATE% req/s, Anomaly %ANOMALY_CHANCE%%

:: PS script yaz
set "PSFILE=%TEMP%\_auto_test_input.ps1"
echo param([int]$chance) > "%PSFILE%"
>> "%PSFILE%" echo $lat = [math]::Round((Get-Random -Minimum -90 -Maximum 90), 6)
>> "%PSFILE%" echo $lon = [math]::Round((Get-Random -Minimum -180 -Maximum 180), 6)
>> "%PSFILE%" echo function GetVal {
>> "%PSFILE%" echo param($chance)
>> "%PSFILE%" echo if ((Get-Random -Minimum 0 -Maximum 100) -lt $chance) { return (Get-Random -Minimum 150 -Maximum 501) } else { return (Get-Random -Minimum 20 -Maximum 51) }
>> "%PSFILE%" echo }
>> "%PSFILE%" echo $co = GetVal $chance; $no = GetVal $chance; $no2 = GetVal $chance
>> "%PSFILE%" echo $o3 = GetVal $chance; $so2 = GetVal $chance; $pm25 = GetVal $chance
>> "%PSFILE%" echo $pm10 = GetVal $chance; $nh3 = GetVal $chance
>> "%PSFILE%" echo Start-Process -NoNewWindow -FilePath "manual-input.bat" -ArgumentList $lat, $lon, $co, $no, $no2, $o3, $so2, $pm25, $pm10, $nh3

:: İstek gönderme döngüsü
for /L %%S in (1,1,%DURATION%) do (
  for /L %%I in (1,1,%RATE%) do (
    powershell -ExecutionPolicy Bypass -File "%PSFILE%" %ANOMALY_CHANCE%
  )
  timeout /t 1 >nul
)

echo [INFO] Auto-test finished.
del "%PSFILE%" >nul 2>&1
endlocal
