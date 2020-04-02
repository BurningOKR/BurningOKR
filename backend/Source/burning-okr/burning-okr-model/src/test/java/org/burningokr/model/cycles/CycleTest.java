package org.burningokr.model.cycles;

import java.time.LocalDate;
import java.util.ArrayList;
import org.burningokr.model.structures.Company;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CycleTest {

  private Long originalId;
  private String originalName;
  private LocalDate originalPlannedStartDate;
  private LocalDate originalPlannedEndDate;
  private LocalDate originalFactualStartDate;
  private LocalDate originalFactualEndDate;
  private CycleState originalCycleState;
  private ArrayList<Company> originalCompanies;

  private Cycle expectedCycle;

  @Before
  public void init() {
    originalName = "originalName";
    originalId = 100L;
    originalPlannedStartDate = LocalDate.now().minusMonths(1);
    originalPlannedEndDate = LocalDate.now().plusMonths(1);
    originalFactualStartDate = originalPlannedStartDate.plusDays(1);
    originalFactualEndDate = originalPlannedEndDate.plusDays(1);
    originalCycleState = CycleState.ACTIVE;
    originalCompanies = new ArrayList<>();
    originalCompanies.add(new Company());

    expectedCycle = new Cycle();
    expectedCycle.setId(originalId);
    expectedCycle.setName(originalName);
    expectedCycle.setPlannedStartDate(originalPlannedStartDate);
    expectedCycle.setPlannedEndDate(originalPlannedEndDate);
    expectedCycle.setFactualStartDate(originalFactualStartDate);
    expectedCycle.setFactualEndDate(originalFactualEndDate);
    expectedCycle.setCycleState(originalCycleState);
    expectedCycle.setCompanies(originalCompanies);
  }

  @Test
  public void getCopyWithoutRelations_expectedNotNull() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    Assert.assertNotNull(actualCycle);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedCycle, actualCycle);
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectCopiedInformation() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    Assert.assertEquals(originalName, actualCycle.getName());
    Assert.assertEquals(originalPlannedStartDate, actualCycle.getPlannedStartDate());
    Assert.assertEquals(originalPlannedEndDate, actualCycle.getPlannedEndDate());
    Assert.assertEquals(originalFactualStartDate, actualCycle.getFactualStartDate());
    Assert.assertEquals(originalFactualEndDate, actualCycle.getFactualEndDate());
    Assert.assertEquals(originalCycleState, actualCycle.getCycleState());
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedCycle.getId(), actualCycle.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    Assert.assertEquals(0, actualCycle.getCompanies().size());
  }
}
