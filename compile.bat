@echo off
echo Compiling Philosofit MySQL Application...
echo.

REM Create output directory
if not exist "bin" mkdir bin

REM Compile Java files
javac -d bin src/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Run the application with: run.bat
) else (
    echo Compilation failed! Please check for errors.
)

pause
