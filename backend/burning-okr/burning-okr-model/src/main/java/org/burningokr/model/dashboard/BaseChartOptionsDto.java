package org.burningokr.model.dashboard;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.Collection;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "chartType")
@JsonSubTypes({
  @JsonSubTypes.Type(value = LineChartOptionsDto.class, name = "0"),
  @JsonSubTypes.Type(value = PieChartOptionsDto.class, name = "1")
})
@Data
public abstract class BaseChartOptionsDto {
  Long id;
  String title;
  int chartType;
  Collection<Long> selectedTeamIds;
}
