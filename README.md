<p align="center"><b> ***Status of this document: Work in progress*** </p></b>

<p align="center"><img src="/docs/ci/Logo_burningOKR_RGB_web.png" width="35%" height="35%" alt="Burning OKR"/></p>

<p align="center">
<a href="http://www.burningokr.org" target="_blank">home</a> | <a href="https://burning-okr.gitbook.io/burningokr" target="_blank">documentation</a> </a></p>
<br/><br/><br/>

# BurningOKR 
Burning OKR is our vision to help consistently establish focus and alignment around company goals and embed transparency into the corporate culture.

BurningOKR has been developed as a web application with an Angular Frontend and Java Spring Boot Backend. As database Postgres SQL is used. 


## Getting started

## Software dependencies

### Postgres SQL

We use Postgres as SQL database for local development. Therefore it is necessary that PostgresSQL is installed locally on the development machines to allow database access.

Therefore Postgres must be downloaded first. A compatible version to the project is version 9.5.14. Other versions, especially higher ones, should also be compatible.

After the download the installer must be executed, an installation Direcotry and Data Direcotry must be selected, a superuser password (e.g. admin) must be chosen. The port remains at 5432, the language for the database cluster is German, Germany. After that PostgresSQL can be installed.

The last step is to uncheck Launch Stack Builder at exist. Now you have to configure PostgresSQL.

For this the pgAdmin must be started. In the object browser you have to connect to the local Postgres server with the previously defined superuser password. Then you have to create a new database named 'okr' and a new login role with the role name and password 'admin' and all role privileges.



### Node & npm

Node and npm are required for the Angular SPA. These can be downloaded and installed via installer. For Angular development, the Angular CLI must be installed with `npm install -g @angular/cli`


### JDK

For the backend the JDK 8 is required. The Open JDK 8 can be downloaded here.

## Source Code

The source code of the project consists of two parts. A Frontend SPA with the framework Angular and a Spring Boot project for the backend.

With `git clone https://github.com/BurningOKR/BurningOKR` the comlete repository (including frontend and backend) is cloned.


## Frontend

With Intellij you can now import the frontend project.


Next you have to run `npm install` on the command line to download the dependencies. 


With `npm start` the application is started and can now be called via localhost:4200.


## Backend

To open the project in IntelliJ, go to Import Project and select the file build.gradle in the folder backend. In the dialog that opens you have to select the field use auto-import.

If a configuration was not created automatically to start the project in IntelliJ, a gradle configuration must be created. For the grandle project, okr-tool must be selected, and for tasks, bootRun must be entered.

If PostgresSQL has already been successfully installed and configured in the previous step, the Spring Boot Application can now be started.

The REST interfaces are listed under `localhost:8080/swagger-ui.html` and can be called there.

## Build and Test

With `gradlew build` the backend can be built.

With `ng build` the frontend can be built.


## contribute

Thank you to all the people and bots who already contributed to BurningOKR!

<!-- generate new contributor list.. https://contributors-img.firebaseapp.com/ -->
<a href="https://github.com/BurningOKR/BurningOKR/graphs/contributors"><img src="https://contributors-img.firebaseapp.com/image?repo=BurningOKR/BurningOKR"/>
</a>

<br/>


## contact
<br/>

You can write an [E-Mail](mailto:burningokr@brockhaus-ag.de) or mention our twitter account [@BurningOKR](https://twitter.com/BurningOkr).

<br/>

## license
<br/>

BurningOKR was initially developed as part of a research project at [BROCKHAUS AG](http://brockhaus-ag.de) in Lünen.

Only an Open Source solution can unfold its true potential. That's why we released it on GitHub as an open-source project under the Apache 2.0 license.

See [LICENSE.txt](LICENSE.txt)

