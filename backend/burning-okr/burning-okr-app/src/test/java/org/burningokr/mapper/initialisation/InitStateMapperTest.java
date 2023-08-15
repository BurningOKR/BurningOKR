package org.burningokr.mapper.initialisation;

import org.burningokr.dto.initialisation.InitStateDto;
import org.burningokr.model.initialisation.InitState;
import org.burningokr.model.initialisation.InitStateName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InitStateMapperTest {

  private InitState initState;
  private InitStateDto initStateDto;
  private InitStateMapper initStateMapper;

  @BeforeEach
  public void init() {
    this.initState = new InitState();
    this.initStateDto = new InitStateDto();
    this.initStateMapper = new InitStateMapper();
  }

  @Test
  public void mapDtoToEntity_expectAllMapped() {
    initStateDto.setInitState(InitStateName.CREATE_USER);

    initState = initStateMapper.mapDtoToEntity(initStateDto);

    assertEquals(initStateDto.getInitState(), initState.getInitState());
  }

  @Test
  public void mapEntityToDto_expectAllMapped() {
    initState.setInitState(InitStateName.CREATE_USER);
    initState.setId(5L);

    initStateDto = initStateMapper.mapEntityToDto(initState);

    assertEquals(initState.getInitState(), initStateDto.getInitState());
  }

  @Test
  public void mapEntitiesToDtos_expectEmptyList() {
    Collection<InitState> initStates = new ArrayList<>();

    Collection<InitStateDto> initStateDtos = initStateMapper.mapEntitiesToDtos(initStates);

    assertTrue(initStateDtos.isEmpty());
  }

  @Test
  public void mapEntitiesToDtos_expectAllMapped() {
    InitState initState1 = new InitState();
    initState1.setId(1L);
    initState1.setInitState(InitStateName.CREATE_USER);

    InitState initState2 = new InitState();
    initState2.setId(2L);
    initState2.setInitState(InitStateName.INITIALIZED);

    Collection<InitState> initStates = Arrays.asList(initState1, initState2);

    Collection<InitStateDto> initStateDtos = initStateMapper.mapEntitiesToDtos(initStates);

    assertEquals(initStates.size(), initStateDtos.size());
    Optional<InitStateDto> firstInitStateDto = initStateDtos.stream().findFirst();
    Optional<InitStateDto> secondInitStateDto = initStateDtos.stream().skip(1).findFirst();
    if (firstInitStateDto.isPresent() && secondInitStateDto.isPresent()) {
      assertEquals(initState1.getInitState(), firstInitStateDto.get().getInitState());
      assertEquals(initState2.getInitState(), secondInitStateDto.get().getInitState());
    } else {
      fail();
    }
  }

  @Test
  public void mapDtosToEntities_expectEmptyList() {
    Collection<InitStateDto> initStateDtos = new ArrayList<>();

    Collection<InitState> initStates = initStateMapper.mapDtosToEntities(initStateDtos);

    assertTrue(initStates.isEmpty());
  }

  @Test
  public void mapDtosToEntities_expectAllMapped() {
    InitStateDto initStateDto1 = new InitStateDto();
    initStateDto1.setInitState(InitStateName.CREATE_USER);

    InitStateDto initStateDto2 = new InitStateDto();
    initStateDto2.setInitState(InitStateName.INITIALIZED);

    Collection<InitStateDto> initStateDtos = Arrays.asList(initStateDto1, initStateDto2);

    Collection<InitState> initStates = initStateMapper.mapDtosToEntities(initStateDtos);

    assertEquals(initStateDtos.size(), initStates.size());
    Optional<InitState> firstInitState = initStates.stream().findFirst();
    Optional<InitState> secondInitState = initStates.stream().skip(1).findFirst();
    if (firstInitState.isPresent() && secondInitState.isPresent()) {
      assertEquals(initStateDto1.getInitState(), firstInitState.get().getInitState());
      assertEquals(initStateDto2.getInitState(), secondInitState.get().getInitState());
    } else {
      fail();
    }
  }
}
