package org.burningokr.model.structures;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CorporateObjectiveStructureTest {

  private CorporateObjectiveStructure original;

  @Before
  public void init() {
    original = new CorporateObjectiveStructure();
    original.setId(4L);
    original.setName("originalName");
  }

  @Test
  public void getCopyWithoutRelation_ExpectNotToBeNull() {
    CorporateObjectiveStructure copy = original.getCopyWithoutRelations();
    Assert.assertNotNull(copy);
  }

  @Test
  public void getCopyWithoutRelation_ExpectNameToBeEquals() {
    CorporateObjectiveStructure copy = original.getCopyWithoutRelations();
    Assert.assertEquals(original.getName(), copy.getName());
  }
}
