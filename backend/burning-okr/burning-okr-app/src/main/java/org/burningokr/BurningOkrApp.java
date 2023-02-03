package org.burningokr;

import org.slf4j.MDC;
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

  /**
   * Main Method to configure and start SpringApplication.
   *
   * @param args an array of Strings
   */
  public static void main(String[] args) {
    // change root directory for slf4j logging via MDC
    String osName = System.getProperty("os.name").toLowerCase();
    MDC.put("tempPath", (osName.contains("win") ? "C:\\temp\\" : "/tmp/"));
    SpringApplication app = new SpringApplication(BurningOkrApp.class);
    app.setWebApplicationType(WebApplicationType.SERVLET);
    app.addListeners(new ApplicationPidFileWriter());
    app.run(args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(BurningOkrApp.class);
  }
}
