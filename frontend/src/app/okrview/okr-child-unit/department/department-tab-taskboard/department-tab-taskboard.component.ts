import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { MatDialog, MatDialogRef, MatSnackBar } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { RxStompService } from '@stomp/ng2-stompjs';
import { BehaviorSubject, forkJoin, Observable, Subscription } from 'rxjs';
import { filter, map, take, tap } from 'rxjs/operators';
import { TaskBoardViewEventService } from 'src/app/okrview/taskboard-services/task-board-view-event.service';
import { ConfirmationDialogData, ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { TaskDto } from 'src/app/shared/model/api/task.dto';
import { ViewTaskBoardEvent } from 'src/app/shared/model/events/view-taskboard-event';
import { OkrUnitId } from 'src/app/shared/model/id-types';
import { ContextRole } from 'src/app/shared/model/ui/context-role';
import { CycleUnit } from 'src/app/shared/model/ui/cycle-unit';
import { OkrChildUnit } from 'src/app/shared/model/ui/OrganizationalUnit/okr-child-unit';
import { ViewTask } from 'src/app/shared/model/ui/taskboard/view-task';
import { ViewTaskState } from 'src/app/shared/model/ui/taskboard/view-task-state';
import { TaskBoardGeneralHelper } from 'src/app/shared/services/helper/task-board/task-board-general-helper';
import { KeyResultMapper } from 'src/app/shared/services/mapper/key-result.mapper';
import { TaskStateMapper } from 'src/app/shared/services/mapper/task-state.mapper';
import { TaskMapperService } from 'src/app/shared/services/mapper/task.mapper';
import { TaskFormComponent, TaskFormData } from '../department-tab-task-form/department-tab-task-form.component';
import { RxStompState } from '@stomp/rx-stomp';

@Component({
  selector: 'app-department-tab-taskboard',
  templateUrl: './department-tab-taskboard.component.html',
  styleUrls: ['./department-tab-taskboard.component.css']
})
export class DepartmentTabTaskboardComponent implements OnDestroy, OnChanges, OnInit {
  @Input() childUnit: OkrChildUnit;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  showSwimlanes: boolean = false;
  isInteractive: boolean;
  viewDataEmitter$: BehaviorSubject<ViewTaskBoardEvent> = new BehaviorSubject(null);

  viewData: ViewTaskBoardEvent = new ViewTaskBoardEvent();
  subscriptions: Subscription[] = [];
  websocketErrorsOccured: boolean = false;

  private snackbarDuration: number = 2000;
  private websocketNotConnectedMessage: string = this.i18n({
    id: 'websocketNotConnectedMessage',
    description: 'Websocket connection not established',
    value: 'Websocketverbindung konnte nicht erstellt werden. Änderungen werden momentan nicht übernommen.'
  });

  constructor(
    private taskMapperService: TaskMapperService,
    private taskStateMapper: TaskStateMapper,
    private taskHelper: TaskBoardGeneralHelper,
    private matDialog: MatDialog,
    private taskBoardEventService: TaskBoardViewEventService,
    private keyResultMapper: KeyResultMapper,
    private stompService: RxStompService,
    private snackBar: MatSnackBar,
    private i18n: I18n) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.stompService.webSocketErrors$.subscribe(tmp => {
        this.websocketErrorsOccured = true;
        this.isInteractive = false;
        this.snackBar.open(this.websocketNotConnectedMessage, null, {
          verticalPosition: 'top',
          duration: 2000
        });
      }),
    );
  }

  ngOnDestroy(): void {
    this.clearSubscriptions();
    this.stompService.deactivate();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.stompService.active) {
      this.stompService.activate();
    }

    let isConnected: boolean = this.isWebsocketConnected();
    if (isConnected && this.websocketErrorsOccured) {
      this.websocketErrorsOccured = false;
      this.isInteractive = true;
    }
    isConnected = this.isWebsocketConnected();
    if (!this.websocketErrorsOccured) {
      if (changes.cycle) {
        this.cycle = changes.cycle.currentValue;
        this.isInteractive = this.isTaskBoardInteractive();
        this.viewDataEmitter$.next(this.viewData);
      }

      if (changes.childUnit) {
        this.clearSubscriptions();
        this.updateEventHandler();
        this.viewData = null;
        this.viewDataEmitter$.next(this.viewData);
        this.loadViewData(this.childUnit.id)
          .pipe(
            map(viewData => {
              viewData.tasks = this.taskHelper.orderTaskList(viewData.tasks);

              return viewData;
            })
          )
          .subscribe(viewData => {
            this.viewData = viewData;
            this.updateWebsocketConnections();
            this.viewDataEmitter$.next(this.viewData);
          });
      }
    }

  }

  private isWebsocketConnected(): boolean {
    return this.stompService.connected();
  }

  private isTaskBoardInteractive(): boolean {
    return this.cycle.isCycleActive() || this.cycle.isCycleInPreparation();
  }

  updateEventHandler(): void {
    this.subscriptions.push(
      this.taskBoardEventService.taskAddButtonClick$.subscribe(task => this.onTaskAddButtonClick(task)),
      this.taskBoardEventService.taskDeleteButtonClick$.subscribe(task => this.onTaskDeleteButtonClick(task)),
      this.taskBoardEventService.taskUpdateButtonClick$.subscribe(task => this.onTaskUpdateButtonClick(task)),
      this.taskBoardEventService.taskMovedInView$.subscribe(dropEvent => this.onTaskDragAndDrop(dropEvent))
    );
  }

  loadViewData(childUnitId: OkrUnitId): Observable<ViewTaskBoardEvent> {
    return forkJoin({
      tasks$: this.taskMapperService.getTasksForOkrUnit$(childUnitId),
      states$: this.taskStateMapper.getTaskStates$(childUnitId),
      keyResults$: this.keyResultMapper.getKeyResultsForOkrUnit(childUnitId)
    })
      .pipe(
        map(result => {
          const viewData: ViewTaskBoardEvent = new ViewTaskBoardEvent();
          viewData.tasks.push(...result["tasks$"]);
          viewData.taskStates.push(...result["states$"]);
          viewData.keyResults.push(...result['keyResults$']);

          return viewData;
        })
      );
  }

  updateWebsocketConnections(): void {
    this.subscriptions.push(
      this.stompService.watch({ destination: `/topic/unit/${this.childUnit.id}/tasks` })
        .pipe(
          map(taskDtosMessage => {
            const newAndUpdatedTasksDtos: TaskDto[] = JSON.parse(taskDtosMessage.body);
            const newAndUpdatedTasks: ViewTask[] = newAndUpdatedTasksDtos.map(this.taskMapperService.mapToViewTask);

            return newAndUpdatedTasks;
          })
        )
        .subscribe(newAndUpdatedTasks => {
          if (newAndUpdatedTasks && newAndUpdatedTasks[0]) {
            this.taskHelper.mergeTaskListWithNewOrUpdatedElements(this.viewData.tasks, newAndUpdatedTasks);
            this.viewData.tasks = this.taskHelper.orderTaskList(this.viewData.tasks);
            this.viewDataEmitter$.next(this.viewData);
          }
        }),
      this.stompService.watch({ destination: `/topic/unit/${this.childUnit.id}/tasks/deleted` })
        .pipe(
          map(taskDtosMessage => {
            const deletedTasksDto: TaskDto = JSON.parse(taskDtosMessage.body);
            const deletedTask: ViewTask = this.taskMapperService.mapToViewTask(deletedTasksDto);

            return deletedTask;
          })
        )
        .subscribe(task => {
          this.viewData.tasks = this.taskHelper.removeTaskAndUpdateTaskList(this.viewData.tasks, task);
          this.viewDataEmitter$.next(this.viewData);
        })
    );
  }

  clearSubscriptions(): void {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
    this.subscriptions = [];
  }

  viewToggleChanged(): void {
    this.showSwimlanes = !this.showSwimlanes;
  }

  onTaskUpdateButtonClick(task: ViewTask): void {
    const states: ViewTaskState[] = this.viewData.taskStates;
    const formData: TaskFormData = {
      unitId: this.childUnit.id,
      defaultState: states.find(state => state.id === task.taskStateId),
      states,
      task,
      keyResults: this.viewData.keyResults,
      isInteractive: this.isInteractive
    };

    const updatedTask$: Observable<ViewTask> = this.openDialog$(formData);
    this.subscriptions.push(
      updatedTask$.pipe(
        tap(task => {
          this.stompService.publish({ destination: `/ws/unit/${this.childUnit.id}/tasks/update`, body: JSON.stringify(task) });
        })
      ).subscribe()
    );
  }

  onTaskAddButtonClick(taskState: ViewTaskState): void {
    let state: ViewTaskState;
    if (!taskState) {
      state = this.viewData.taskStates[0];
    } else {
      state = taskState;
    }
    const states: ViewTaskState[] = this.viewData.taskStates;
    const formData: TaskFormData = {
      unitId: this.childUnit.id,
      defaultState: state, states,
      keyResults: this.viewData.keyResults,
      isInteractive: this.isInteractive
    };

    const newTask$: Observable<ViewTask> = this.openDialog$(formData);
    this.subscriptions.push(
      newTask$.pipe(
        tap(task => {
          this.stompService.publish({ destination: `/ws/unit/${this.childUnit.id}/tasks/add`, body: JSON.stringify(task) });
        }),
      )
        .subscribe()
    );
  }

  onTaskDeleteButtonClick(task: ViewTask): void {
    console.log("onTaskDelete");
    const title: string = this.i18n({
      id: 'deleteTaskDialog_title',
      value: 'Aufgabe löschen'
    });
    const message: string =
      this.i18n({
        id: 'deleteTaskDialog_message',
        value: 'Soll die Aufgabe "{{title}}" wirklich gelöscht werden?'
      }, { title: task.title });
    const confirmButtonText: string = this.i18n({
      id: 'deleteButtonText',
      description: 'deleteButtonText',
      value: 'Löschen'
    });

    const formData: ConfirmationDialogData = {
      title, message, confirmButtonText
    };

    this.subscriptions.push(
      this.openConfirmationDialog$(formData)
        .pipe(
          tap(_ => {
            this.stompService.publish({ destination: `/ws/unit/${this.childUnit.id}/tasks/delete`, body: JSON.stringify(task) });
          }),
        )
        .subscribe()
    );
  }


  moveTaskInList(currentList: ViewTask[], movedAndUpdatedTask: ViewTask): ViewTask[] {
    let copiedTasks: ViewTask[] = this.taskHelper.copyTaskList(currentList);
    copiedTasks = this.taskHelper.removeTaskAndUpdateTaskList(copiedTasks, movedAndUpdatedTask);
    copiedTasks = this.taskHelper.addTaskAndUpdateTaskList(copiedTasks, movedAndUpdatedTask);

    return copiedTasks;
  }

  onTaskDragAndDrop(movedAndUpdatedTask: ViewTask): void {
    this.viewData.tasks = this.moveTaskInList(this.viewData.tasks, movedAndUpdatedTask);

    this.viewDataEmitter$.next(this.viewData);

    const updatedTaskDto = this.taskMapperService.mapToTaskDTO(movedAndUpdatedTask);

    this.stompService.publish({
      destination: `/ws/unit/${this.childUnit.id}/tasks/update`,
      body: JSON.stringify(updatedTaskDto)
    });
  }

  private openDialog$(formData: TaskFormData): Observable<ViewTask> {
    const dialogReference: MatDialogRef<TaskFormComponent, ViewTask> = this.matDialog.open(TaskFormComponent, { data: formData });

    return dialogReference
      .afterClosed()
      .pipe(
        filter(v => v ? true : false)
      );
  }

  private openConfirmationDialog$(formData: ConfirmationDialogData): Observable<boolean> {
    const dialogReference: MatDialogRef<ConfirmationDialogComponent, boolean> = this.matDialog
      .open(ConfirmationDialogComponent, { data: formData });

    return dialogReference
      .afterClosed()
      .pipe(
        take(1),
        filter(value => value)
      );
  }
}
