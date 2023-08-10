package org.burningokr.service.okr.feedback;

import jakarta.mail.MessagingException;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.model.configuration.ConfigurationName;
import org.burningokr.model.mail.Mail;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.mail.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {
  @Mock
  private MailService mailService;
  @Mock
  private ConfigurationService configurationService;
  @InjectMocks
  private FeedbackService feedbackService;

  @Test
  public void sendFeedbackMail_shouldCallMailService() throws MessagingException {
    Configuration configurationTo = new Configuration();
    configurationTo.setValue("info@brockhaus-ag.de,test@brockhaus-ag.de");
    Configuration configurationFrom = new Configuration();
    configurationFrom.setValue("sender@brockhaus-ag.de");
    Configuration configurationSubject = new Configuration();
    configurationSubject.setValue("Feedback incoming");
    when(configurationService.getConfigurationByName(ConfigurationName.FEEDBACK_RECEIVERS.getName())).thenReturn(configurationTo);
    when(configurationService.getConfigurationByName(ConfigurationName.EMAIL_FROM.getName())).thenReturn(configurationFrom);
    when(configurationService.getConfigurationByName(ConfigurationName.EMAIL_SUBJECT_FEEDBACK.getName())).thenReturn(configurationSubject);

    feedbackService.sendFeedbackMail("feedbacksender@brockhaus-ag.de", "Mein Feedback");

    ArgumentCaptor<Mail> argument = ArgumentCaptor.forClass(Mail.class);
    verify(mailService).sendMail(argument.capture());
    assertEquals("sender@brockhaus-ag.de", argument.getValue().getFrom());
    assertEquals(2, argument.getValue().getTo().size());
    assertEquals("Feedback incoming", argument.getValue().getSubject());
    assertTrue(argument.getValue().getVariables().containsKey("feedbackSender"));
    assertEquals("feedbacksender@brockhaus-ag.de", argument.getValue().getVariables().get("feedbackSender"));
    assertTrue(argument.getValue().getVariables().containsKey("feedbackText"));
    assertEquals("Mein Feedback", argument.getValue().getVariables().get("feedbackText"));
  }
}