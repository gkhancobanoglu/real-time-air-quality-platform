@echo off
chcp 437 >nul
setlocal enabledelayedexpansion

:: Parameters with default values
set DURATION=%~1
if "%DURATION%"=="" set DURATION=30
set RATE=%~2
if "%RATE%"=="" set RATE=2
set ANOMALY_CHANCE=%~3
if "%ANOMALY_CHANCE%"=="" set ANOMALY_CHANCE=30

set PARAMETERS=PM2.5 PM10 NO2 SO2 O3

echo Starting: Duration=%DURATION% sec, Rate=%RATE% req/s, Anomaly Chance=%ANOMALY_CHANCE%%

:: Get start time in seconds
for /F "tokens=2 delims==" %%A in ('wmic os get localdatetime /value') do set "start=%%A"
set /A startsec=1*%start:~8,2%*3600 + 1*%start:~10,2%*60 + 1*%start:~12,2%
set /A end=startsec + DURATION

:loop
:: Get current time in seconds
for /F "tokens=2 delims==" %%A in ('wmic os get localdatetime /value') do set "now=%%A"
set /A nowsec=1*!now:~8,2!*3600 + 1*!now:~10,2!*60 + 1*!now:~12,2!

if !nowsec! GEQ !end! goto :eof

for /L %%i in (1,1,%RATE%) do (
    :: Random latitude between 40.90 and 41.10
    set /A rand=!random! %% 2001
    set /A latint=40900 + !rand!
    set LAT=!latint:~0,-2!.!latint:~-2!

    :: Random longitude between 28.80 and 29.20
    set /A rand=!random! %% 4001
    set /A lonint=28800 + !rand!
    set LON=!lonint:~0,-2!.!lonint:~-2!

    :: Random parameter
    set /A index=!random! %% 5
    for /F "tokens=1-5" %%a in ("PM2.5 PM10 NO2 SO2 O3") do (
        if !index! == 0 set PARAM=%%a
        if !index! == 1 set PARAM=%%b
        if !index! == 2 set PARAM=%%c
        if !index! == 3 set PARAM=%%d
        if !index! == 4 set PARAM=%%e
    )

    :: Normal value 20-35 or anomaly 100-200
    set /A chance=!random! %% 100
    if !chance! LSS %ANOMALY_CHANCE% (
        set /A VALUE=100 + (!random! %% 100)
    ) else (
        set /A VALUE=20 + (!random! %% 16)
    )

    start /B "" cmd /c manual-input.bat !LAT! !LON! !PARAM! !VALUE!
)

timeout /t 1 >nul
goto loop
