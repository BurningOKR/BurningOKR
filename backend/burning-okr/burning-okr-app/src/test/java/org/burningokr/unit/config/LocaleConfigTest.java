package org.burningokr.unit.config;

import org.burningokr.config.LocaleConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

class LocaleConfigTest {

  private LocaleConfig localeConfig;

  @BeforeEach
  public void setUp() {
    this.localeConfig = new LocaleConfig();
  }

  @Test
  void localeChangeInterceptor_shouldSetParamName() {
    String expected = "lang";

    LocaleChangeInterceptor lci = this.localeConfig.localeChangeInterceptor();
    String actual = lci.getParamName();

    Assertions.assertEquals(expected, actual);
  }
}
