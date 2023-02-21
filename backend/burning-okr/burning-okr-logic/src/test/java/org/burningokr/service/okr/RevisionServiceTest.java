package org.burningokr.service.okr;

//TODO: fix Test
import junit.framework.TestCase;
import org.burningokr.model.okr.Revision;
import org.burningokr.service.userhandling.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

/*
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = {"/application-test.yaml"})
public class RevisionServiceTest extends TestCase {

  @Mock private UserService userService;

  @Autowired private TestEntityManager entityManager;

  @InjectMocks private RevisionService revisionService;

  @Before
  public void setUp() {
    revisionService.setEntityManager(entityManager.getEntityManager());
  }


  @Test
  public void testCompare() {
    TestEntity t = new TestEntity();
    entityManager.persist(t);
    //entityManager.flush();
    //entityManager.getEntityManager().getTransaction().commit();
    //System.out.println("entityManager = " + entityManager.);
    //entityManager.merge(t);
    System.out.println("t = " + t);
    Collection<Revision> revisions = revisionService.getRevisions(1l, TestEntity.class);
    System.out.println("revisions.size() = " + revisions.size());
    System.out.println("Test");
    System.out.println("revisions = " + entityManager.find(TestEntity.class,1l));
    ;
    for (Revision rev : revisions) {
      System.out.println("rev = " + rev);
    }
    assertTrue(true);
  }

  public void testGetObjectStringByFieldList() {}

  public void testStoresCollectionOfUUIDs() {}

  public void testGetChangedFields() {}

  public void testGetReferencedAuditedFields() {}

  public void testIsBurningOkrField() {}

  public void testGetAuditedFields() {}

  public void testGetValueByGetter() {}
}


 */
