# Implementation Information

## Backend

### Logging

This project uses [`java.util.logging.Logger`](https://docs.oracle.com/javase/7/docs/api/java/util/logging/Logger.html) for displaying runtime information.

#### Levels

- `Level.FINE` Exception Stacktrace, Background Information for Exceptions in Tests
- `Level.SEVERE` Parameter Information that caused exception, Exception Error Message

### Code Documentation

The project uses [these guidelines](/docs/javadoc_guidelines.md) for *JavaDoc* comments in source files.

### Formatting

Section needs to be fixed.

We use the [format-maven-plugin](https://github.com/coveooss/fmt-maven-plugin) during build which makes use of google-java-format to enforce consistent formatting across the codebase.

To trigger autoformat manually run `mvn com.coveo:fmt-maven-plugin:format` in the project root directory.

### Checkstyle

The [Checkstyle Maven Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/index.html) can be run via `mvn checkstyle:check`.
It uses a [modified](build-tools/src/main/resources/google_checks.xml) [google java style](https://google.github.io/styleguide/javaguide.html) configuration.

### Migrations

Migrations have to be created for PostgreSQL and Microsoft SQL Server separately.
Some data types are different between these two database systems.
Here is a table with equivalent data types, to keep the migrations consistent:

| PostgreSQL | MSSQL |
|------------|-------|
| boolean    | bit   |
| timestamp without timezone | datetime2 |
| uuid   | uniqueidentifier |

To use the PostgreSQL Server you have to set the following setting in the `application.yaml`:

```yaml
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

and to use the MSSQL Server yiu have to set the following setting in the `application.yaml`:

```yaml
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.burningokr.dialects.SQLServer2012UUIDFixDialect
```

For every migration you create, you **MUST** create a migration for the MSSQL Server and for the PostgreSQL server.
Migrations are simple sql scripts, and they can be found in `burning-okr-app/src/main/resources/db/migration`.
There are two directories, `postgresql` and `sqlserver`. Create a migration script in each directory. The migration scripts
should generally do the same, but they need to stick to the dialect of the corresponding dbms.

## Frontend

### Decorators

#### Fetchable Decorator

A Service which needs to Fetch Data on Application Startup or when a User logs in, needs to have the `@Fetchable()` Decorator and the `Fetchable` interface.
The Fetchable Interface enforces the implementation of the `fetchData(): void` method.

You can add the whole logic for fetching any data, that the service needs within the fetchData Method.
This Method will be called by the Fetchable Decorator on Application Startup and
when a new User logs in.

Here is an example usage of Fetchable:

```typescript
@Fetchable()
@Injectable({
  providedIn: 'root'
})
export class CurrentUserService implements Fetchable {
  private isAdmin$: Observable<boolean>;

  constructor(private oAuthService: OAuthService,
              private userApiService: UserApiService) {
  }

  getCurrentUser(): Observable<User> {
    return this.userApiService.getCurrentUser();
  }

  isCurrentUserAdmin(): Observable<boolean> {
    return this.isAdmin$;
  }

  fetchData(): void {
    this.isAdmin$ = this.userApiService.isCurrentUserAdmin$()
      .pipe(shareReplay(1));
  }
}
```
