package org.burningokr.model.mail;

import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class Mail {
  private String templateName;
  private Map<String, Object> variables;
  private String from;
  private String subject;
  private String recipient;
  private Collection<String> to;
  private Collection<String> cc;
  private Collection<String> bcc;
}
