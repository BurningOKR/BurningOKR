package org.burningokr.model.okrUnits;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.burningokr.model.okr.OkrTopicDescription;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

  @Before
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

    okrBranch = new OkrBranch();
    okrBranch.setOkrChildUnits(Collections.singleton(expectedOkrDepartment));
  }

  @Test
  public void getCopyWithoutRelations_expectedNotNull() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    Assert.assertNotNull(actualOkrDepartment);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedOkrDepartment, actualOkrDepartment);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedOkrDepartment.getId(), actualOkrDepartment.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedMemberListDeepClone() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    Assert.assertEquals(originalName, actualOkrDepartment.getName());
    Assert.assertEquals(originalLabel, actualOkrDepartment.getLabel());
    Assert.assertEquals(originalOkrMasterUuid, actualOkrDepartment.getOkrMasterId());
    Assert.assertEquals(originalOkrSponsorUuid, actualOkrDepartment.getOkrTopicSponsorId());
    Assert.assertTrue(actualOkrDepartment.isActive());
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectNameList() {
    OkrDepartment actualOkrDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    Assert.assertEquals(originalOkrMembers, actualOkrDepartment.getOkrMemberIds());
    Assert.assertNotSame(originalOkrMembers, actualOkrDepartment.getOkrMemberIds());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    OkrBranch actualDepartment = okrBranch.getCopyWithoutRelations();

    Assert.assertNull(actualDepartment.getParentOkrUnit());
    Assert.assertEquals(0, actualDepartment.getOkrChildUnits().size());
  }

  @Test
  public void getCopyWithoutRelations_expectedTopicDescriptionsAreEqual() {
    OkrDepartment actualDepartment = expectedOkrDepartment.getCopyWithoutRelations();

    Assert.assertSame(expectedOkrDepartment.getOkrTopicDescription(), actualDepartment.getOkrTopicDescription());
  }
}
