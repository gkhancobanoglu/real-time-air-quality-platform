@echo off
setlocal enabledelayedexpansion

if "%~10"=="" (
  echo Usage: manual-input.bat ^<lat^> ^<lon^> ^<co^> ^<no^> ^<no2^> ^<o3^> ^<so2^> ^<pm2_5^> ^<pm10^> ^<nh3^>
  exit /b 1
)

set LAT=%1
set LON=%2
set CO=%3
set NO=%4
set NO2=%5
set O3=%6
set SO2=%7
set PM25=%8
set PM10=%9
set NH3=%10

echo Sending request to http://localhost:8081/api/air/data ...
curl -v -X POST http://localhost:8081/api/air/data ^
  -H "Content-Type: application/json" ^
  -d "{\"latitude\": %LAT%, \"longitude\": %LON%, \"co\": %CO%, \"no\": %NO%, \"no2\": %NO2%, \"o3\": %O3%, \"so2\": %SO2%, \"pm2_5\": %PM25%, \"pm10\": %PM10%, \"nh3\": %NH3%}"

echo Exit Code: %ERRORLEVEL%
