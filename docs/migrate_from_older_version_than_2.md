# Migration to version 2.0

## What are the changes? A quick overview

BurningOKR introduced some big changes in version 2:

- Local auth got replaced by Keycloak or AzureAD
and dependencies are now up-to-date.
- Our Backend is now running the latest Spring Boot version (3.X.X) and we updated our Java version to 17.
- Gradle also got an update to version 7.X.

With these changes the development process for new features and the rework process for older ones
got more easy for us, and you, if you want to contribute.

But on the downside we had to make some breaking changes:

- Local auth got removed from the project due to it's deprecation and got replaced by state of the art OIDC, which is much more simple to use for us as developers and you as users/admins. By using a well known standard we can ensure that future BurningOKR releases can reuse the current implementations.
- We also decided to remove the Tomcat support and to replace it with something much more convenient: docker containers! We believe that docker containers are a great way to support multi arch and ensure that BurningOKR can run without issues because it comes bundled with everything you need.
- We also added docker support for older versions and the feedback was great so far.

Thank you for your support and stay tuned for more new things in the future!

## Migration process from local auth (not relevant if you used AzureAD)

Keycloak is our replacement for local auth, if you want to self host your auth provider. But you can also switch to AzureADy if you want to. The old local auth users will be marked as DEPRECATED but are still at the same teams etc. as before. The new users will be present in the application after the users first login. Then you can replace the old deprecated users with the new ones manually.

### Migration steps

1. Please make sure you are using the latest non 2.0 version of BurningOKR (1.5.2).
2. Only when you want to use Keycloak as your login provider: [install and configure Keycloak](./keycloak_development_install.md).
3. At this point please check that you have: Keycloak and BurningOKR version 1.5.2 configured and running.
4. Stop your BurningOKR docker compose stack.
5. Backup your database and update to postgres 15, if you are using an old version of postgres. A guide can be found [here](https://www.cloudytuts.com/tutorials/docker/how-to-upgrade-postgresql-in-docker-and-kubernetes/).
6. Make sure to use a new volume for your postgres15 instance and not the old one from the previous postgres instance.
7. You can now use the [new docker compose](../docker/docker-compose.yml) to start the BurningOKR stack, make sure to modify the env files before starting the stack. Also make sure to map the correct volume for your postgres15 instance. The volume you need to map is the one you inserted your existing data in while upgrading your postgres version in step 5 and 6.
8. When you start the docker compose stack migrations will be applied and all local auth users will be set to DEPRECATED. The DEPRECATED users will stay in teams etc and you need to replace them with the new users manually. For doing so you need to create user accounts in your keycloak instance and the users have to login at least once into BurningOKR. The user is present in the BurningOKR database after the first login. After that the admin can replace the DEPRECATED user account in teams etc with the new one.
