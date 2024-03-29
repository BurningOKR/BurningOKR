package org.burningokr;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties
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
    app.run(args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(BurningOkrApp.class);
  }
}
