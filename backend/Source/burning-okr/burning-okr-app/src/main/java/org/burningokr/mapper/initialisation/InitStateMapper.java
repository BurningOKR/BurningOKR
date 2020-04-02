package org.burningokr.mapper.initialisation;

import java.util.Collection;
import java.util.stream.Collectors;
import org.burningokr.dto.initialisation.InitStateDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.initialisation.InitState;
import org.springframework.stereotype.Service;

@Service
public class InitStateMapper implements DataMapper<InitState, InitStateDto> {
  @Override
  public InitState mapDtoToEntity(InitStateDto input) {
    InitState initState = new InitState();
    initState.setInitState(input.getInitState());
    initState.setRuntimeId(input.getRuntimeId());

    return initState;
  }

  @Override
  public InitStateDto mapEntityToDto(InitState input) {
    InitStateDto initStateDto = new InitStateDto();
    initStateDto.setInitState(input.getInitState());
    initStateDto.setRuntimeId(input.getRuntimeId());

    return initStateDto;
  }

  @Override
  public Collection<InitState> mapDtosToEntities(Collection<InitStateDto> input) {
    return input.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
  }

  @Override
  public Collection<InitStateDto> mapEntitiesToDtos(Collection<InitState> input) {
    return input.stream().map(this::mapEntityToDto).collect(Collectors.toList());
  }
}
