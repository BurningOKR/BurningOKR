package org.burningokr.dto.initialisation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.burningokr.model.initialisation.InitStateName;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitStateDto {

  private InitStateName initState;
  private String runtimeId;
}
