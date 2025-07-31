@echo off
echo Starting Philosofit MySQL Application...
echo.

REM Check if compiled
if not exist "bin" (
    echo Application not compiled yet. Running compile.bat first...
    call compile.bat
)

REM Run the application
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" Main

pause
