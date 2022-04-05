@ECHO OFF

CLS
ECHO #### Cloning project ####
git clone https://github.com/BurningOKR/BurningOKR
GOTO :nodejs_question

:nodejs_question
CLS
SET /p isInstalled=Is Node.js LTS already installed [y/N]
if i %isInstalled% == Y GOTO :install_angular
if i %isInstalled% == N GOTO :install_nodejs
GOTO :install_nodejs

:install_nodejs
CLS
ECHO #### Installing Node.js LTS v16.14.2 64bit ####
powershell -Command (New-Object Net.WebClient).DownloadFile('https://nodejs.org/dist/v16.14.2/node-v16.14.2-x64.msi', 'nodejs.msi')
ECHO Follow the installer.
msiexec /i nodejs.msi
DEL nodejs.msi
GOTO :install_angular

:install_angular
CLS
ECHO #### Installing Angular CLI ####
CALL npm install @angular/cli -g
CD BurningOKR\frontend
GOTO :install_npm_dependencies

:install_npm_dependencies
CLS
ECHO #### Installing npm dependencies ####
CALL npm install
GOTO :with_docker_question

:with_docker_question
CLS
SET /p choice=Do you want to use PostgreSQL inside a Docker container? [Y/n]
if i %choice% == Y GOTO :postgres_with_docker
if i %choice% == N GOTO :postgres_without_docker
GOTO :postgres_with_docker

:postgres_with_docker
CLS
SET /p isDockerInstalled=Is Docker already installed? [y/N]
if i %isDockerInstalled% == Y GOTO :docker_setup
if i %isDockerInstalled% == N GOTO :install_docker
GOTO :install_docker

:install_docker
CLS
ECHO #### Installing Docker Desktop 64bit ####
SET /p wait=Press enter to open the docker website.
START https://www.docker.com/get-started
SET /p wait=Press enter to continue.
GOTO :docker_setup

:docker_setup
CLS
ECHO #### Starting PostgreSQL Docker Container ####
docker pull postgres
docker run -p 5434:5432 --name okr-postgres -e POSTGRES_PASSWORD=4c0K8sJGcxIercJDlmhs -e POSTGRES_DB=okr -d postgres
GOTO :finished

:postgres_without_docker
ECHO #### Manual installation of PostgreSQL ####
ECHO You need PostgreSQL version 9.5 or higher.
ECHO You can find more information here:
ECHO https://github.com/BurningOKR/BurningOKR/blob/masterdocspostgres_install.md#Windows
SET /p c=Press enter to open the download link.
START https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
SET /p c=Press enter if you want to continue.
GOTO :finished

:finished
ECHO #### Installation completed ####
ECHO You can close this window now.
PAUSE >nul