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
  private String originalDescription;
  private String originalRemark;
  private Objective emptyObjective;
  private String originalContactPerson;

  private Objective expectedObjective;

  @BeforeEach
  public void init() {
    originalName = "originalName";
    long originalId = 100L;
    originalRemark = "originalRemark";
    OkrDepartment originalOkrDepartment = new OkrDepartment();
    originalDescription = "originalDescription";
    Objective originalObjective = new Objective();
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

    emptyObjective = new Objective();
  }

  @Test
  public void hasParentObjective_shouldBeTrue() {
    assertTrue(expectedObjective.hasParentObjective());
  }

  @Test
  public void hasParentObjective_shouldBeFalse() {
    assertFalse(emptyObjective.hasParentObjective());
  }

  @Test
  public void hasContactPerson_shouldBeTrue() {
    assertTrue(expectedObjective.hasContactPerson());
  }

  @Test
  public void hasContactPerson_shouldBeFalse() {
    assertFalse(emptyObjective.hasContactPerson());
  }

  @Test
  public void isActive_shouldBeTrue() {
    assertTrue(expectedObjective.isActive());
  }

  @Test
  public void isActive_shouldBeFalse() {
    assertFalse(emptyObjective.isActive());
  }

  @Test
  public void setActive_shouldBeTrue() {
    emptyObjective.setActive(true);
    assertTrue(emptyObjective.isActive());
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
