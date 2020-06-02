package org.burningokr.service.structure;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.structre.CorporateObjectiveStructureRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.IdDeviationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CorporateObjectiveStructureServiceTest {

  @Mock private CorporateObjectiveStructureRepository corporateObjectiveStructureRepository;

  @Mock private ActivityService activityService;

  @InjectMocks private CorporateObjectiveStructureService corporateObjectiveStructureService;

  @Mock private final User mockedUser = mock(User.class);

  private final long corporateObjectiveStructureId = 42L;
  private CorporateObjectiveStructure corporateObjectiveStructure;

  @Before
  public void init() {
    corporateObjectiveStructure = new CorporateObjectiveStructure();
    corporateObjectiveStructure.setId(corporateObjectiveStructureId);
    when(corporateObjectiveStructureRepository.findByIdOrThrow(corporateObjectiveStructureId))
        .thenReturn(corporateObjectiveStructure);
  }

  @Test(expected = EntityNotFoundException.class)
  public void findById_expectEntityNotFoundExceptionIfItNotExists() {
    when(corporateObjectiveStructureRepository.findByIdOrThrow(anyLong()))
        .thenThrow(EntityNotFoundException.class);
    corporateObjectiveStructureService.findById(444L);
  }

  @Test
  public void findById_expectEntityFound() {
    CorporateObjectiveStructure actual =
        corporateObjectiveStructureService.findById(corporateObjectiveStructureId);
    Assert.assertEquals(corporateObjectiveStructure, actual);
  }

  @Test
  public void create_expectEntitySaved() {
    when(corporateObjectiveStructureRepository.save(corporateObjectiveStructure))
        .thenReturn(corporateObjectiveStructure);
    CorporateObjectiveStructure actual =
        corporateObjectiveStructureService.create(corporateObjectiveStructure, mockedUser);
    Assert.assertEquals(corporateObjectiveStructure, actual);
    verify(corporateObjectiveStructureRepository).save(corporateObjectiveStructure);
  }

  @Test
  public void update_expectEntityNotFoundExceptionIfThereIsNoSuchEntity() {
    long id = 444L;
    when(corporateObjectiveStructureRepository.findByIdOrThrow(id))
        .thenThrow(EntityNotFoundException.class);
    corporateObjectiveStructure.setId(id);
    try {
      corporateObjectiveStructureService.update(id, corporateObjectiveStructure, mockedUser);
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
    }
    verify(corporateObjectiveStructureRepository).findByIdOrThrow(id);
  }

  @Test
  public void update_EntityShouldHaveBeenUpdated() {
    CorporateObjectiveStructure updatedEntity = new CorporateObjectiveStructure();
    updatedEntity.setId(corporateObjectiveStructureId);
    updatedEntity.setName("s");
    updatedEntity.setObjectives(Arrays.asList(new Objective(), new Objective()));
    updatedEntity.setSubStructures(Arrays.asList(new Department(), new Department()));
    updatedEntity.setParentStructure(new Department());
    CorporateObjectiveStructure actual =
        corporateObjectiveStructureService.update(
            corporateObjectiveStructureId, updatedEntity, mockedUser);
    Assert.assertEquals(updatedEntity, actual);
    verify(corporateObjectiveStructureRepository).findByIdOrThrow(anyLong());
    verify(corporateObjectiveStructureRepository).save(any());
  }

  @Test(expected = IdDeviationException.class)
  public void update_EntityIdAndIdAreDifferentShouldThrowIdDeviationException() {
    CorporateObjectiveStructure updatedEntity = new CorporateObjectiveStructure();
    updatedEntity.setId(444L);
    corporateObjectiveStructureService.update(
        corporateObjectiveStructureId, updatedEntity, mockedUser);
  }

  @Test
  public void delete_expectedEntityNotFoundExceptionIfThereIsNoSuchEntity() {
    long id = 444L;
    when(corporateObjectiveStructureRepository.findByIdOrThrow(id))
        .thenThrow(EntityNotFoundException.class);
    corporateObjectiveStructure.setId(id);
    try {
      corporateObjectiveStructureService.delete(id, mockedUser);
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
    }
    verify(corporateObjectiveStructureRepository).findByIdOrThrow(id);
  }

  @Test
  public void delete_expectEntityShouldBeDeleted() {
    corporateObjectiveStructureService.delete(corporateObjectiveStructureId, mockedUser);
    verify(corporateObjectiveStructureRepository).delete(corporateObjectiveStructure);
  }
}
