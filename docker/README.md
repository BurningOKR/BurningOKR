# Docker
## Getting Started
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


### Configuration (Windows and Linux)

There are two sample environment files within the BurningOKR\Docker directory: ```backend.env.sample``` and ``postgres.env.sample``

At first, you need to rename both environment files to ``backend.env`` and `postgres.env`


