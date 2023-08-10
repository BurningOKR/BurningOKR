package org.burningokr.service.okr.feedback;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.ConfigurationName;
import org.burningokr.model.mail.Mail;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.exceptions.SendingMailFailedException;
import org.burningokr.service.mail.MailService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FeedbackService {

  private final MailService mailService;
  private final ConfigurationService configurationService;

  /**
   * Sends Feedback Mail to all configured Contact Persons.
   *
   * @param feedbackSender a String value
   * @param feedbackText   a String value
   */
  public void sendFeedbackMail(String feedbackSender, String feedbackText) {
    Mail mail = createFeedbackMail(feedbackSender, feedbackText);
    try {
      mailService.sendMail(mail);
    } catch (MessagingException e) {
      throw new SendingMailFailedException("Failed sending feedback mail");
    }
  }

  private Mail createFeedbackMail(String feedbackSender, String feedbackText) {
    Mail mail = new Mail();

    mail.setVariables(getVariablesMap(feedbackSender, feedbackText));
    mail.setTo(Arrays.asList(getFeedbackReceivers()));
    mail.setTemplateName("feedback-mail");
    mail.setFrom(getConfigurationValue(ConfigurationName.EMAIL_FROM));
    mail.setSubject(getConfigurationValue(ConfigurationName.EMAIL_SUBJECT_FEEDBACK));
    return mail;
  }

  private Map<String, Object> getVariablesMap(String feedbackSender, String feedbackText) {
    Map<String, Object> thymeleafVariables = new HashMap<>();
    thymeleafVariables.put("feedbackSender", feedbackSender);
    thymeleafVariables.put("feedbackText", feedbackText);
    return thymeleafVariables;
  }

  private String getConfigurationValue(ConfigurationName configurationName) {
    return configurationService.getConfigurationByName(configurationName.getName()).getValue();
  }

  private String[] getFeedbackReceivers() {
    return getConfigurationValue(ConfigurationName.FEEDBACK_RECEIVERS).split(",");
  }
}
