<p align="center"><img src="/docs/ci/Logo_burningOKR_RGB_web.png" width="35%" height="35%" alt="Burning OKR"/></p>

<br/><br/><br/>

# BurningOKR
Burning OKR is our vision to help consistently establish focus and alignment around company goals and embed transparency into the corporate culture.

BurningOKR has been developed as a web application with an Angular Frontend and Java Spring Boot Backend. As a database, Postgres SQL is used.

## Quick start (familiar with Docker)
When you have Docker and Docker-Compose installed you can proceed with the next steps, otherwise please install Docker and Docker-Compose first.  
You can use our docker-compose file for easy use and compatibility!  
1. Download the [TODO update][docker-compose-prod.yml](/docker/docker-compose-prod.yml) file
2. Download [TODO update][backend.env.sample](/docker/backend.env.sample) file and rename it to backend.env
3. Download [TODO update][postgres.env.sample](/docker/postgres.env.sample) file and rename it to postgres.env
4. Now fill in your configurations in the two downloaded .env-files
5. Hint: When you don't want to use Azure or a SMTP-Mailserver just comment these parts in the .env-files out and they won't be used. For more information read the full docs.

6. After that you are good to go and you can run `docker compose -f docker-compose-prod.yml up` in the directory where the previously downloaded files are saved. Hint: When you want to reuse the console windows add a `-d` to the compose command to run in detached mode!

## Installation
You can install BurningOKR using the following technologies.
- Docker [Tutorial](/docker/README.md) *(Recommended for beginners)*

// [TODO important?]- Tomcat [Tutorial](/docs/tomcat_install.md)

## Getting started with the development
We are already working on a faster and easier installation process, so stay tuned.

### Checklist
0. Please follow our [Code Guidelines](/CODE_GUIDELINES.md) and [General information about implementations](/docs/developer_readme.md)
   0.1 You may also read the [Frontend-Readme](/frontend/README.md)
1. Install [PostgreSQL](/docs/postgres_install.md) 9.5.14 or higher or [MSSQL](/docs/mssql_install.md)
2. Install JDK 17. The Open JDK 17 can be downloaded [here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
3. Install the (!) LTS Version of node & npm [here](https://nodejs.org/en/download/)
4. Download the following [Batch-Script](/docs/scripts/SetupBurningOKR_windows.bat) and put it in the folder, where you want the project to be. Execute the script.  
Or execute the following command:  
```
curl https://raw.githubusercontent.com/BurningOKR/BurningOKR/master/docs/scripts/SetupBurningOKR_windows.bat > temp.bat && temp.bat && DEL temp.bat
```   
The following tasks will be done by the script:
   1. Clone the Repository with `git clone https://github.com/BurningOKR/BurningOKR`. (The whole repository, including frontend and backend)
   2. Install NodeJS (optional)
   3. Install Docker (optional)
   4. Install Angular `npm install @angular/cli -g`
   5. Install Dependencies (in the frontend folder) `npm install`
   6. Install Postgres (optional via Docker) 

5. Open the frontend-folder and the backend-folder in seperate windows in your IDE. (We recommend IntelliJ)
6. [Configure](/docs/configure.md) the backend.
7. Start the frontend with `npm start`
8. Start the backend with the gradle `Start backend` configuration. 
   1. Alternatively create the configuration like \
   ![intelliJ-Run-Configuration](./docs/images/boot-run-config.png)

### Source Code

The source code of the project consists of two parts. A Frontend SPA with the framework **Angular** and a **Spring Boot** project for the backend.

With `git clone https://github.com/BurningOKR/BurningOKR` the complete repository (including frontend and backend) is cloned.


### Frontend

With IntelliJ, you can now import the frontend project.


Next, you have to run `npm install` on the command line to download the dependencies.


With `npm start` the application is started and can now be called via `http://localhost:4200`.


### Backend

To open the project in IntelliJ, go to Import Project and select the file build.gradle in the folder backend. In the dialog that opens you have to select the field use auto-import.

If a configuration was not created automatically to start the project in IntelliJ, a gradle configuration must be created. For the gradle project, okr-tool must be selected, and for tasks, bootRun must be entered.
![intelliJ-Run-Configuration](./docs/images/boot-run-config.png)
The backend project uses [Project Lombok](https://projectlombok.org). To use Project Lombok in your IDE, please refer to [using Lombok](https://projectlombok.org/setup/overview).

If PostgresSQL has already been successfully installed and configured in the previous step, the Spring Boot application can now be started.

To start the application without IntelliJ, switch to backend/burning-okr and run `./gradlew bootRun --scan --scan -Dspring.profiles.active=local`. On some UNIX systems there might be some issues with gradle regarding finding the JDK.
If some weird exceptions occur, try running `export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk<version>.jdk/Contents/Home` (replace the path with the actual path of your JDK). Then, restart your shell or run `source ~/.bash_profile`.

The REST interfaces are listed under `localhost:8080/swagger-ui.html` and can be called there.

### Build and Test

With `gradlew build` the backend can be built.

With `ng build` the frontend can be built.

### FAQ

* **I get some errors with npm install (python2, node-sass, node-gyp):** <br>
  Use the LTS version of node, not the current! https://nodejs.org/en/download/


* **I get a _entityManagerFactory Persistence Exception_ / error on instancing entityManagerFactory when starting the backend**<br>
  Please make sure to use JDK17 on Tomcat.
  You can check the JDK Version on `localhost:8080/manager`. Then you can log in with the tomcat user for the gui.
  It should look like this
  //TODO raus damit ![tomcat-jdk](./docs/images/tomcat-jdk.png)


* **I can't log in into the tomcat manager gui**
  Create a tomcat user for the web management console.
  You need to do this to access the manager app, that comes with tomcat.
  Start by editing the `tomcat-users.xml` file.
  Then add the following lines above `</tomcat-users>` (at the bottom of the file)
    ```xml
    <tomcat-users>
        <role rolename="manager-gui"/>
        <role rolename="admin-gui"/>
        <user username="username" password="password" roles="manager-gui,admin-gui"/>
    </tomcat-users>
    ```
  Change `username` and `password` to a secure account.

## Contribute

Thank you to all the people and bots who already contributed to BurningOKR!

<!-- generate new contributor list.. https://contributors-img.firebaseapp.com/ -->
<a href="https://github.com/BurningOKR/BurningOKR/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=BurningOKR/BurningOKR" />
</a>

Made with [contributors-img](https://contrib.rocks).

## Contact

You can write an [E-Mail](mailto:burningokr@brockhaus-ag.de) or mention our Twitter account [@BurningOKR](https://twitter.com/BurningOkr).

## License

BurningOKR was initially developed as part of a training project at [BROCKHAUS AG](http://brockhaus-ag.de) in Lünen.

Only an Open Source solution can unfold its true potential. That's why we released it on GitHub as an open-source project under the Apache 2.0 license.

See [LICENSE.txt](LICENSE.txt)

