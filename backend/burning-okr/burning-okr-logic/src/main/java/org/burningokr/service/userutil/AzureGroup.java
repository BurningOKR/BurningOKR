package org.burningokr.service.userutil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class AzureGroup {
  @NotEmpty
  private String name;
  @NotEmpty
  private String id;
}
