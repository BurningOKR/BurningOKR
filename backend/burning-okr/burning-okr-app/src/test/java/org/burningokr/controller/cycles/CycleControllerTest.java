package org.burningokr.controller.cycles;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.validators.CycleDtoValidator;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.mapper.cycle.CycleMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.service.cycle.CycleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CycleControllerTest {

  @Mock
  private CycleService cycleService;
  @Mock
  private CycleDtoValidator cycleDtoValidator;
  @Mock
  private CycleMapper cycleMapper;

  private CycleController cycleController;

  @BeforeEach
  void setUp() {
    this.cycleController = new CycleController(
      this.cycleService,
      this.cycleDtoValidator,
      this.cycleMapper
    );
  }


  @Test
  void getAllCycles_shouldReturnResponseWithCodeOK() {
    when(this.cycleService.getAllCycles()).thenReturn(Collections.emptyList());
    int expected = 200;

    ResponseEntity<Collection<CycleDto>> result = this.cycleController.getAllCycles();

    Assertions.assertEquals(expected, result.getStatusCode().value());
  }

  @Test
  void getAllCycles_shouldReturnEmptyList() {
    when(this.cycleService.getAllCycles()).thenReturn(Collections.emptyList());

    ResponseEntity<Collection<CycleDto>> result = this.cycleController.getAllCycles();
    Collection<CycleDto> body = result.getBody();

    Assertions.assertNotNull(body);
    Assertions.assertEquals(0, body.size());
  }

  @Test
  void getAllCycles_shouldReturnListContainingOneDto() {
    when(this.cycleService.getAllCycles()).thenReturn(List.of(new Cycle()));
    when(this.cycleMapper.mapEntityToDto(any())).thenReturn(new CycleDto());

    ResponseEntity<Collection<CycleDto>> result = this.cycleController.getAllCycles();
    Collection<CycleDto> body = result.getBody();

    Assertions.assertNotNull(body);
    Assertions.assertEquals(1, body.size());
  }

  @Test
  void getCycleById_shouldThrowEntityNotFoundException() {
    when(this.cycleService.findById(any())).thenThrow(new EntityNotFoundException());

    Assertions.assertThrows(
      EntityNotFoundException.class,
      () -> this.cycleController.getCycleById(1L)
    );
  }

  @Test
  void getCycleById_shouldReturnResponseWithCodeOk() {
    when(this.cycleService.findById(any())).thenReturn(new Cycle());
    when(this.cycleMapper.mapEntityToDto(any())).thenReturn(new CycleDto());
    int expected = 200;

    ResponseEntity<CycleDto> result = this.cycleController.getCycleById(1L);

    Assertions.assertEquals(expected, result.getStatusCode().value());
  }

  @Test
  void getCycleById_shouldReturnDto() {
    CycleDto expected = new CycleDto();
    when(this.cycleService.findById(any())).thenReturn(new Cycle());
    when(this.cycleMapper.mapEntityToDto(any())).thenReturn(expected);

    ResponseEntity<CycleDto> result = this.cycleController.getCycleById(1L);
    CycleDto body = result.getBody();

    Assertions.assertNotNull(body);
    Assertions.assertEquals(expected, body);
  }

  @Test
  void updateCycleById_shouldThrowInvalidDtoException() throws InvalidDtoException {
    doThrow(new InvalidDtoException("")).when(this.cycleDtoValidator).validateCycleDto(any());

    Assertions.assertThrows(
      InvalidDtoException.class,
      () -> this.cycleController.updateCycleById(1L, new CycleDto())
    );
  }

  @Test
  void updateCycleById_shouldThrowEntityNotFoundException() throws InvalidDtoException {
    doNothing().when(this.cycleDtoValidator).validateCycleDto(any());
    Cycle cycle = new Cycle();
    when(this.cycleMapper.mapDtoToEntity(any())).thenReturn(cycle);
    when(this.cycleService.updateCycle(any())).thenThrow(new EntityNotFoundException());

    Assertions.assertThrows(
      EntityNotFoundException.class,
      () -> this.cycleController.updateCycleById(1L, new CycleDto())
    );
  }

  @Test
  void updateCycleById_shouldReturnResponseWithCodeOk() throws InvalidDtoException {
    doNothing().when(this.cycleDtoValidator).validateCycleDto(any());
    Cycle cycle = new Cycle();
    when(this.cycleMapper.mapDtoToEntity(any())).thenReturn(cycle);
    when(this.cycleService.updateCycle(any())).thenReturn(cycle);
    when(this.cycleMapper.mapEntityToDto(any())).thenReturn(new CycleDto());
    int expected = 200;

    ResponseEntity<CycleDto> result = this.cycleController.updateCycleById(1L, new CycleDto());

    Assertions.assertEquals(expected, result.getStatusCode().value());
  }

  @Test
  void updateCycleById_shouldReturnDto() throws InvalidDtoException {
    doNothing().when(this.cycleDtoValidator).validateCycleDto(any());
    Cycle cycle = new Cycle();
    CycleDto expected = new CycleDto();
    when(this.cycleMapper.mapDtoToEntity(any())).thenReturn(cycle);
    when(this.cycleService.updateCycle(any())).thenReturn(cycle);
    when(this.cycleMapper.mapEntityToDto(any())).thenReturn(expected);

    ResponseEntity<CycleDto> result = this.cycleController.updateCycleById(1L, new CycleDto());

    CycleDto body = result.getBody();

    Assertions.assertNotNull(body);
    Assertions.assertEquals(expected, body);
  }

  @Test
  void deleteCycleById_shouldThrowException() throws Exception {
    doThrow(new Exception()).when(this.cycleService).deleteCycle(any());

    Assertions.assertThrows(
      Exception.class,
      () -> this.cycleController.deleteCycleById(1L)
    );
  }

  @Test
  void deleteCycleById_shouldThrowEntityNotFoundException() throws Exception {
    doThrow(new EntityNotFoundException()).when(this.cycleService).deleteCycle(any());

    Assertions.assertThrows(
      EntityNotFoundException.class,
      () -> this.cycleController.deleteCycleById(1L)
    );
  }

  @Test
  void deleteCycleById_shouldReturnResponseWithCodeOk() throws Exception {
    doNothing().when(this.cycleService).deleteCycle(any());
    int expected = 200;

    ResponseEntity<Boolean> result = this.cycleController.deleteCycleById(1L);

    Assertions.assertEquals(expected, result.getStatusCode().value());
  }
}
