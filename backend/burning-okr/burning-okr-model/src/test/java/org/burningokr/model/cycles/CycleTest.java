package org.burningokr.model.cycles;

import org.burningokr.model.okrUnits.OkrCompany;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CycleTest {

  private Long originalId;
  private String originalName;
  private LocalDate originalPlannedStartDate;
  private LocalDate originalPlannedEndDate;
  private LocalDate originalFactualStartDate;
  private LocalDate originalFactualEndDate;
  private CycleState originalCycleState;
  private ArrayList<OkrCompany> originalCompanies;

  private Cycle expectedCycle;

  @BeforeEach
  public void init() {
    originalName = "originalName";
    originalId = 100L;
    originalPlannedStartDate = LocalDate.now().minusMonths(1);
    originalPlannedEndDate = LocalDate.now().plusMonths(1);
    originalFactualStartDate = originalPlannedStartDate.plusDays(1);
    originalFactualEndDate = originalPlannedEndDate.plusDays(1);
    originalCycleState = CycleState.ACTIVE;
    originalCompanies = new ArrayList<>();
    originalCompanies.add(new OkrCompany());

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

    assertNotNull(actualCycle);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    assertNotEquals(expectedCycle, actualCycle);
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectCopiedInformation() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    assertEquals(originalName, actualCycle.getName());
    assertEquals(originalPlannedStartDate, actualCycle.getPlannedStartDate());
    assertEquals(originalPlannedEndDate, actualCycle.getPlannedEndDate());
    assertEquals(originalFactualStartDate, actualCycle.getFactualStartDate());
    assertEquals(originalFactualEndDate, actualCycle.getFactualEndDate());
    assertEquals(originalCycleState, actualCycle.getCycleState());
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    assertNotEquals(expectedCycle.getId(), actualCycle.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    Cycle actualCycle = expectedCycle.getCopyWithoutRelations();

    assertEquals(0, actualCycle.getCompanies().size());
  }
}
