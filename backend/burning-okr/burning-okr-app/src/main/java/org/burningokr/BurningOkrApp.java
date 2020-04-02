package org.burningokr;

import java.util.UUID;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class BurningOkrApp extends SpringBootServletInitializer {
  private static ConfigurableApplicationContext context;

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(BurningOkrApp.class);
  }

  /**
   * Main Method to configure and start SpringApplication.
   *
   * @param args an array of Strings
   */
  public static void main(String[] args) {
    startApplication(args);
  }

  /** Restarts the Application. */
  public static void restart() {
    ApplicationArguments args = context.getBean(ApplicationArguments.class);

    Thread thread =
        new Thread(
            () -> {
              context.close();
              startApplication(args.getSourceArgs());
            });

    thread.setDaemon(false);
    thread.start();
  }

  private static void startApplication(String[] sourceArgs) {
    SpringApplication app = new SpringApplication(BurningOkrApp.class);
    app.setWebApplicationType(WebApplicationType.SERVLET);
    app.addListeners(new ApplicationPidFileWriter("./shutdown.pid"));
    context = app.run(sourceArgs);
    context.setId(UUID.randomUUID().toString());
  }
}
