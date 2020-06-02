package org.burningokr.model.structures;

import java.util.ArrayList;
import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.model.cycles.Cycle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompanyTest {

  private long originalId;
  private Company expectedCompany;
  private Cycle originalCycle;
  private CompanyHistory originalHistory;
  private String originalName;
  private String originalLabel;
  private ArrayList<SubStructure> originalDepartments;

  @Before
  public void init() {
    originalCycle = new Cycle();
    originalHistory = new CompanyHistory();
    originalName = "originalName";
    originalLabel = "originalLabel";
    originalDepartments = new ArrayList<>();
    originalDepartments.add(new Department());
    originalId = 100L;

    expectedCompany = new Company();
    expectedCompany.setId(originalId);
    expectedCompany.setCycle(originalCycle);
    expectedCompany.setHistory(originalHistory);
    expectedCompany.setName(originalName);
    expectedCompany.setLabel(originalLabel);
    expectedCompany.setSubStructures(originalDepartments);
  }

  @Test
  public void getCopyWithoutRelations_expectedNotNull() {
    Company actualCompany = expectedCompany.getCopyWithoutRelations();

    Assert.assertNotNull(actualCompany);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    Company actualCompany = expectedCompany.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedCompany, actualCompany);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    Company actualCompany = expectedCompany.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedCompany.getId(), actualCompany.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectCopiedInformation() {
    Company actualCompany = expectedCompany.getCopyWithoutRelations();

    Assert.assertEquals(originalName, actualCompany.getName());
    Assert.assertEquals(originalLabel, actualCompany.getLabel());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    Company actualCompany = expectedCompany.getCopyWithoutRelations();

    Assert.assertNull(actualCompany.getCycle());
    Assert.assertEquals(0, actualCompany.getSubStructures().size());
  }
}
