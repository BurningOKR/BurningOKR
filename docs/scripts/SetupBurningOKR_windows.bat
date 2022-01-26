@ECHO OFF
cls
ECHO #### Cloning project ####
echo.
git clone https://github.com/BurningOKR/BurningOKR
echo.
ECHO #### Installing Angular/CLI ####
echo.
call npm install @angular/cli -g
cd BurningOKR\frontend
echo.
ECHO #### Installing dependencies ####
echo.
call npm install
echo.
echo.
ECHO #### Installed successfully! ####
echo.
ECHO You can close this window now.
pause >nul