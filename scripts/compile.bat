@echo off
REM Create the build directory if it doesn't exist
if not exist "..\build" (
    mkdir "..\build"
)

del sources.txt 2>nul

setlocal EnableDelayedExpansion
set FILES=

for /R "..\src" %%f in (*.java) do (
    echo %%f >> sources.txt
)
type sources.txt
javac -d "..\build" -sourcepath "..\src" @sources.txt

if %errorlevel% neq 0 (
    echo [!] Compilation failed.
    exit /b %errorlevel%
)

echo Compilation successful.
pause 

