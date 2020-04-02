package org.burningokr.service.userutil;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties("azure.ad")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
class AzureAdProperties {
  @NotEmpty private String tenantId;

  private List<AzureGroup> azureGroups;
}
