package org.burningokr.model.configuration;

public enum ConfigurationName {
  MAX_KEY_RESULTS("max-key-results"),
  OBJECTIVE_PROGRESS_GREEN_YELLOW_THRESHOLD("objective-progress-green-yellow-threshold"),
  OBJECTIVE_PROGRESS_YELLOW_RED_THRESHOLD("objective-progress-yellow-red-threshold"),
  GENERAL_FRONTEND_BASE_URL("general_frontend-base-url"),
  FEEDBACK_RECEIVERS("feedback_receivers"),
  EMAIL_FROM("email_from"),
  EMAIL_SUBJECT_NEW_USER("email_subject_new-user"),
  EMAIL_SUBJECT_FORGOT_PASSWORD("email_subject_forgot-password"),
  EMAIL_SUBJECT_FEEDBACK("email_subject_feedback"),
  TOPIC_SPONSORS_ACTIVATED("topic-sponsors-activated");

  private String name;

  ConfigurationName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
