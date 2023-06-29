package org.burningokr.service.okrUnit;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.model.okr.histories.OkrTopicDraftHistory;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrDepartmentHistory;
import org.burningokr.repositories.cycle.BranchHistoryRepository;
import org.burningokr.repositories.cycle.DepartmentHistoryRepository;
import org.burningokr.repositories.cycle.TopicDraftHistoryRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.repositories.okrUnit.OkrUnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okr.TaskBoardService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OkrCompanyServiceTest {

  private final Long companyId = 1337L;
  private final String companyName = "Brockhaus AG";
  private final String updatedCompanyName = "BAG";
  @Mock
  private CompanyRepository companyRepository;
  @Mock
  private OkrUnitRepository<OkrChildUnit> unitRepository;
  @Mock
  private EntityCrawlerService entityCrawlerService;
  @Mock
  private ObjectiveRepository objectiveRepository;
  @Mock
  private ActivityService activityService;
  @Mock
  private OkrTopicDescriptionRepository topicDescriptionRepository;
  @Mock
  private BranchHistoryRepository branchHistoryRepository;
  @Mock
  private DepartmentHistoryRepository departmentHistoryRepository;
  @Mock
  private TopicDraftHistoryRepository topicDraftHistoryRepository;
  @Mock
  private OkrTopicDraftRepository topicDraftRepository;
  @InjectMocks
  private CompanyService companyService;
  @Mock
  private TaskBoardService taskBoardService;
  private OkrCompany company;

  @BeforeEach
  public void setUp() {
    company = new OkrCompany();
    company.setId(companyId);
    company.setName(companyName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    Mockito.lenient().when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(activeCycle);
  }

  @Test
  public void createDepartment_shouldCheckIfParentUnitIsSet() {
    OkrDepartment department = new OkrDepartment();
    TaskBoard taskBoard = new TaskBoard();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(department);
    when(taskBoardService.createNewTaskBoardWithDefaultStates(department)).thenReturn(taskBoard);
    //TODO (F. L. 28.06.2023) is this needed?
    //when(taskBoardService.saveTaskBoard(any())).thenReturn(taskBoard);

    companyService.createDepartment(companyId, department);

    assertEquals(company.getId(), department.getParentOkrUnit().getId());
    verify(companyRepository).findByIdOrThrow(any(Long.class));
    verify(unitRepository).save(any(OkrDepartment.class));
  }

  @Test
  public void createDepartment_shouldThrowForbiddenWhenCycleOfDepartmentIsClosed() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    OkrDepartment department = new OkrDepartment();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(entityCrawlerService.getCycleOfCompany(company)).thenReturn(closedCycle);
    try {
      companyService.createDepartment(companyId, department);
      fail();
    } catch (Exception ex) {
      assertEquals(ex.getClass(), ForbiddenException.class);
    }
  }

  @Test
  public void createDepartment_shouldCreateOkrTopicDescription() {
    OkrDepartment department = new OkrDepartment();
    department.setName("test");
    TaskBoard taskBoard = new TaskBoard();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(department);
    when(topicDescriptionRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));
    when(taskBoardService.createNewTaskBoardWithDefaultStates(department)).thenReturn(taskBoard);
    //TODO (F. L. 28.06.2023) is this needed?
    //when(taskBoardService.saveTaskBoard(any())).thenReturn(taskBoard);

    OkrDepartment created = companyService.createDepartment(companyId, department);

    assertNotNull(created.getOkrTopicDescription());
    assertEquals(department.getName(), created.getOkrTopicDescription().getName());
  }

  @Test
  public void createDepartment_shouldCreateTaskBoard() {
    OkrDepartment department = new OkrDepartment();
    department.setName("test");
    TaskBoard taskBoard = new TaskBoard();
    Collection<TaskState> availableStates = new ArrayList<>();
    TaskState state1 = new TaskState();
    state1.setParentTaskBoard(taskBoard);
    state1.setTitle("state1");
    state1.setId(1L);
    availableStates.add(state1);
    taskBoard.setAvailableStates(availableStates);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(department);
    when(topicDescriptionRepository.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));
    when(taskBoardService.createNewTaskBoardWithDefaultStates(department)).thenReturn(taskBoard);
    //TODO (F. L. 28.06.2023) is this needed?
    //when(taskBoardService.saveTaskBoard(any())).thenReturn(taskBoard);

    OkrDepartment created = companyService.createDepartment(companyId, department);

    assertNotNull(created.getTaskBoard());
  }

  @Test
  public void updateCompany_shouldUpdateName() {
    OkrCompany updateCompany = new OkrCompany();
    updateCompany.setId(companyId);
    updateCompany.setName(updatedCompanyName);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(companyRepository.save(any(OkrCompany.class))).thenReturn(updateCompany);

    company = companyService.updateCompany(updateCompany);

    assertEquals(updatedCompanyName, company.getName());

    verify(companyRepository).findByIdOrThrow(anyLong());
    verify(companyRepository).save(any(OkrCompany.class));
  }

  @Test
  public void updateCompany_shouldThrowForbiddenWhenCycleOfDepartmentIsClosed() {
    OkrCompany updateCompany = new OkrCompany();
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(closedCycle);

    try {
      company = companyService.updateCompany(updateCompany);
      fail();
    } catch (Exception ex) {
      assertEquals(ex.getClass(), ForbiddenException.class);
    }
  }

  @Test
  public void updateCompany_shouldThrowEntityNotFound() {
    OkrCompany updateCompany = new OkrCompany();
    updateCompany.setId(companyId);
    updateCompany.setName(updatedCompanyName);

    when(companyRepository.findByIdOrThrow(anyLong())).thenThrow(new EntityNotFoundException());
    try {
      company = companyService.updateCompany(updateCompany);
      fail();
    } catch (Exception ex) {
      assertEquals(ex.getClass(), EntityNotFoundException.class);
    }
    verify(companyRepository).findByIdOrThrow(anyLong());
  }

  @Test
  public void deleteCompany_shouldDeleteCompany() {
    when(companyRepository.findByIdOrThrow(companyId)).thenReturn(company);

    companyService.deleteCompany(companyId, true);

    verify(companyRepository).deleteById(companyId);
  }

  @Test
  public void deleteCompany_shouldThrowEntityNotFoundException() {
    Long notExistingCompanyId = 2000L;

    when(companyRepository.findByIdOrThrow(2000L)).thenThrow(EntityNotFoundException.class);
    try {
      companyService.deleteCompany(notExistingCompanyId, true);
      fail();
    } catch (Exception ex) {
      assertEquals(ex.getClass(), EntityNotFoundException.class);
    }
  }

  @Test
  public void createOkrDraft_shouldThrowExceptionWhenCycleIsClosed() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(closedCycle);

    assertThrows(ForbiddenException.class, () -> companyService.createTopicDraft(1L, topicDraft));
  }

  @Test
  public void createOkrDraft_shouldSaveToDatabase() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(topicDraftRepository.save(any())).thenReturn(topicDraft);

    companyService.createTopicDraft(1L, topicDraft);

    verify(topicDraftRepository).save(any());
  }

  @Test
  public void createOkrDraft_shouldCreateActivity() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(topicDraftRepository.save(any())).thenReturn(topicDraft);

    companyService.createTopicDraft(1L, topicDraft);

    verify(activityService).createActivity(any(), eq(Action.CREATED));
  }

  @Test
  public void createOkrDraft_shouldSetParentUnit() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(topicDraftRepository.save(any())).thenReturn(topicDraft);

    OkrTopicDraft actual = companyService.createTopicDraft(1L, topicDraft);

    assertEquals(actual.getParentUnit(), company);
  }

  @Test
  public void createOkrBranch_shouldSetHistory() {
    OkrBranchHistory branchHistory = new OkrBranchHistory();
    OkrBranch branch = new OkrBranch();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(branchHistoryRepository.save(any())).thenReturn(branchHistory);
    when(unitRepository.save(any())).thenReturn(branch);

    OkrBranch createdOkrBranch = companyService.createOkrBranch(1L, branch);

    assertSame(branchHistory, createdOkrBranch.getBranchHistory());
  }

  @Test
  public void createOkrBranch_shouldThrowForbiddenException() {
    OkrBranch branch = new OkrBranch();
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfCompany(company)).thenReturn(closedCycle);
    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);

    assertThrows(ForbiddenException.class, () -> companyService.createOkrBranch(1L, branch));
  }

  @Test
  public void createOkrBranchHistory_shouldSaveToDatabase() {
    OkrBranchHistory branchHistory = new OkrBranchHistory();
    OkrBranch branch = new OkrBranch();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(branchHistoryRepository.save(any())).thenReturn(branchHistory);
    when(unitRepository.save(any())).thenReturn(branch);

    companyService.createOkrBranch(1L, branch);

    verify(branchHistoryRepository).save(branchHistory);
  }

  @Test
  public void createDepartment_shouldSetHistory() {
    OkrDepartment department = new OkrDepartment();
    OkrDepartmentHistory departmentHistory = new OkrDepartmentHistory();
    OkrTopicDescription topicDescription = new OkrTopicDescription();
    TaskBoard taskBoard = new TaskBoard();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(departmentHistoryRepository.save(any())).thenReturn(departmentHistory);
    when(topicDescriptionRepository.save(any())).thenReturn(topicDescription);
    when(unitRepository.save(any())).thenReturn(department);
    when(taskBoardService.createNewTaskBoardWithDefaultStates(department)).thenReturn(taskBoard);

    OkrDepartment createdOkrDepartment = companyService.createDepartment(1L, department);

    assertSame(departmentHistory, createdOkrDepartment.getDepartmentHistory());
  }

  @Test
  public void createOkrDepartmentHistory_shouldSaveToDatabase() {
    OkrDepartmentHistory departmentHistory = new OkrDepartmentHistory();
    OkrDepartment department = new OkrDepartment();

    when(departmentHistoryRepository.save(any())).thenReturn(departmentHistory);

    companyService.createHistory(department, departmentHistory, departmentHistoryRepository);

    verify(departmentHistoryRepository).save(any());
  }

  @Test
  public void createTopicDraftHistory_shouldSaveToDatabase() {
    OkrTopicDraftHistory topicDraftHistory = new OkrTopicDraftHistory();
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    companyService.createTopicDraftHistory(
            topicDraft, topicDraftHistory, topicDraftHistoryRepository);
    verify(topicDraftHistoryRepository).save(any());
  }

  @Test
  public void createTopicDraftHistory_shouldSetHistory() {
    OkrTopicDraftHistory topicDraftHistory = new OkrTopicDraftHistory();
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(topicDraftHistoryRepository.save(any())).thenReturn(topicDraftHistory);
    when(topicDraftRepository.save(any())).thenReturn(topicDraft);

    OkrTopicDraft createdOkrTopicDraft = companyService.createTopicDraft(1L, topicDraft);

    assertSame(topicDraftHistory, createdOkrTopicDraft.getHistory());
  }
}
