package org.burningokr.model.okrUnits;

import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OkrCompanyTest {

  private long originalId;
  private OkrCompany expectedOkrCompany;
  private Cycle originalCycle;
  private OkrCompanyHistory originalHistory;
  private String originalName;
  private String originalLabel;
  private ArrayList<OkrChildUnit> originalDepartments;

  @BeforeEach
  public void init() {
    originalCycle = new Cycle();
    originalHistory = new OkrCompanyHistory();
    originalName = "originalName";
    originalLabel = "originalLabel";
    originalDepartments = new ArrayList<>();
    originalDepartments.add(new OkrDepartment());
    originalId = 100L;

    expectedOkrCompany = new OkrCompany();
    expectedOkrCompany.setId(originalId);
    expectedOkrCompany.setCycle(originalCycle);
    expectedOkrCompany.setHistory(originalHistory);
    expectedOkrCompany.setName(originalName);
    expectedOkrCompany.setLabel(originalLabel);
    expectedOkrCompany.setOkrChildUnits(originalDepartments);
  }

  @Test
  public void getCopyWithoutRelations_expectedNotNull() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    assertNotNull(actualOkrCompany);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    assertNotEquals(expectedOkrCompany, actualOkrCompany);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    assertNotEquals(expectedOkrCompany.getId(), actualOkrCompany.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectCopiedInformation() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    assertEquals(originalName, actualOkrCompany.getName());
    assertEquals(originalLabel, actualOkrCompany.getLabel());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    assertNull(actualOkrCompany.getCycle());
    assertEquals(0, actualOkrCompany.getOkrChildUnits().size());
  }
}
