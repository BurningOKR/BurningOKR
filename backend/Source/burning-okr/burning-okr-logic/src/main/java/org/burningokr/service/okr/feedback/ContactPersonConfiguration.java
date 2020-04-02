package org.burningokr.service.okr.feedback;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("feedback")
public class ContactPersonConfiguration {
  @NotEmpty private List<ContactPerson> contactPersons;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ContactPerson {
    private String name;
    private String surname;
    private String email;
  }
}
