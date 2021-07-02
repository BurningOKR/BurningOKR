# Install and Setup PostgreSQL (Docker)

## Overview
You haven't created a database yet? Then you are at the right place!

After the installation you will have an running postgres instance with the following configuration:

* Host: localhost
* Port: 5432
* User: postgres
* Password: 4c0K8sJGcxIercJDlmhs

## Windows
1. Install Docker
2. Execute the following command:
   
    `docker run -p 5432:5432 --name okr-postgres -e POSTGRES_PASSWORD=4c0K8sJGcxIercJDlmhs -e DB=okr -d postgres`

3. The Database is now ready for usage.

With the credentials mentioned above you can now connect to the database using IntelliJ, Datagrip or pgAdmin.
1. `docker rm okr-postgres -f`
2. `docker run -p 5432:5432 --name okr-postgres -e POSTGRES_PASSWORD=4c0K8sJGcxIercJDlmhs -e DB=okr -d postgres`

Done!
###Recreating Container
If you want to recreate the container, just do the following:


You can now go back to the Installation Tutorial.
