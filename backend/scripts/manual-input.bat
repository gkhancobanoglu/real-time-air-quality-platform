@echo off
setlocal enabledelayedexpansion

set LAT=%1
set LON=%2
set PARAM=%3
set VALUE=%4

curl -X POST http://localhost:8081/api/air/data ^
  -H "Content-Type: application/json" ^
  -d "{\"latitude\": %LAT%, \"longitude\": %LON%, \"parameter\": \"%PARAM%\", \"value\": %VALUE%}"
