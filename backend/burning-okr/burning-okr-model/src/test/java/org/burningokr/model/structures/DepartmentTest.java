package org.burningokr.model.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentTest {

  private Department expectedDepartment;
  private CorporateObjectiveStructure corporateObjectiveStructure;
  private long originalId;
  private Company originalCompany;
  private String originalName;
  private String originalLabel;
  private Collection<UUID> originalOkrMembers;
  private UUID originalOkrMasterUuid;
  private UUID originalOkrSponsorUuid;
  private Collection<Department> originalDepartments;

  @Before
  public void init() {
    originalCompany = new Company();
    originalName = "originalName";
    originalLabel = "originalLabel";
    originalOkrMembers = new ArrayList<>();
    originalOkrMembers.add(UUID.randomUUID());
    originalOkrMembers.add(UUID.randomUUID());
    originalOkrMasterUuid = UUID.randomUUID();
    originalOkrSponsorUuid = UUID.randomUUID();
    originalDepartments = new ArrayList<>();
    originalDepartments.add(new Department());
    originalId = 100L;

    expectedDepartment = new Department();
    expectedDepartment.setName(originalName);
    expectedDepartment.setLabel(originalLabel);
    expectedDepartment.setOkrMemberIds(originalOkrMembers);
    expectedDepartment.setOkrMasterId(originalOkrMasterUuid);
    expectedDepartment.setOkrTopicSponsorId(originalOkrSponsorUuid);
    expectedDepartment.setParentStructure(originalCompany);
    expectedDepartment.setId(originalId);
    expectedDepartment.setActive(true);

    corporateObjectiveStructure = new CorporateObjectiveStructure();
    corporateObjectiveStructure.setSubStructures(Collections.singleton(expectedDepartment));
  }

  @Test
  public void getCopyWithoutRelations_expectedNotNull() {
    Department actualDepartment = expectedDepartment.getCopyWithoutRelations();

    Assert.assertNotNull(actualDepartment);
  }

  @Test
  public void getCopyWithoutRelations_expectedCopyNotOriginal() {
    Department actualDepartment = expectedDepartment.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedDepartment, actualDepartment);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    Department actualDepartment = expectedDepartment.getCopyWithoutRelations();

    Assert.assertNotEquals(expectedDepartment.getId(), actualDepartment.getId());
  }

  @Test
  public void getCopyWithoutRelations_expectedMemberListDeepClone() {
    Department actualDepartment = expectedDepartment.getCopyWithoutRelations();

    Assert.assertEquals(originalName, actualDepartment.getName());
    Assert.assertEquals(originalLabel, actualDepartment.getLabel());
    Assert.assertEquals(originalOkrMasterUuid, actualDepartment.getOkrMasterId());
    Assert.assertEquals(originalOkrSponsorUuid, actualDepartment.getOkrTopicSponsorId());
    Assert.assertTrue(actualDepartment.isActive());
  }

  @Test
  public void getCopyWithoutRelations_expectedCorrectNameList() {
    Department actualDepartment = expectedDepartment.getCopyWithoutRelations();

    Assert.assertEquals(originalOkrMembers, actualDepartment.getOkrMemberIds());
    Assert.assertNotSame(originalOkrMembers, actualDepartment.getOkrMemberIds());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    CorporateObjectiveStructure actualDepartment =
        corporateObjectiveStructure.getCopyWithoutRelations();

    Assert.assertNull(actualDepartment.getParentStructure());
    Assert.assertEquals(0, actualDepartment.getSubStructures().size());
  }
}
