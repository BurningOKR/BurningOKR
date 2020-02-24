<br/><br/>
<p aling="center"><img src="/docs/ci/Logo_burningOKR_RGB_web.png" width="20%" height="20%" alt="Burning OKR"/></p>

<br/><br/><br/><br/><br/>

# BurningOKR 
Burning OKR is our vision to help consistently establish focus and alignment around company goals and embed transparency into the corporate culture.

BurningOKR has been developed as a web application with an Angular Frontend and Java Spring Boot Backend. As database Postgres SQL is used. 

***Status of this document: Work in progress***

## Getting started

## Software dependencies

### Postgres SQL

We use Postgres as SQL database for local development. Therefore it is necessary that PostgresSQL is installed locally on the development machines to allow database access.

Therefore Postgres must be downloaded first. A compatible version to the project is version 9.5.14. Other versions, especially higher ones, should also be compatible.

After the download the installer must be executed, an installation Direcotry and Data Direcotry must be selected, a superuser password (e.g. admin) must be chosen. The port remains at 5432, the language for the database cluster is German, Germany. After that PostgresSQL can be installed.

The last step is to uncheck Launch Stack Builder at exist. Now you have to configure PostgresSQL.

For this the pgAdmin must be started. In the object browser you have to connect to the local Postgres server with the previously defined superuser password. Then you have to create a new database named 'okr' and a new login role with the role name and password 'admin' and all role privileges.



### Node & npm

Node and npm are required for the Angular SPA. These can be downloaded and installed via installer. For Angular development, the Angular CLI must be installed with: 
> npm install -g @angular/cli


### JDK

For the backend the JDK 8 is required. The Open JDK 8 can be downloaded here.

##Source Code

The source code of the project consists of two parts. A Frontend SPA with the framework Angular and a Spring Boot project for the backend.


## Frontend
With git clone [tbd] the frontend project is cloned.

With Intellij you can now import the project.

Next you have to run npm install on the command line to download the dependencies. With npm start the application is started and can now be called via localhost:4200.


## Backend
With git clone [tbd] the backend project is cloned.

To open the project in IntelliJ, go to Import Project and select the file build.gradle in the folder okr-tool. In the dialog that opens you have to select the field use auto-import.

If a configuration was not created automatically to start the project in IntelliJ, a gradle configuration must be created. For the grandle project, okr-tool must be selected, and for tasks, bootRun must be entered.

If PostgresSQL has already been successfully installed and configured in the previous step, the Spring Boot Application can now be started.

The REST interfaces are listed under localhost:8080/swagger-ui.html and can be called there.

## Build and Test
Per gradlew build kann das Backend gebaut werden.

Per ng build kann das Frontend gebaut werden.
