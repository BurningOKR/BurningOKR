### Configuration (Windows and Linux)
This documentation ist for the local dev enviroment.
For the configuration of the docker containers, please head to [Configuration](/docker/README.md#configuration-windows-and-linux).

1. Go to "backend/burning-okr/burning-okr-app/src/main/resources/".
2. Copy the "application.yaml.sample" and create an "application-local.yaml".
3. Replace the placeholders with values.
* url
* username
* password

4. If not needed, delete the placeholders at mail.

5. Under auth-mode select `local` or `azure`. If you are not sure, use `local`.
For more information about the usage of Azure-AD look in the [docker configuration](/docker/README.md#configuration-windows-and-linux).
6. Optional: Please note that the yaml application will be overwritten if you deploy a new version.
   To prevent it from happening go to the tomcat context-configuration file under `<tomcat9-installation>/conf/context.xml` and enter the following in the context
```xml
    <Parameter name="spring.config.location" value="C:\Program Files\Apache Software Foundation\Tomcat 9.0\conf\application.yaml" />
</Context>
```
![example-config](./images/tomcat-context.png)

7. Save

You have now configured the application.yaml :)
