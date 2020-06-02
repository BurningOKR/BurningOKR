package org.burningokr.service.structure.departmentservices.structureServiceAdminsTest;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;
import org.burningokr.model.structures.Department;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StructureServiceAdminsTest_Department extends StructureServiceAdminsTest<Department> {

  @Test
  public void updateDepartment_expectsOkrMasterIdIsChanged() {
    Department updateDepartment = new Department();
    updateDepartment.setId(departmentId);
    updateDepartment.setOkrMasterId(UUID.randomUUID());

    Department persistedDepartment = new Department();
    structure.setId(departmentId);

    when(structureRepository.save(any(Department.class))).then(returnsFirstArg());
    when(structureRepository.findByIdOrThrow(departmentId)).thenReturn(persistedDepartment);

    structure = structureServiceAdmins.updateStructure(updateDepartment, user);

    Assert.assertEquals(updateDepartment.getOkrMasterId(), structure.getOkrMasterId());
  }

  @Test
  public void updateDepartment_expectsOkrTopicSponsorIdIsChanged() {
    Department updateDepartment = new Department();
    updateDepartment.setId(departmentId);
    updateDepartment.setOkrTopicSponsorId(UUID.randomUUID());

    Department persistedDepartment = new Department();
    structure.setId(departmentId);

    when(structureRepository.save(any(Department.class))).then(returnsFirstArg());
    when(structureRepository.findByIdOrThrow(departmentId)).thenReturn(persistedDepartment);

    structure = structureServiceAdmins.updateStructure(updateDepartment, user);

    Assert.assertEquals(updateDepartment.getOkrTopicSponsorId(), structure.getOkrTopicSponsorId());
  }

  @Test
  public void updateDepartment_expectsOkrMemberIdsAreChanged() {
    Department updateDepartment = new Department();
    updateDepartment.setId(departmentId);
    Collection<UUID> okrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    updateDepartment.setOkrMemberIds(okrMemberIds);
    Department persistedDepartment = new Department();
    structure.setId(departmentId);
    when(structureRepository.save(any(Department.class))).then(returnsFirstArg());
    when(structureRepository.findByIdOrThrow(departmentId)).thenReturn(persistedDepartment);

    structure = structureServiceAdmins.updateStructure(updateDepartment, user);

    Assert.assertEquals(okrMemberIds, structure.getOkrMemberIds());
  }

  @Override
  protected Department createStructure() {
    return new Department();
  }

  @Override
  protected Class<Department> getStructureClass() {
    return Department.class;
  }
}
