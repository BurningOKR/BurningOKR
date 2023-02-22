package org.burningokr.service.userutil;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.burningokr.model.users.AadUser;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"@odata.context", "@odata.nextLink", "value"})
@Data
public class AzureGroupResponse {
  @JsonProperty("@odata.context")
  private String odataContext;

  @JsonProperty("@odata.nextLink")
  private String odataNextLink;

  @JsonProperty("value")
  private List<AadUser> users = null;
}
