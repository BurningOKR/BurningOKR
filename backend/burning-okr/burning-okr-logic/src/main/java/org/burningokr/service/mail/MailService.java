package org.burningokr.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.mail.Mail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
  private final Optional<JavaMailSender> javaMailSender;
  private final TemplateEngine templateEngine;

  /**
   * Send an E-Mail.
   *
   * @param mail a {@link Mail} object
   * @throws MessagingException if email can't be send
   */
  public void sendMail(Mail mail) throws MessagingException {
    if (hasMailConfigured()) {
      checkAndInitializeCollections(mail);

      MimeMessage message = createMimeMessage(mail);
      if (message != null) {
        javaMailSender.get().send(message);
      } else {
        throw new NullPointerException("No message to be send. Mail Message is null!");
      }
    }
  }

  public boolean hasMailConfigured() {
    return javaMailSender.isPresent();
  }

  private void checkAndInitializeCollections(Mail mail) {
    if (mail.getTo() == null) {
      mail.setTo(new ArrayList<>(0));
    }
    if (mail.getBcc() == null) {
      mail.setBcc(new ArrayList<>(0));
    }
    if (mail.getCc() == null) {
      mail.setCc(new ArrayList<>(0));
    }
    if (mail.getVariables() == null) {
      mail.setVariables(new HashMap<>());
    }
  }

  private MimeMessage createMimeMessage(Mail mail) throws MessagingException {
    if (hasMailConfigured()) {
      MimeMessage message = javaMailSender.get().createMimeMessage();
      MimeMessageHelper helper =
        new MimeMessageHelper(
          message,
          MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
          StandardCharsets.UTF_8.name()
        );

      helper.setTo(convertToArray(mail.getTo()));
      helper.setCc(convertToArray(mail.getCc()));
      helper.setBcc(convertToArray(mail.getBcc()));
      helper.setText(getHtmlBodyFromTemplate(mail), true);
      helper.setSubject(mail.getSubject());
      helper.setFrom(mail.getFrom());
      return message;
    } else {
      return null;
    }
  }

  private String[] convertToArray(Collection<String> collection) {
    return collection.isEmpty() ? new String[0] : (String[]) collection.toArray();
  }

  private String getHtmlBodyFromTemplate(Mail mail) {
    Context thymeleafContext = new Context();
    thymeleafContext.setVariables(mail.getVariables());

    String template = getSanitizedTemplateName(mail.getTemplateName());
    return templateEngine.process(template, thymeleafContext);
  }

  private String getSanitizedTemplateName(String templateName) {
    String result;
    if (templateName.endsWith(".html")) {
      result = templateName.substring(0, (templateName.length() - ".html".length()));
    } else {
      result = templateName;
    }
    return result;
  }
}
