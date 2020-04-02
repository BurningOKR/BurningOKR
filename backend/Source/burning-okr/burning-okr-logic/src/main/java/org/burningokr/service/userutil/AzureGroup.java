package org.burningokr.service.userutil;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class AzureGroup {
  @NotEmpty private String name;
  @NotEmpty private String id;
}
