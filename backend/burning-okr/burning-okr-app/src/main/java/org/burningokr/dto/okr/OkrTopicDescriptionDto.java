package org.burningokr.dto.okr;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OkrTopicDescriptionDto {

  public OkrTopicDescriptionDto(OkrTopicDescriptionDto.OkrTopicDescriptionDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.name = b.name;
    this.initiatorId = b.initiatorId;
    this.stakeholders = b.stakeholders;
    this.description = b.description;
    this.contributesTo = b.contributesTo;
    this.delimitation = b.delimitation;
    this.beginning = b.beginning;
    this.dependencies = b.dependencies;
    this.resources = b.resources;
    this.handoverPlan = b.handoverPlan;
    this.startTeam = b.startTeam;
  }

  public OkrTopicDescriptionDto(OkrTopicDescriptionDto dto) {
    this.id = dto.id;
    this.name = dto.name;
    this.initiatorId = dto.initiatorId;
    this.stakeholders = dto.stakeholders;
    this.description = dto.description;
    this.contributesTo = dto.contributesTo;
    this.delimitation = dto.delimitation;
    this.beginning = dto.beginning;
    this.dependencies = dto.dependencies;
    this.resources = dto.resources;
    this.handoverPlan = dto.handoverPlan;
    this.startTeam = dto.startTeam;
  }

  private Long id;

  @Size(
      min = 1,
      max = 255,
      message = "Name should not be longer than 255 characters."
  )
  private String name;

  private UUID initiatorId;

  @NotNull
  private Collection<UUID> startTeam;

  @NotNull
  private Collection<UUID> stakeholders;

  @Size(
      min = 1,
      max = 1023,
      message = "Description should not be longer than 1024 characters."
  )
  private String description;

  @Size(
      min = 1,
      max = 1023,
      message = "Contributes To should not be longer than 1024 characters."
  )
  private String contributesTo;

  @Size(
      min = 1,
      max = 1023,
      message = "Delimitation should not be longer than 1024 characters."
  )
  private String delimitation;

  private String beginning;

  @Size(
      min = 1,
      max = 1023,
      message = "Dependencies should not be longer than 1024 characters."
  )
  private String dependencies;

  @Size(
      min = 1,
      max = 1023,
      message = "Resources should not be longer than 1024 characters."
  )
  private String resources;

  @Size(
      min = 1,
      max = 1023,
      message = "Handover Over Plan should not be longer than 1024 characters."
  )
  private String handoverPlan;
}
