package org.burningokr.model.okrUnits;

import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class OkrCompanyTest {

  private long originalId;
  private OkrCompany expectedOkrCompany;
  private Cycle originalCycle;
  private OkrCompanyHistory originalHistory;
  private String originalName;
  private String originalLabel;
  private ArrayList<OkrChildUnit> originalDepartments;

  @Before
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

    Assert.assertNotNull(actualOkrCompany);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedOkrCompany, actualOkrCompany);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedOkrCompany.getId(), actualOkrCompany.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectCopiedInformation() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    Assert.assertEquals(originalName, actualOkrCompany.getName());
    Assert.assertEquals(originalLabel, actualOkrCompany.getLabel());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    OkrCompany actualOkrCompany = expectedOkrCompany.getCopyWithoutRelations();

    Assert.assertNull(actualOkrCompany.getCycle());
    Assert.assertEquals(0, actualOkrCompany.getOkrChildUnits().size());
  }
}
