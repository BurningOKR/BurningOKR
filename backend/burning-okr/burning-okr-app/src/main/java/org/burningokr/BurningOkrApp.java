package org.burningokr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class BurningOkrApp extends SpringBootServletInitializer {

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
    SpringApplication app = new SpringApplication(BurningOkrApp.class);
    app.setWebApplicationType(WebApplicationType.SERVLET);
    app.addListeners(new ApplicationPidFileWriter());
    app.run(args);
  }
}
