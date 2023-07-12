<p align="center"><img src="/docs/ci/Logo_burningOKR_RGB_web.png" width="35%" height="35%" alt="Burning OKR"/></p>

<br/><br/><br/>

# BurningOKR

Burning OKR is our vision to help consistently establish focus and alignment around company goals and embed transparency into the corporate culture.

BurningOKR has been developed as a web application with an Angular Frontend and Java Spring Boot Backend. As a database, Postgres SQL is used.

## Installation

When you have Docker and Docker-Compose installed you can proceed with the next steps, otherwise please install Docker and Docker-Compose first.  
You can use our docker-compose file for easy use and compatibility!

1. Download the [docker-compose-prod.yml](/docker/docker-compose-prod.yml) file
2. Download [backend.env.sample](/docker/backend.env.sample) file and **rename** it to backend.env
3. Download [postgres.env.sample](/docker/postgres.env.sample) file and **rename** it to postgres.env
4. Now fill in your configurations in the two downloaded .env-files
5. Hint: When you don't want to use Azure or a SMTP-Mailserver just comment these parts in the .env-files out and they won't be used. For more information read the [development docs](/docs/development.md).
6. After that you are good to go and you can run `docker compose -f docker-compose-prod.yml up` in the directory where the previously downloaded files are saved. Hint: When you want to reuse the console window add a `-d` to the compose command to run in detached mode.

## FAQ

- **I have BurningOKR running in a Tomcat, should i migrate to Docker?**  
  Yes you should. We will only support the docker images. When you want to use Tomcat you
  can do so but we will offer no support.

- **I get some errors with npm install (python2, node-sass, node-gyp):**  
  Use the LTS version of node, not the current! <https://nodejs.org/en/download/>

## Contact

You can write an [E-Mail](mailto:burningokr@brockhaus-ag.de) or mention our Twitter account [@BurningOKR](https://twitter.com/BurningOkr).

## License

BurningOKR was initially developed as part of a training project at [BROCKHAUS AG](http://brockhaus-ag.de) in Lünen.

Only an Open Source solution can unfold its true potential. That's why we released it on GitHub as an open-source project under the Apache 2.0 license.

See [LICENSE.txt](LICENSE.txt)
