# Coding Guidelines

## Logging
This project uses [`java.util.logging.Logger`](https://docs.oracle.com/javase/7/docs/api/java/util/logging/Logger.html) for displaying runtime information.

### Levels
- `Level.FINE`: Exception Stacktraces, Background Information for Exceptions in Tests
- `Level.SEVERE`: Parameter Informaton that caused exception, Exception Error Message
 
## Code Documentation
The project uses [these guidelines](/docs/javadoc_guidelines.md) for *JavaDoc* comments in source files.

## Formatting

Section needs to be fixed.

We use the [format-maven-plugin](https://github.com/coveooss/fmt-maven-plugin) during build which makes use of google-java-format to enforce consisten formatting across the codebase.

To trigger autoformat manually run `mvn com.coveo:fmt-maven-plugin:format` in the project root directory.

## Checkstyle

The [Checkstyle Maven Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/index.html) can be run via `mvn checkstyle:check`.
It uses a [modified](build-tools/src/main/resources/google_checks.xml) [google java style](https://google.github.io/styleguide/javaguide.html) configuration.

