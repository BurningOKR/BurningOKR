package org.burningokr.model.okrUnits;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OkrBranchTest {

  private OkrBranch original;

  @Before
  public void init() {
    original = new OkrBranch();
    original.setId(4L);
    original.setName("originalName");
  }

  @Test
  public void getCopyWithoutRelation_ExpectNotToBeNull() {
    OkrBranch copy = original.getCopyWithoutRelations();
    Assert.assertNotNull(copy);
  }

  @Test
  public void getCopyWithoutRelation_ExpectNameToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();
    Assert.assertEquals(original.getName(), copy.getName());
  }
}
