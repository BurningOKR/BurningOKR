package org.burningokr.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;

import java.util.Collection;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "chartType")
@JsonSubTypes({
  @JsonSubTypes.Type(value = PieChartOptionsDto.class, name = "1"),
  @JsonSubTypes.Type(value = LineChartOptionsDto.class, name = "0")
})
@Data
public abstract class BaseChartOptionsDto {
//  Long id;
  String title;
  int chartType;
  Collection<Long> selectedTeamIds;
}
