# Docker
## Getting Started

Docker Desktop requires Windows 10 Pro or Enterprise version 15063 to run

### Windows
1. Install Docker: https://www.docker.com/products/docker-desktop
2. Install Git: https://gitforwindows.org/
3. Open Powershell and clone the BurningOKR Git Repository: ```git clone https://github.com/BurningOKR/BurningOKR.git```
4. Open the BurningOKR\Docker directory
5. Configure your docker container. See [Configuration](#configuration-windows-and-linux)
6. Open Powershell within the BurningOKR\Docker directory (Press shift and right click -> open Powershell)
7. Run ```docker-compose build```. This will take several minutes.
8. Run ```docker-compose up```.
9. Done. You should now be able to open your browser on ```localhost:4200``` and see the BurningOKR Tool.

### Linux

1. Install [Docker](https://docs.docker.com/engine/install/): 
	1. Pull install script ``curl -fsSL https://get.docker.com -o get-docker.sh``
	2. Execute install script ``sudo sh get-docker.sh`` 
	3. Add current user to userlist ``sudo usermod -aG docker $USER``
	4. Logout and Login again for iii. to take effect. 
2. Install [Docker-Compose](https://docs.docker.com/compose/install/) 
	1. Install Docker-Compose ```sudo curl -L "https://github.com/docker/compose/releases/download/1.25.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose```
	2. Add Execution Permission ``sudo chmod +x /usr/local/bin/docker-compose``
3. Install Git ``sudo apt install git-all``
4. Open Terminal and clone the BurningOKR Git Repository: ```git clone https://github.com/BurningOKR/BurningOKR.git```
5. Open the BurningOKR\docker directory
6. Configure your docker container. See [Configuration](#configuration-windows-and-linux)
7. Open Terminal and change directory to BurningOKR\docker.
8. Run ```docker-compose build```. This will take several minutes.
9. Run ```docker-compose up```.
10. Done. You should now be able to open your browser on ```localhost:4200``` and see the BurningOKR Tool.


### Configuration (Windows and Linux)


1. There are two sample environment files within the BurningOKR\Docker directory: ```backend.env.sample``` 
and ``postgres.env.sample`` Rename both environment files to ``backend.env`` and `postgres.env`.
2. Replace the ``<username>``, ``<password>`` and ``<database>`` placeholder in the `postgres.env`. You can choose 
these freely.
3. Replace the ``<db-connection-string>`` placeholder with the jdbc string corresponding with the favored 
database location (E.g. Line 13 for a Docker based Database, recommended for testing). Remove the # at the front. 
4. Replace ``<database>``, ``<db-username>`` and ``<db-password>`` with the same as set in the ``postgres.env``.
5. If you want the software to send mails, replace ``<smtp-username``, ``<smtp-password>``, ``<smtp-host>``
and ``<smtp-port>``. You might need to acquire these from your system administrator. If mails are not wanted, delete 
placeholder.
6. Decide if you want to use a local user database (also saved in the postgres database) or if you want to use 
Azure Active Directory as your userbase by replacing ``<local|aad>`` with either ``local`` or ``aad``. 
    1. **When using aad as the userbase, you also need to do the following steps. You do not need to this,
    when you are using the local user database.**
        1. Replace the ``<azure-issuer>`` placeholder with your azure issuer URI. e.g. ``https://<login-provider>/<tenannt-id>/v2.0``
        2. Replace ``<azure-client-id>``, ``<azure-client-secret>``, ``<azure-access-token-uri>``, ``<azure-user-authorization-uri>`` and ``<azure-user-info-uri>``
        with the corresponding values from your Azure Active Directory App registration.
        3. Add as many Azure Active Directory Groups as you want. All users from these groups will be authorized, to use the BurningOKR Tool.
        The rest will not be able to use the tool. You need to specify the ``<azure-group-name>`` and the ``<azure-group-id>``.
        4. To add a group, you need to add more ``AZURE_AD_AZUREGROUPS[0]_NAME=`` and ``AZURE_AD_AZUREGROUPS[0]_ID=`` configuration keys.
        Replace the ``[0]`` with a ``[1]`` for the second group, with a ``[2]`` for the third group, and so on. Here is an example for three azure groups:
        ```yaml
       AZURE_AD_AZUREGROUPS[0]_NAME=<azure-group-name>
       AZURE_AD_AZUREGROUPS[0]_ID=<azure-group-id>
       AZURE_AD_AZUREGROUPS[1]_NAME=<second-azure-group-name>
       AZURE_AD_AZUREGROUPS[1]_ID=<second-azure-group-id>
       AZURE_AD_AZUREGROUPS[2]_NAME=<third-azure-group-name>
       AZURE_AD_AZUREGROUPS[2]_ID=<third-azure-group-id>
       ```

## Developer Information

The application consists of three containers. Frontend, Backend and Database. 

The Databases' volume is saved at the default location (Windows: ``C:\Users\Public\Documents\Hyper-V\Virtual hard disks`` 
| Linux: ``/var/lib/docker/volumes``) and is called docker_pgdata. 

The ports used by the application can be configured in the docker-compose.yml.

The network allows external access to port 4200 for the frontend and port 54023 for the database (in case you want to 
access it with pgAdmin for example). The backend can't be called from the outside. 

After first application start, the database persists even if you rebuild the containers.

