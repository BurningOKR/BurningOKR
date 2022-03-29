@ECHO OFF
CLS

ECHO #### Cloning project ####
echo.
git clone https://github.comBurningOKR/BurningOKR
echo.

set p c=Is Node.js LTS already installed [yN]
if i %c% == Y GOTO :install_angular
if i %c% == N GOTO :install_nodejs
GOTO :install_nodejs

:install_nodejs
ECHO #### Installing Node.js LTS v16.14.2 64bit ####
powershell -Command (New-Object Net.WebClient).DownloadFile('https://nodejs.org/dist/v16.14.2/node-v16.14.2-x64.msi', 'node-v16.14.2-x64.msi')
ECHO Follow the installer.
msiexec i node-v16.14.2-x64.msi
ECHO Deleting the installer.
DEL node-v16.14.2-x64.msi
GOTO :install_angular

:install_angular
ECHO #### Installing AngularCLI ####
echo.
call npm install @angularcli -g
cd BurningOKR\frontend
echo.
GOTO :install_npm_dependencies

:install_npm_dependencies
ECHO #### Installing dependencies ####
echo.
call npm install
echo.

set p c=Do you want to use PostgreSQL inside a Docker container? [Y/n]
if i %c% == Y GOTO :postgres_with_docker
if i %c% == N GOTO :postgres_without_docker
GOTO :postgres_with_docker

:install_docker
ECHO #### Installing Docker Desktop 64bit ####
ECHO Download-URL http://swww.docker.com/get-started
set p c=Done [Enter]
GOTO :docker_setup

:postgres_with_docker
set p c=Is Docker-Desktop already installed? [y/N]
if i %c% == Y GOTO :docker_setup
if i %c% == N GOTO :install_docker
GOTO :install_docker

:docker_setup
ECHO #### Starting PostgreSQL Docker Container ####
set p c=Do you want to use the default password? [Y/n]
if i %c% == Y GOTO :default_postgres_config
if i %c% == N GOTO :custom_postgres_config
GOTO :default_postgres_config

:default_postgres_config
docker run -p 5434:5432 --name okr-postgres -e POSTGRES_PASSWORD=4c0K8sJGcxIercJDlmhs -e POSTGRES_DB=okr -d postgres
GOTO :finished

:custom_postgres_config
set p pwd=Which POSTGRES_PASSWORD do you want?
docker run -p 5434:5432 --name okr-postgres -e POSTGRES_PASSWORD=%pwd% -e POSTGRES_DB=okr -d postgres
GOTO :finished

:postgres_without_docker
ECHO You decided to install PostgreSQL manually.
ECHO You can find more information here:
ECHO https://github.com/BurningOKR/BurningOKR/blob/masterdocspostgres_install.md#Windows
ECHO Install PostgreSQL (Version 9.5 or higher)
ECHO https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
GOTO :finished

:finished
ECHO #### Installation completed ####
echo.
ECHO You can close this window now.
pause nul