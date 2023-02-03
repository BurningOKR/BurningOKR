package org.burningokr.service.userutil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.burningokr.service.condition.AadCondition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Conditional(AadCondition.class)
@Component
@Validated
@ConfigurationProperties("azure.ad")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AzureAdProperties {
  @NotEmpty
  private String issuer;

  private List<AzureGroup> azureGroups;
}
