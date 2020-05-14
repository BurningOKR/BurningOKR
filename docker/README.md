# Docker
## Getting Started

Docker Desktop requires Windows 10 Pro or Enterprise version 15063 to run

### Windows
1. Install Docker: https://www.docker.com/products/docker-desktop
2. Install Git: https://gitforwindows.org/
3. Open Powershell and clone the BurningOKR Git Repository: ```git clone https://github.com/BurningOKR/BurningOKR.git```
4. Open the BurningOKR\Docker directory
5. Configure your docker container. See [Configuration](#configuration)
6. Open Powershell within the BurningOKR\Docker directory (Press shift and right click -> open Powershell)
7. Run ```docker-compose build```. This will take several minutes.
8. Run ```docker-compose up```.
9. Done. You should now be able to open your browser on ```localhost:4200``` and see the BurningOKR Tool.

### Linux

1. Install [Docker](https://docs.docker.com/engine/install/): 
   a. Pull install script ``curl -fsSL https://get.docker.com -o get-docker.sh``
   b. Execute install script ``sudo sh get-docker.sh`` 
   c. Add current user to userlist ``sudo usermod -aG docker $USER``
   d. Logout and Login again for iii. to take effect. 
2. Install [Docker-Compose](https://docs.docker.com/compose/install/) 
   a. Install Docker-Compose ```sudo curl -L "https://github.com/docker/compose/releases/download/1.25.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose```
   b. Add Execution Permission ``sudo chmod +x /usr/local/bin/docker-compose``
3. Install Git ``sudo apt install git-all``
4. Open Terminal and clone the BurningOKR Git Repository: ```git clone https://github.com/BurningOKR/BurningOKR.git```
5. Open the BurningOKR\docker directory
6. Configure your docker container. See [Configuration](#configuration)
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
database location (E.g. Line 13 for a Docker based Database, recommended for testing). Remove the # at the front.			#?
4. Replace ``<database>``, ``<db-username>`` and ``<db-password>`` with the same as set in the ``postgres.env``.			<database>?
5. If you want the software to send mails, replace ``<smtp-username``, ``<smtp-password>``, ``<smtp-host>``
and ``<smtp-port>``. You might need to acquire these from your system administrator. If mails are not wanted, delete 
placeholder.
6. Decide if you want to use a local user database (also saved in the postgres database) or if you want to use 
Azure Active Directory as your userbase by replacing ``<local|aad>`` with either ``local`` or ``aad``. 


## Developer Information

The application consists of three containers. Frontend, Backend and Database. 

The Databases' volume is saved at the default location (Windows: ``C:\Users\Public\Documents\Hyper-V\Virtual hard disks`` 
| Linux: ``/var/lib/docker/volumes``) and is called docker_pgdata. 

The ports used by the application can be configured in the docker-compose.yml.

The network allows external access to port 4200 for the frontend and port 54023 for the database (in case you want to 
access it with pgAdmin for example). The backend can't be called from the outside. 

After first application start, the database persists even if you rebuild the containers.

