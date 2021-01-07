package org.burningokr.dto.okr;

import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Data
public class OkrTeamDescriptionDto {
  private Long id;
  private String name;
  private UUID initiatorId;
  private Collection<UUID> startTeam;
  private Collection<UUID> stakeholders;
  private String acceptanceCriteria;
  private String contributesTo;
  private String delimitation;
  private LocalDate beginning;
  private String dependencies;
  private String resources;
  private String handoverPlan;
}
