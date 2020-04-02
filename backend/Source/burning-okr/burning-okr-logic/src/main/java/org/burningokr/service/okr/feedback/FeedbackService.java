package org.burningokr.service.okr.feedback;

import javax.mail.MessagingException;
import org.burningokr.service.exceptions.SendingMailFailedException;
import org.burningokr.service.mail.FeedbackMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

  private final FeedbackMailService feedbackMailService;
  private final ContactPersonConfiguration contactPersonConfiguration;

  @Autowired
  public FeedbackService(
      FeedbackMailService feedbackMailService,
      ContactPersonConfiguration contactPersonConfiguration) {
    this.feedbackMailService = feedbackMailService;
    this.contactPersonConfiguration = contactPersonConfiguration;
  }

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
              try {
                feedbackMailService.sendFeedbackMail(contactPerson, feedbackSender, feedbackText);
              } catch (MessagingException e) {
                throw new SendingMailFailedException(
                    "Failed sending mail to " + contactPerson.getEmail());
              }
            });
  }
}
