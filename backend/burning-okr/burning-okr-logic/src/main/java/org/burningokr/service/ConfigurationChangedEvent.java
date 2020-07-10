package org.burningokr.service;

import org.burningokr.model.configuration.Configuration;
import org.springframework.context.ApplicationEvent;

public class ConfigurationChangedEvent extends ApplicationEvent {
    private Configuration changedConfiguration;

    public ConfigurationChangedEvent(Object source, Configuration changedConfiguration) {
        super(source);
        this.changedConfiguration = changedConfiguration;
    }
    public Configuration getChangedConfiguration() {
        return changedConfiguration;
    }
}
