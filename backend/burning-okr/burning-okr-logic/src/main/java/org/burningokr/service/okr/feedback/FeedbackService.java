package org.burningokr.service.okr.feedback;

import java.util.*;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.mail.Mail;
import org.burningokr.service.exceptions.SendingMailFailedException;
import org.burningokr.service.mail.MailService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

  private final MailService mailService;
  private final ContactPersonConfiguration contactPersonConfiguration;

  /**
   * Sends Feedback Mail to all configured Contact Persons.
   *
   * @param feedbackSender a String value
   * @param feedbackText a String value
   */
  public void sendFeedbackMail(String feedbackSender, String feedbackText) {
    contactPersonConfiguration
        .getContactPersons()
        .forEach(
            contactPerson -> {
              Mail mail = createFeedbackMail(contactPerson, feedbackSender, feedbackText);
              try {
                mailService.sendMail(mail);
              } catch (MessagingException e) {
                throw new SendingMailFailedException(
                    "Failed sending mail to " + contactPerson.getEmail());
              }
            });
  }

  private Mail createFeedbackMail(
      ContactPersonConfiguration.ContactPerson contactPerson,
      String feedbackSender,
      String feedbackText) {
    Mail mail = new Mail();

    mail.setVariables(getVariablesMap(contactPerson, feedbackSender, feedbackText));
    mail.setTo(Arrays.asList(contactPerson.getEmail()));
    mail.setTemplateName("feedback-mail");
    mail.setFrom("no-reply@okr-tool.com");
    mail.setSubject("OKR Tool - Feedback");
    return mail;
  }

  private Map<String, Object> getVariablesMap(
      ContactPersonConfiguration.ContactPerson contactPerson,
      String feedbackSender,
      String feedbackText) {
    Map<String, Object> thymeleafVariables = new HashMap<>();
    thymeleafVariables.put(
        "contactPerson", contactPerson.getName() + ' ' + contactPerson.getSurname());
    thymeleafVariables.put("feedbackSender", feedbackSender);
    thymeleafVariables.put("feedbackText", feedbackText);
    return thymeleafVariables;
  }
}
