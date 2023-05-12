package org.burningokr.dto.okr;

import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class OkrTopicDescriptionDto {
  private Long id;
  private String name;
  private UUID initiatorId;
  private Collection<UUID> startTeam;
  private Collection<UUID> stakeholders;
  private String description;
  private String contributesTo;
  private String delimitation;
  private String beginning;
  private String dependencies;
  private String resources;
  private String handoverPlan;
}
