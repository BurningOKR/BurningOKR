package org.burningokr.model.okr;

import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ObjectiveTest {

  private String originalName;
  private long originalId;
  private OkrDepartment originalOkrDepartment;
  private String originalDescription;
  private String originalRemark;
  private Objective originalObjective;
  private String originalContactPerson;

  private Objective expectedObjective;

  @BeforeEach
  public void init() {
    originalName = "originalName";
    originalId = 100L;
    originalRemark = "originalRemark";
    originalOkrDepartment = new OkrDepartment();
    originalDescription = "originalDescription";
    originalObjective = new Objective();
    originalContactPerson = "originalContactPerson";

    expectedObjective = new Objective();
    expectedObjective.setId(originalId);
    expectedObjective.setParentOkrUnit(originalOkrDepartment);
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

    assertNotNull(actualObjective);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    assertNotEquals(expectedObjective, actualObjective);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    assertNotEquals(expectedObjective.getId(), actualObjective.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectCopiedInformation() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    assertEquals(originalContactPerson, actualObjective.getContactPersonId());
    assertEquals(originalDescription, actualObjective.getDescription());
    assertEquals(originalName, actualObjective.getName());
    assertEquals(originalRemark, actualObjective.getRemark());
    assertTrue(actualObjective.isActive());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    Objective actualObjective = expectedObjective.getCopyWithoutRelations();

    assertNull(actualObjective.getParentObjective());
    assertNull(actualObjective.getParentOkrUnit());
  }
}
