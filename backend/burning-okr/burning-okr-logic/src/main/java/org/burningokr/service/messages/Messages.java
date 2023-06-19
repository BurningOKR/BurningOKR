package org.burningokr.service.messages;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class Messages {

  private MessageSource messageSource;
  private MessageSourceAccessor accessor;

  @Autowired
  public Messages(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @PostConstruct
  public void init() {
    accessor = new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
  }

  public String get(String code) {
    return accessor.getMessage(code);
  }

  public String get(String code, Object[] args) {
    return accessor.getMessage(code, args);
  }
}
