package org.burningokr.service.okrUnit;

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
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okrUnit.OkrBranchRepository;
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
public class OkrBranchServiceTest {

  @Mock private OkrBranchRepository okrBranchRepository;

  @Mock private ActivityService activityService;

  @InjectMocks private OkrBranchService okrBranchService;

  @Mock private final User mockedUser = mock(User.class);

  private final long okrBranchId = 42L;
  private OkrBranch okrBranch;

  @Before
  public void init() {
    okrBranch = new OkrBranch();
    okrBranch.setId(okrBranchId);
    when(okrBranchRepository.findByIdOrThrow(okrBranchId)).thenReturn(okrBranch);
  }

  @Test(expected = EntityNotFoundException.class)
  public void findById_expectEntityNotFoundExceptionIfItNotExists() {
    when(okrBranchRepository.findByIdOrThrow(anyLong())).thenThrow(EntityNotFoundException.class);
    okrBranchService.findById(444L);
  }

  @Test
  public void findById_expectEntityFound() {
    OkrBranch actual = okrBranchService.findById(okrBranchId);
    Assert.assertEquals(okrBranch, actual);
  }

  @Test
  public void create_expectEntitySaved() {
    when(okrBranchRepository.save(okrBranch)).thenReturn(okrBranch);
    OkrBranch actual = okrBranchService.create(okrBranch, mockedUser);
    Assert.assertEquals(okrBranch, actual);
    verify(okrBranchRepository).save(okrBranch);
  }

  @Test
  public void update_expectEntityNotFoundExceptionIfThereIsNoSuchEntity() {
    long id = 444L;
    when(okrBranchRepository.findByIdOrThrow(id)).thenThrow(EntityNotFoundException.class);
    okrBranch.setId(id);
    try {
      okrBranchService.update(id, okrBranch, mockedUser);
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
    }
    verify(okrBranchRepository).findByIdOrThrow(id);
  }

  @Test
  public void update_EntityShouldHaveBeenUpdated() {
    OkrBranch updatedEntity = new OkrBranch();
    updatedEntity.setId(okrBranchId);
    updatedEntity.setName("s");
    updatedEntity.setObjectives(Arrays.asList(new Objective(), new Objective()));
    updatedEntity.setOkrChildUnits(Arrays.asList(new OkrDepartment(), new OkrDepartment()));
    updatedEntity.setParentOkrUnit(new OkrDepartment());
    OkrBranch actual = okrBranchService.update(okrBranchId, updatedEntity, mockedUser);
    Assert.assertEquals(updatedEntity, actual);
    verify(okrBranchRepository).findByIdOrThrow(anyLong());
    verify(okrBranchRepository).save(any());
  }

  @Test(expected = IdDeviationException.class)
  public void update_EntityIdAndIdAreDifferentShouldThrowIdDeviationException() {
    OkrBranch updatedEntity = new OkrBranch();
    updatedEntity.setId(444L);
    okrBranchService.update(okrBranchId, updatedEntity, mockedUser);
  }

  @Test
  public void delete_expectedEntityNotFoundExceptionIfThereIsNoSuchEntity() {
    long id = 444L;
    when(okrBranchRepository.findByIdOrThrow(id)).thenThrow(EntityNotFoundException.class);
    okrBranch.setId(id);
    try {
      okrBranchService.delete(id, mockedUser);
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
    }
    verify(okrBranchRepository).findByIdOrThrow(id);
  }

  @Test
  public void delete_expectEntityShouldBeDeleted() {
    okrBranchService.delete(okrBranchId, mockedUser);
    verify(okrBranchRepository).delete(okrBranch);
  }
}
