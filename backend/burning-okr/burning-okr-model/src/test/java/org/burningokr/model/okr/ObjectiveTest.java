package org.burningokr.model.okr;

import org.burningokr.model.structures.Department;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectiveTest {

  private String originalName;
  private long originalId;
  private Department originalDepartment;
  private String originalDescription;
  private String originalRemark;
  private Objective originalObjective;
  private String originalContactPerson;

  private Objective expectedObjective;

  @Before
  public void init() {
    originalName = "originalName";
    originalId = 100L;
    originalRemark = "originalRemark";
    originalDepartment = new Department();
    originalDescription = "originalDescription";
    originalObjective = new Objective();
    originalContactPerson = "originalContactPerson";

    expectedObjective = new Objective();
    expectedObjective.setId(originalId);
    expectedObjective.setParentStructure(originalDepartment);
    expectedObjective.setParentObjective(originalObjective);
    expectedObjective.setContactPersonId(originalContactPerson);
    expectedObjective.setDescription(originalDescription);
    expectedObjective.setName(originalName);
    expectedObjective.setRemark(originalRemark);
    expectedObjective.setActive(true);
  }

  @Test
  public void getCopyWithoutRelations_expectedNotNull() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    Assert.assertNotNull(actualObjective);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedObjective, actualObjective);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedObjective.getId(), actualObjective.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectCopiedInformation() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    Assert.assertEquals(originalContactPerson, actualObjective.getContactPersonId());
    Assert.assertEquals(originalDescription, actualObjective.getDescription());
    Assert.assertEquals(originalName, actualObjective.getName());
    Assert.assertEquals(originalRemark, actualObjective.getRemark());
    Assert.assertTrue(actualObjective.isActive());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    Assert.assertNull(actualObjective.getParentObjective());
    Assert.assertNull(actualObjective.getParentStructure());
  }
}
