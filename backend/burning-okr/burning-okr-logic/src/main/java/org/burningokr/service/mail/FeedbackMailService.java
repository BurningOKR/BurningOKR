package org.burningokr.service.mail;

import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.burningokr.service.okr.feedback.ContactPersonConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class FeedbackMailService {

  private final JavaMailSender emailSender;
  private final TemplateEngine templateEngine;
  private final Logger logger = LoggerFactory.getLogger(FeedbackMailService.class);

  @Autowired
  public FeedbackMailService(JavaMailSender emailSender, TemplateEngine templateEngine) {
    this.emailSender = emailSender;
    this.templateEngine = templateEngine;
  }

  /**
   * Sends Feedback Mails to contact Persons.
   *
   * @param contactPerson a {@link
   *     org.burningokr.service.okr.feedback.ContactPersonConfiguration.ContactPerson} object
   * @param feedbackSender a String value
   * @param feedbackText a String value
   * @throws MessagingException if MimeMessageHelper throws Exception
   */
  public void sendFeedbackMail(
      ContactPersonConfiguration.ContactPerson contactPerson,
      String feedbackSender,
      String feedbackText)
      throws MessagingException {
    Context context = new Context();
    context.setVariable("contactPerson", getFullName(contactPerson));
    context.setVariable("feedbackSender", feedbackSender);
    context.setVariable("feedbackText", feedbackText);
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper =
        new MimeMessageHelper(
            message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
    String html = templateEngine.process("feedback-mail", context);
    helper.setTo(contactPerson.getEmail());
    helper.setText(html, true);
    helper.setSubject("OKR Tool - Feedback");
    helper.setFrom("no-reply@okr-tool.com");

    emailSender.send(message);

    logger.info("Sent feedback to " + getFullName(contactPerson));
  }

  private String getFullName(ContactPersonConfiguration.ContactPerson contactPerson) {
    return contactPerson.getName() + " " + contactPerson.getSurname();
  }
}
