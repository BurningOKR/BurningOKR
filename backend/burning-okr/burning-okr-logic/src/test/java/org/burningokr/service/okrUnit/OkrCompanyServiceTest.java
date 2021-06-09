package org.burningokr.service.okrUnit;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.histories.OkrTopicDraftHistory;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrDepartmentHistory;
import org.burningokr.model.users.User;
import org.burningokr.repositories.cycle.BranchHistoryRepository;
import org.burningokr.repositories.cycle.DepartmentHistoryRepository;
import org.burningokr.repositories.cycle.TopicDraftHistoryRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okr.TaskBoardService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrCompanyServiceTest {

  @Mock private CompanyRepository companyRepository;

  @Mock private UnitRepository<OkrChildUnit> unitRepository;

  @Mock private EntityCrawlerService entityCrawlerService;

  @Mock private ObjectiveRepository objectiveRepository;

  @Mock private ActivityService activityService;

  @Mock private OkrTopicDescriptionRepository okrTopicDescriptionRepository;

  @Mock private BranchHistoryRepository branchHistoryRepository;

  @Mock private DepartmentHistoryRepository departmentHistoryRepository;

  @Mock private TopicDraftHistoryRepository topicDraftHistoryRepository;

  @Mock private OkrTopicDraftRepository okrTopicDraftRepository;

  @Mock private User user;

  @InjectMocks private CompanyService companyService;

  @Mock private TaskBoardService taskBoardService;

  private final Long companyId = 1337L;
  private final String companyName = "Brockhaus AG";
  private final String updatedCompanyName = "BAG";
  private OkrCompany okrCompany;

  @Before
  public void setUp() {
    okrCompany = new OkrCompany();
    okrCompany.setId(companyId);
    okrCompany.setName(companyName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(activeCycle);
  }

  @Test
  public void createDepartment_expectsParentUnitIsSet() {
    OkrDepartment okrDepartment = new OkrDepartment();
    TaskBoard taskBoard = new TaskBoard();

    User user = mock(User.class);
    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(okrDepartment);
    when(taskBoardService.createNewTaskBoardWithDefaultStates()).thenReturn(taskBoard);
    when(taskBoardService.saveTaskBoard(any())).thenReturn(taskBoard);

    companyService.createDepartment(companyId, okrDepartment, user);

    Assert.assertEquals(okrCompany.getId(), okrDepartment.getParentOkrUnit().getId());

    verify(companyRepository).findByIdOrThrow(any(Long.class));
    verify(unitRepository).save(any(OkrDepartment.class));
  }

  @Test
  public void createDepartment_cycleOfDepartmentClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    OkrDepartment okrDepartment = new OkrDepartment();
    User user = mock(User.class);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(entityCrawlerService.getCycleOfCompany(okrCompany)).thenReturn(closedCycle);
    try {
      companyService.createDepartment(companyId, okrDepartment, user);
      Assert.fail();
    } catch (Exception ex) {
      assertThat("Should only throw ForbiddenException.", ex, instanceOf(ForbiddenException.class));
    }
  }

  @Test
  public void createDepartment_expectOkrTopicDescriptionIsCreated() {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setName("test");
    TaskBoard taskBoard = new TaskBoard();

    User user = mock(User.class);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(okrDepartment);
    when(okrTopicDescriptionRepository.save(any()))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(taskBoardService.createNewTaskBoardWithDefaultStates()).thenReturn(taskBoard);
    when(taskBoardService.saveTaskBoard(any())).thenReturn(taskBoard);

    OkrDepartment created = companyService.createDepartment(companyId, okrDepartment, user);

    assertNotNull(created.getOkrTopicDescription());
    assertEquals(okrDepartment.getName(), created.getOkrTopicDescription().getName());
  }

  @Test
  public void createDepartment_expectTaskBoardIsCreated() {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setName("test");
    TaskBoard taskBoard = new TaskBoard();
    Collection<TaskState> availableStates = new ArrayList();

    TaskState state1 = new TaskState();
    state1.setParentTaskBoard(taskBoard);
    state1.setTitle("state1");
    state1.setId(1l);

    availableStates.add(state1);
    taskBoard.setAvailableStates(availableStates);

    User user = mock(User.class);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(okrDepartment);
    when(okrTopicDescriptionRepository.save(any()))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(taskBoardService.createNewTaskBoardWithDefaultStates()).thenReturn(taskBoard);
    when(taskBoardService.saveTaskBoard(any())).thenReturn(taskBoard);

    OkrDepartment created = companyService.createDepartment(companyId, okrDepartment, user);

    assertNotNull(created.getTaskBoard());
  }

  @Test
  public void updateCompany_expectsNameIsUpdated() {
    OkrCompany updateOkrCompany = new OkrCompany();
    updateOkrCompany.setId(companyId);
    updateOkrCompany.setName(updatedCompanyName);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(companyRepository.save(any(OkrCompany.class))).thenReturn(updateOkrCompany);

    User user = mock(User.class);
    okrCompany = companyService.updateCompany(updateOkrCompany, user);

    Assert.assertEquals(updatedCompanyName, okrCompany.getName());

    verify(companyRepository).findByIdOrThrow(anyLong());
    verify(companyRepository).save(any(OkrCompany.class));
  }

  @Test
  public void updateCompany_cycleOfCompanyIsClosed_expectedForbiddenThrow() {
    OkrCompany updateOkrCompany = new OkrCompany();
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(closedCycle);

    User user = mock(User.class);
    try {
      okrCompany = companyService.updateCompany(updateOkrCompany, user);
      Assert.fail();
    } catch (Exception ex) {
      assertThat("Should only throw ForbiddenException.", ex, instanceOf(ForbiddenException.class));
    }
  }

  @Test
  public void updateCompany_expectsEntityNotFoundException() {
    OkrCompany updateOkrCompany = new OkrCompany();
    updateOkrCompany.setId(companyId);
    updateOkrCompany.setName(updatedCompanyName);

    when(companyRepository.findByIdOrThrow(anyLong())).thenThrow(new EntityNotFoundException());

    User user = mock(User.class);
    try {
      okrCompany = companyService.updateCompany(updateOkrCompany, user);
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
    }

    verify(companyRepository).findByIdOrThrow(anyLong());
  }

  @Test
  public void deleteCompany_shouldDeleteCompany() {
    when(companyRepository.findByIdOrThrow(companyId)).thenReturn(okrCompany);

    companyService.deleteCompany(companyId, true, user);

    verify(companyRepository).deleteById(companyId);
  }

  @Test
  public void deleteCompany_expectsEntityNotFoundException() {
    Long notExistingCompanyId = 2000L;

    when(companyRepository.findByIdOrThrow(2000L)).thenThrow(EntityNotFoundException.class);

    try {
      companyService.deleteCompany(notExistingCompanyId, true, user);
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
    }
  }

  @Test(expected = ForbiddenException.class)
  public void createOkrDraft_cycleClosed_expectedThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(closedCycle);

    OkrTopicDraft topicDraft = new OkrTopicDraft();

    companyService.createTopicDraft(1L, topicDraft, user);
  }

  @Test
  public void createOkrDraft_expectIsSavedToDatabase() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    companyService.createTopicDraft(1L, topicDraft, user);

    verify(okrTopicDraftRepository).save(any());
  }

  @Test
  public void createOkrDraft_expectCreatesActivity() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    companyService.createTopicDraft(1L, topicDraft, user);

    verify(activityService).createActivity(eq(user), any(), eq(Action.CREATED));
  }

  @Test
  public void createOkrDraft_expectSetsParentUnit() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    OkrTopicDraft actual = companyService.createTopicDraft(1L, topicDraft, user);

    assertEquals(actual.getParentUnit(), okrCompany);
  }

  @Test
  public void createOkrBranch_expectsSetsHistory() {
    OkrBranchHistory okrBranchHistory = new OkrBranchHistory();
    OkrBranch okrBranch = new OkrBranch();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(branchHistoryRepository.save(any())).thenReturn(okrBranchHistory);
    when(unitRepository.save(any())).thenReturn(okrBranch);

    OkrBranch createdOkrBranch = companyService.createOkrBranch(1L, okrBranch, user);

    assertSame(okrBranchHistory, createdOkrBranch.getHistory());
  }

  @Test (expected = ForbiddenException.class)
  public void createOkrBranch_expectsForbiddenException() {
    OkrBranch okrBranch = new OkrBranch();
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfCompany(okrCompany)).thenReturn(closedCycle);
    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);

    companyService.createOkrBranch(1L, okrBranch, user);
  }

  @Test
  public void createOkrBranchHistory_expectsToBeSavedToDb() {
    OkrBranchHistory okrBranchHistory = new OkrBranchHistory();
    OkrBranch okrBranch = new OkrBranch();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(branchHistoryRepository.save(any())).thenReturn(okrBranchHistory);
    when(unitRepository.save(any())).thenReturn(okrBranch);

    companyService.createOkrBranch(1L, okrBranch, user);

    verify(branchHistoryRepository).save(okrBranchHistory);
  }

  @Test
  public void createDepartment_expectsSetsHistory() {
    OkrDepartment okrDepartment = new OkrDepartment();
    OkrDepartmentHistory okrDepartmentHistory = new OkrDepartmentHistory();
    OkrTopicDescription okrTopicDescription = new OkrTopicDescription();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(departmentHistoryRepository.save(any())).thenReturn(okrDepartmentHistory);
    when(okrTopicDescriptionRepository.save(any())).thenReturn(okrTopicDescription);
    when(unitRepository.save(any())).thenReturn(okrDepartment);

    OkrDepartment createdOkrDepartment = companyService.createDepartment(1L, okrDepartment, user);

    assertSame(okrDepartmentHistory, createdOkrDepartment.getHistory());
  }

  @Test
  public void createOkrDepartmentHistory_expectsToBeSavedToDb() {
    OkrDepartmentHistory okrDepartmentHistory = new OkrDepartmentHistory();
    OkrDepartment okrDepartment = new OkrDepartment();

    when(departmentHistoryRepository.save(any())).thenReturn(okrDepartmentHistory);

    companyService.createHistory(okrDepartment, okrDepartmentHistory, departmentHistoryRepository);

    verify(departmentHistoryRepository).save(any());
  }

  @Test
  public void createTopicDraftHistory_expectsToBeSavedToDb() {
    OkrTopicDraftHistory okrTopicDraftHistory = new OkrTopicDraftHistory();
    OkrTopicDraft okrTopicDraft = new OkrTopicDraft();

    companyService.createTopicDraftHistory(okrTopicDraft, okrTopicDraftHistory, topicDraftHistoryRepository);
    verify(topicDraftHistoryRepository).save(any());
  }

  @Test
  public void createTopicDraftHistory_expectsSetsHistory() {
    OkrTopicDraftHistory okrTopicDraftHistory = new OkrTopicDraftHistory();
    OkrTopicDescription okrTopicDescription = new OkrTopicDescription();
    OkrTopicDraft okrTopicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(topicDraftHistoryRepository.save(any())).thenReturn(okrTopicDraftHistory);
    when(okrTopicDraftRepository.save(any())).thenReturn(okrTopicDraft);

    OkrTopicDraft createdOkrTopicDraft = companyService.createTopicDraft(1L, okrTopicDraft, user);

    assertSame(okrTopicDraftHistory, createdOkrTopicDraft.getHistory());
  }
}
