package org.burningokr.model.okrUnits;

import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrDepartmentHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class OkrDepartmentTest {

  private OkrDepartment expectedOkrDepartment;
  private OkrBranch okrBranch;
  private long originalId;
  private OkrCompany originalOkrCompany;
  private String originalName;
  private String originalLabel;
  private Collection<UUID> originalOkrMembers;
  private UUID originalOkrMasterUuid;
  private UUID originalOkrSponsorUuid;
  private Collection<OkrDepartment> originalOkrDepartments;
  private OkrTopicDescription originalOkrTopicDescription;

  @BeforeEach
  public void init() {
    originalOkrCompany = new OkrCompany();
    originalName = "originalName";
    originalLabel = "originalLabel";
    originalOkrMembers = new ArrayList<>();
    originalOkrMembers.add(UUID.randomUUID());
    originalOkrMembers.add(UUID.randomUUID());
    originalOkrMasterUuid = UUID.randomUUID();
    originalOkrSponsorUuid = UUID.randomUUID();
    originalOkrDepartments = new ArrayList<>();
    originalOkrDepartments.add(new OkrDepartment());
    originalId = 100L;
    originalOkrTopicDescription = new OkrTopicDescription();

    expectedOkrDepartment = new OkrDepartment();
    expectedOkrDepartment.setName(originalName);
    expectedOkrDepartment.setLabel(originalLabel);
    expectedOkrDepartment.setOkrMemberIds(originalOkrMembers);
    expectedOkrDepartment.setOkrMasterId(originalOkrMasterUuid);
    expectedOkrDepartment.setOkrTopicSponsorId(originalOkrSponsorUuid);
    expectedOkrDepartment.setParentOkrUnit(originalOkrCompany);
    expectedOkrDepartment.setId(originalId);
    expectedOkrDepartment.setActive(true);
    expectedOkrDepartment.setOkrTopicDescription(originalOkrTopicDescription);

    OkrDepartmentHistory history = new OkrDepartmentHistory();
    history.setUnits(Collections.singleton(expectedOkrDepartment));
    expectedOkrDepartment.setHistory(history);

    okrBranch = new OkrBranch();
    okrBranch.setOkrChildUnits(Collections.singleton(expectedOkrDepartment));
  }

  @Test
  public void getCopyWithoutRelations_expectedNotNull() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();
    assertNotNull(actualOkrDepartment);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();
    assertNotEquals(expectedOkrDepartment, actualOkrDepartment);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    assertNotEquals(expectedOkrDepartment.getId(), actualOkrDepartment.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedMemberListDeepClone() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    assertEquals(originalName, actualOkrDepartment.getName());
    assertEquals(originalLabel, actualOkrDepartment.getLabel());
    assertEquals(originalOkrMasterUuid, actualOkrDepartment.getOkrMasterId());
    assertEquals(originalOkrSponsorUuid, actualOkrDepartment.getOkrTopicSponsorId());
    assertTrue(actualOkrDepartment.isActive());
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectNameList() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    assertEquals(originalOkrMembers, actualOkrDepartment.getOkrMemberIds());
    assertNotSame(originalOkrMembers, actualOkrDepartment.getOkrMemberIds());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    OkrBranch actualDepartment = okrBranch.getCopyWithoutRelations();

    assertNull(actualDepartment.getParentOkrUnit());
    assertEquals(0, actualDepartment.getOkrChildUnits().size());
  }

  @Test
  public void getCopyWithoutRelations_expectedTopicDescriptionsAreEqual() {
    OkrDepartment actualDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    assertSame(
      expectedOkrDepartment.getOkrTopicDescription(), actualDepartment.getOkrTopicDescription());
  }

  @Test
  public void getCopyWithoutRelations_expectHistoriesAreEqual() {
    OkrDepartment actualDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    assertSame(expectedOkrDepartment.getHistory(), actualDepartment.getHistory());
  }
}
