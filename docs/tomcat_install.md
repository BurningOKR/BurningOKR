# Tomcat
## Getting Started
### Windows

1. Install PostgreSQL (Version 9.5 or higher): https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
    1. IMPORTANT: During installation you have to set a password for the postgres superuser and you have to choose a port number. You must remember these two values.
2. The PostgreSQL Installer also should have installed pgAdmin4. Search for pgAdmin4 on your computer and start it. This should open your browser. You may have to set a master password for pgAdmin on the first startup.
3. On the left, you should see your server browser. 

    ![Server Browser](./images/pgAdmin_browser.PNG)
    
    Expand all entries as shown in the image.
4. Right click on "Login/Group Roles" and choose "Create > Login/Group Role..."
    1. In the general tab, set the "Name" to "admin".
    2. In the definition tab, set a strong password.
    3. Click on "Save" to exit the window.
5. In the server browser right click on "Databases" and choose "Create > Database...".
    1. Give your database a name by filling out the database field. You can choose any name, but it is recommended to use a meaningful name like "okr" or "burningokr".
    2. Set the owner to "admin"
    
        ![Create Database](./images/pgAdmin_create_database.PNG)
        
    3. Click on "Save" to exit the window.
6. You should now see two databases and the admin login role in your server browser:
   
   ![Two Databases and a new Login Role](./images/pgAdmin_done.PNG)
   
   Your Database is now ready for usage. You can now close pgAdmin4.
7. Install Java 8: https://www.java.com/en/download/
8. Install Tomcat 9: https://tomcat.apache.org/download-90.cgi (Download the 32-bit/64-bit Windows Service Installer)
9. Go to https://github.com/BurningOKR/BurningOKR/releases/latest and download the .war file and the frontend-de.zip or frontend-en.zip (depends on your preferred language)
10. Rename the .war file to "api.war".
11. Place the api.war in the following directory: `C:\Program Files (x86)\Apache Software Foundation\Tomcat 9.0\webapps`. Once this is done, you should see that a new directory called "api" was automatically created within this directory.
12. Delete all files in the directory `C:\Program Files (x86)\Apache Software Foundation\Tomcat 9.0\webapps\ROOT`
13. Open the frontend-de/en.zip and move to the directory `fronted\dist\OKRFrontEnd`.
14. Copy all files within this directory to `C:\Program Files (x86)\Apache Software Foundation\Tomcat 9.0\webapps\ROOT`
15. Within the `ROOT` directory, create a new directory called `WEB-INF`
16. Place the `web.xml`, which can be downloaded [here](./files/web.xml) (right click the "raw" button and select "save as...") in the `WEB-INF` directory.
17. Configure BurningOKR. See [Configuration](#configuration-windows-and-linux).
18. Open your browser on http://localhost:8080/manager and login with your tomcat administrator account
19. Click "Start" on the "/api" application
    ![Tomcat start](./images/tomcat_start.PNG)
20. Done. You can now open your browser on http://localhost:8080 and see BurningOKR.

### Linux
1. Install PostgreSQL: 
    ```
    sudo apt install postgresql postgresql-contrib
    ```
2. Create the "admin" user role for BurningOKR: 
    ```
    sudo -u postgres createuser --interactive
    ```
    This should look like this:
    ````
   Enter name of role to add: admin
   Shall the new role be a superuser? (y/n) n
   Shall the new role be allowed to create databases? (y/n) n
   Shall the new role be allowed to create more new roles? (y/n) n
   ````
3. Create a database for BurningOKR: 
    ```
    sudo -u postgres createdb okr -O admin
   ```
4. Install Java 8: 
    ```
    sudo apt install openjdk-8-jre-headless
    ```
5. Create a usergroup and user for Tomcat: 
    ```
    sudo groupadd tomcat
    sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
    ```
6. Download and extract Tomcat: 
    ```
    cd /tmp
    curl -O https://mirror.synyx.de/apache/tomcat/tomcat-9/v9.0.39/bin/apache-tomcat-9.0.39.tar.gz
    sudo tar xzvf apache-tomcat-9*tar.gz -C /opt/tomcat --strip-components=1
    ```
7. Add privileges for the Tomcat user to access the Tomcat directory:
    ```
   cd /opt/tomcat
   sudo chgrp –R tomcat /opt/tomcat
   sudo chmod –R g+r conf
   sudo chmod g+x conf
   sudo chown –R tomcat webapps/ work temp/ logs
   ```
8. Find the "JAVA_HOME" path and copy it:
    ```
   sudo update-java-alternatives -l
   ```
   ![update-java-alternatives output](./images/update_java_alternatives_output.PNG)
   Copy the path from the output.
9. Create a new Systemd service by creating a tomcat.service file:
    ```
    sudo nano /etc/systemd/system/tomcat.service
    ```
    And paste this content into it:
    ```
    [Unit]
    Description=Apache Tomcat Web Application Container
    After=network.target
    
    [Service]
    Type=forking
    
    Environment="JAVA_HOME=<YOUR JAVA_HOME PATH FROM STEP 9>"
    Environment="CATALINA_PID=/opt/tomcat/temp/tomcat.pid"
    Environment="CATALINA_HOME=/opt/tomcat"
    Environment="CATALINA_BASE=/opt/tomcat"
    Environment='CATALINA_OPTS=-Xms512M –Xmx1024M –server –XX:+UserParallelGC’
    Environment=’JAVA_OPTS=-Djava.awt.headless=true Djava.security.egd=file:/dev/./urandom’
    
    ExecStart=/opt/tomcat/bin/startup.sh
    ExecStop=/opt/tomcat/bin/shutdown.sh
    
    User=tomcat
    Group=tomcat
    UMask=0007
    RestartSec=10
    Restart=always
    
    [Install]
    WantedBy=multi-user.target
    ```
    Do not forget to insert your JAVA_HOME path fromstep 9 into the first `Environment` entry.

    **Save** and **Exit** the file by using `Ctrl+X`, followed by `y`es and `Enter`.
10. Reload the system daemon and start the tomcat service, so that the changes take place:
    ```
    sudo systemctl daemon-reload
    sudo systemctl start tomcat
    sudo systemctl status tomcat
    ```
    The status should be **active (running)**
11. Allow tomcat through the firewall.
    ```
    sudo ufw allow 8080
    ```
12. Create a tomcat user for the web management console.
    You need to do this to access the manager app, that comes with tomcat.
    Start by editing the `tomcat-users.xml` file
    ```
    sudo nano /opt/tomcat/conf/tomcat-users.xml
    ```
    Then add the following lines above `</tomcat-users>` (at the bottom of the file)
    ```xml
        <role rolename="manager-gui"/>
        <role rolename="admin-gui"/>
        <user username="username" password="password" roles="manager-gui,admin-gui"/>
    </tomcat-users>
    ```
    Change `username` and `password` to a secure account.
    
    Restart tomcat afterwards:
    ```
    sudo systemctl restart tomcat
    ```
13. Download the latest BurningOKR release:
    1. Change to temp directory
        ```
        cd /tmp
        ```
    2. Download the backend .war file:
        ```
        wget https://github.com/$(wget https://github.com/burningokr/burningokr/releases/latest -O - | egrep '/.*/.*/.*burning-okr-app-.*war' -o)
        ```
    3. Rename the .war file to api.war
        ```
        mv burning-okr-app-*.war api.war
        ```
    4. Download the frontend .zip file:
        1. **If you want german language, use this command**
            ```
            wget https://github.com/$(wget https://github.com/burningokr/burningokr/releases/latest -O - | egrep '/.*/.*/.*frontend-de.zip' -o)
            ```
        2. **If you want english language, use this command**
            ```
            wget https://github.com/$(wget https://github.com/burningokr/burningokr/releases/latest -O - | egrep '/.*/.*/.*frontend-en.zip' -o)
            ```
14. Install the BurningOKR release:
    1. Install the war file:
        ```
        sudo mv /tmp/api.war /opt/tomcat/webapps/api.war
        ```
    2. Install the zip file:
        1. Clear the tomcat ROOT directory:
            ```
            sudo rm -r /opt/tomcat/webapps/ROOT/*
            ```
        2. Install unzip
            ```
            sudo apt install unzip
            ```
        3. Unzip the zip file to the ROOT directory:
            ```
            sudo unzip -j /tmp/frontend-*.zip "frontend/dist/OKRFrontEnd/*" -d /opt/tomcat/webapps/ROOT/
            ```
        4. Create a WEB-INF directory
            ```
            sudo mkdir /opt/tomcat/webapps/ROOT/WEB-INF
            ```
        5. Download a web.xml and place it in the WEB-INF directory
            ```
            cd /opt/tomcat/webapps/ROOT/WEB-INF
            sudo wget https://raw.githubusercontent.com/BurningOKR/BurningOKR/development/docs/files/web.xml
            ```
15. Configure BurningOKR. See [Configuration](#configuration-windows-and-linux).
16. Open your browser on http://localhost:8080/manager and login with your tomcat administrator account which was created in step 12.
17. Click "Start" on the "/api" application
    ![Tomcat start](./images/tomcat_start.PNG)
18. Done. You can now open your browser on http://localhost:8080 and see BurningOKR.


### Configuration (Windows and Linux)
1. Go to the webapps directory of your tomcat server
    1. For Windows: `C:\Program Files (x86)\Apache Software Foundation\Tomcat 9.0\webapps`
    2. For Linux: `cd /opt/tomcat/webapps`
2. Go to `api\WEB-INF\classes`
3. Edit the `application.yaml` and overwrite everything with the following sample:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:<PORT>/<DATABASE NAME>?useSSL=false
    username: admin
    password: <admin Password>

  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect # For postgres
        format_sql: true

  flyway:
    baseline-on-migrate: true

  mail:
    username: <Email username>
    password: <Email password>
    host: <Email server url>
    port: <Email server port>
    properties:
      mail:
        smtp:
          socketFactory:
            class: javax.net.ssl.SSLSocketFactory
          auth: true
      test-connection: false

  messages:
    basename: messages
    pid:
      file: ./shutdown.pid

## This section is optional.
## If you want to use an Azure Active Directory for authentication, uncomment this.
#
#azure:
#  ad:
#    issuer: <Azure issuer>
#    azureGroups:
#      - name: <Azure ad group name>
#        id: <Azure ad group id>
#
#security:
#  oauth2:
#    client:
#      clientId: <OAuth client id>
#      clientSecret: <OAuth client secret>
#      accessTokenUri: <OAuth access token url>
#      userAuthorizationUri: <OAuth use authorization token url>
#      clientAuthenticationScheme: form
#      scope: openid
#      grant-type: client_credentials
#      auto-approve-scopes: '.*'
#      token-name: access_token
#    resource:
#      userInfoUri: https://graph.microsoft.com/v1.0/me/
#      preferTokenInfo: false

system:
  configuration:
    auth-mode: <Your preferred auth-mode ("local" or "azure")>
    api-endpoint: ""
    token-endpoint-prefix: "/api"
```

4. Insert the port and the database name of your Postgres database, that you have just created under `spring: > datasource: > url: ...`
5. Insert the password of the admin role of your Postgres server under `spring: > datasource: > password: ...`
6. You can insert the url, port, username and password of your mail server if you have one under `spring: > mail: > ....`. Otherwise remove the placeholders and leave these configurations empty.
7. Decide if you want to use a local user database (also saved in the postgres database) or if you want to use Azure Active Directory as your userbase by replacing the placeholder under `system: > configuration: > auth-mode: ...` with either local or azure.
    1. **When using an Azure Active Directory as the userbase, you also need to do the following steps. You do not need to this, when you are using the local user database.**
        1. Uncomment the Azure Active Directory configuration by removing the first "#" of each line.
        2. Replace `<OAuth client id>`, `<OAuth client secret>`, `<OAuth access token url>` and `<OAuth use authorization token url>` with the corresponding values from your Azure Active Directory App registration.
        3. Replace `<Azure issuer>` with the azure issuer URI. e.g. `https://<login-provider>/<tenant-id>/v2.0`
        4. Add as many Azure Active Directory Groups as you want. All users from these groups will be authorized, to use the BurningOKR Tool. The rest will not be able to use the tool. You need to specify the `<Azure ad group name>` and the `<Azure ad group id>`.
        5. To add more groups, you need to add more entries to the list. Here is an example:
```yaml
azureGroups:
  - name: <Azure ad group name>
    id: <Azure ad group id>
  - name: <Azure ad group name>
    id: <Azure ad group id>
  - name: <Azure ad group name>
    id: <Azure ad group id>
```
