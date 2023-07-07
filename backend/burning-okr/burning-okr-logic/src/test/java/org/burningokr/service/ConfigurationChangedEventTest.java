package org.burningokr.service;

import org.burningokr.model.configuration.Configuration;
import org.burningokr.service.configuration.ConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConfigurationChangedEventTest {
  @InjectMocks
  private ConfigurationChangedEvent configurationChangedEvent;
  @Mock
  private ConfigurationService configurationService;
  private Configuration changedConfiguration;

  @Test
  public void getChangedConfiguration_shouldReturnChangedConfiguration(){
    configurationChangedEvent = new ConfigurationChangedEvent(configurationService, changedConfiguration);
    assertEquals(changedConfiguration, configurationChangedEvent.getChangedConfiguration());
  }
}
