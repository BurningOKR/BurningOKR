package org.burningokr.service.okrUnit.departmentservices.unitServiceAdminsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.LocalUser;
import org.burningokr.service.exceptions.InvalidDeleteRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrUnitServiceAdminsTest_OkrBranch extends OkrUnitServiceAdminsTest<OkrBranch> {

  @Override
  protected OkrBranch createDepartment() {
    return new OkrBranch();
  }

  @Override
  protected Class<OkrBranch> getDepartment() {
    return OkrBranch.class;
  }

  @Test
  public void createChildDepartment_expectsTopicDescriptionIsCreated() {
    OkrDepartment department = new OkrDepartment();
    department.setName("testDepartment");
    TaskBoard taskBoard = new TaskBoard();

    when(unitRepository.findByIdOrThrow(any(Long.class))).thenReturn(new OkrBranch());
    when(unitRepository.save(any())).thenReturn(department);
    when(okrTopicDescriptionRepository.save(any()))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(taskBoardService.createNewTaskBoardWithDefaultStates()).thenReturn(taskBoard);
    when(taskBoardService.saveTaskBoard(any(TaskBoard.class))).thenReturn(taskBoard);


    OkrDepartment actual =
        (OkrDepartment)
            okrUnitServiceAdmins.createChildUnit(departmentId, department, new LocalUser());

    assertNotNull(actual.getOkrTopicDescription());
    assertEquals(department.getName(), actual.getOkrTopicDescription().getName());
  }

  @Test(expected = InvalidDeleteRequestException.class)
  public void deleteUnitWithChildUnit_expectsInvalidDeleteRequestException() {
    OkrBranch okrBranch = new OkrBranch();
    okrBranch.setId(13L);
    List<OkrChildUnit> okrChildUnits = new ArrayList<>();
    okrChildUnits.add(new OkrDepartment());
    okrBranch.setOkrChildUnits(okrChildUnits);

    when(unitRepository.findByIdOrThrow(anyLong())).thenReturn(okrBranch);

    okrUnitServiceAdmins.deleteUnit(13L, user);
  }

  @Test
  public void deleteUnit_expectsOkrTopicDescriptionIsNotDeleted() {
    OkrBranch okrBranch = new OkrBranch();
    okrBranch.setId(1L);

    okrUnitServiceAdmins.deleteUnit(okrBranch.getId(), user);

    verify(unitRepository).deleteById(okrBranch.getId());
    verify(okrTopicDescriptionService, never()).safeDeleteOkrTopicDescription(any(), any());
  }
}
