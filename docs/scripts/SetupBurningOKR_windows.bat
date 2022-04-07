@ECHO OFF
@REM Parameter
SET nodeVersion=v16.14.2
SET defaultPostgresPassword=4c0K8sJGcxIercJDlmhs

ECHO #### Cloning project ####
git clone https://github.com/BurningOKR/BurningOKR
GOTO :nodejs_question

:nodejs_question
SET /p isInstalled=Is Node.js LTS already installed [y/N]
if /i %isInstalled% == Y GOTO :install_angular
GOTO :install_nodejs

:install_nodejs
ECHO #### Installing Node.js LTS v16.14.2 64bit ####
powershell -Command (New-Object Net.WebClient).DownloadFile('https://nodejs.org/download/release/%nodeVersion%/node-%nodeVersion%-x64.msi', 'nodejs.msi')
ECHO Follow the installer.
msiexec /i nodejs.msi
DEL nodejs.msi
GOTO :install_angular

:install_angular
ECHO #### Installing Angular CLI ####
CALL npm install @angular/cli -g
GOTO :install_npm_dependencies

:install_npm_dependencies
ECHO #### Installing npm dependencies ####
CD BurningOKR\frontend
CALL npm install
CD ..
GOTO :with_docker_question

:with_docker_question
SET /p installPostgres=Do you want to use PostgreSQL inside a Docker container? [Y/n]
if /i %installPostgres% == N GOTO :postgres_without_docker
GOTO :postgres_with_docker

:postgres_with_docker
SET /p isDockerInstalled=Is Docker already installed? [y/N]
if /i %isDockerInstalled% == Y GOTO :docker_setup
GOTO :install_docker

:install_docker
ECHO #### Installing Docker Desktop 64bit ####
powershell -Command (New-Object Net.WebClient).DownloadFile('https://desktop.docker.com/win/main/amd64/Docker%20Desktop%20Installer.exe', 'docker.exe')
docker.exe
DEL docker.exe
SET /p wait=Press enter to continue.
GOTO :docker_setup

:docker_setup
ECHO #### Starting PostgreSQL Docker Container ####
docker run -p 5434:5432 --name okr-postgres -e POSTGRES_PASSWORD=%defaultPostgresPassword% -e POSTGRES_DB=okr -d postgres
GOTO :finished

:postgres_without_docker
ECHO #### Manual installation of PostgreSQL ####
ECHO You need PostgreSQL version 9.5 or higher.
ECHO You can find more information here:
ECHO https://github.com/BurningOKR/BurningOKR/blob/masterdocspostgres_install.md#Windows
powershell -Command (New-Object Net.WebClient).DownloadFile('https://sbp.enterprisedb.com/getfile.jsp?fileid=1258047', 'postgres.exe')
postgres.exe
DEL postgres.exe
SET /p wait=Press enter to continue.
GOTO :finished

:finished
ECHO #### Installation completed ####
ECHO You can close this window now.
PAUSE >nul