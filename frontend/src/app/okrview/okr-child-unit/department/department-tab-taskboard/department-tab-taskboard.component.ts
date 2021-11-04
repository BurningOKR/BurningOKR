import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { RxStompService } from '@stomp/ng2-stompjs';
import { BehaviorSubject, forkJoin, Observable, Subscription } from 'rxjs';
import { filter, map, take, tap } from 'rxjs/operators';

import { TaskFormComponent, TaskFormData } from '../department-tab-task-form/department-tab-task-form.component';
import { OkrChildUnit } from '../../../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { ContextRole } from '../../../../shared/model/ui/context-role';
import { CycleUnit } from '../../../../shared/model/ui/cycle-unit';
import { ViewTaskBoardEvent } from '../../../../shared/model/events/view-taskboard-event';
import { TaskMapperService } from '../../../../shared/services/mapper/task.mapper';
import { TaskStateMapper } from '../../../../shared/services/mapper/task-state.mapper';
import { TaskBoardGeneralHelper } from '../../../../shared/services/helper/task-board/task-board-general-helper';
import { TaskBoardViewEventService } from '../../../taskboard-services/task-board-view-event.service';
import { KeyResultMapper } from '../../../../shared/services/mapper/key-result.mapper';
import { OkrUnitId } from '../../../../shared/model/id-types';
import { TaskDto } from '../../../../shared/model/api/task.dto';
import { ViewTask } from '../../../../shared/model/ui/taskboard/view-task';
import { ViewTaskState } from '../../../../shared/model/ui/taskboard/view-task-state';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../../../shared/components/confirmation-dialog/confirmation-dialog.component';
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

  eventSubscriptions: Subscription[] = [];
  websocketSubcriptions: Subscription[] = [];
  tryingToReconnect: boolean = false;

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
    this.eventSubscriptions.push(
      this.stompService.webSocketErrors$.subscribe(tmp => {
        if (!this.tryingToReconnect) {
          this.tryingToReconnect = true;
          this.isInteractive = false;
          this.snackBar.open(this.websocketNotConnectedMessage, null, {
            verticalPosition: 'top',
            duration: this.snackbarDuration
          });
          this.clearWebsocketConnectionSubscriptions();
        }
      }),
      this.stompService.connectionState$.subscribe(observer => {
        if (observer === RxStompState.OPEN && this.tryingToReconnect) {
          this.tryingToReconnect = false;
          this.isInteractive = true;
          this.updateWebsocketConnections();
        }
      }),
    );
  }

  ngOnDestroy(): void {
    this.clearEventSubscriptions();
    this.clearWebsocketConnectionSubscriptions();
    this.stompService.deactivate();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.stompService.active && !this.tryingToReconnect) {
      this.stompService.activate();
    }

    const isConnected: boolean = this.isWebsocketConnected();
    if (isConnected && this.tryingToReconnect) {
      this.tryingToReconnect = false;
      this.isInteractive = true;
    }

    if (!this.tryingToReconnect) {
      if (changes.cycle) {
        this.cycle = changes.cycle.currentValue;
        this.isInteractive = this.isTaskBoardInteractive();
        this.viewDataEmitter$.next(this.viewData);
      }

      if (changes.childUnit) {
        this.clearEventSubscriptions();
        this.clearWebsocketConnectionSubscriptions();
        this.updateEventHandler();
        this.viewData = null;
        this.viewDataEmitter$.next(this.viewData);
        this.loadViewData$(this.childUnit.id)
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

  updateEventHandler(): void {
    this.eventSubscriptions.push(
      this.taskBoardEventService.taskAddButtonClick$.subscribe(task => this.onTaskAddButtonClick(task)),
      this.taskBoardEventService.taskDeleteButtonClick$.subscribe(task => this.onTaskDeleteButtonClick(task)),
      this.taskBoardEventService.taskUpdateButtonClick$.subscribe(task => this.onTaskUpdateButtonClick(task)),
      this.taskBoardEventService.taskMovedInView$.subscribe(dropEvent => this.onTaskDragAndDrop(dropEvent))
    );
  }

  loadViewData$(childUnitId: OkrUnitId): Observable<ViewTaskBoardEvent> {
    return forkJoin({
      tasks$: this.taskMapperService.getTasksForOkrUnit$(childUnitId),
      states$: this.taskStateMapper.getTaskStates$(childUnitId),
      keyResults$: this.keyResultMapper.getKeyResultsForOkrUnit$(childUnitId)
    })
      .pipe(
        map(result => {
          const viewData: ViewTaskBoardEvent = new ViewTaskBoardEvent();
          viewData.tasks.push(...result.tasks$);
          viewData.taskStates.push(...result.states$);
          viewData.keyResults.push(...result.keyResults$);

          return viewData;
        })
      );
  }

  updateWebsocketConnections(): void {
    this.websocketSubcriptions.push(
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

  clearEventSubscriptions(): void {
    for (const subscription of this.eventSubscriptions) {
      subscription.unsubscribe();
    }
    this.eventSubscriptions = [];
  }

  clearWebsocketConnectionSubscriptions(): void {
    for (const subscription of this.websocketSubcriptions) {
      subscription.unsubscribe();
    }
    this.websocketSubcriptions = [];
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
    this.eventSubscriptions.push(
      updatedTask$.pipe(
        tap((updatedTask: ViewTask) => {
          this.stompService.publish({ destination: `/ws/unit/${this.childUnit.id}/tasks/update`, body: JSON.stringify(updatedTask) });
        })
      )
        .subscribe()
    );
  }

  onTaskDeleteButtonClick(task: ViewTask): void {
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
      id: 'capitalised_delete',
      description: 'deleteButtonText',
      value: 'Löschen'
    });

    const formData: ConfirmationDialogData = {
      title, message, confirmButtonText
    };

    this.eventSubscriptions.push(
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
    this.eventSubscriptions.push(
      newTask$.pipe(
        tap(task => {
          this.stompService.publish({ destination: `/ws/unit/${this.childUnit.id}/tasks/add`, body: JSON.stringify(task) });
        }),
      )
        .subscribe()
    );
  }

  onTaskDragAndDrop(movedAndUpdatedTask: ViewTask): void {
    this.viewData.tasks = this.moveTaskInList(this.viewData.tasks, movedAndUpdatedTask);

    this.viewDataEmitter$.next(this.viewData);

    const updatedTaskDto: TaskDto = this.taskMapperService.mapToTaskDTO(movedAndUpdatedTask);

    this.stompService.publish({
      destination: `/ws/unit/${this.childUnit.id}/tasks/update`,
      body: JSON.stringify(updatedTaskDto)
    });
  }

  private isWebsocketConnected(): boolean {
    return this.stompService.connected();
  }

  private isTaskBoardInteractive(): boolean {
    return this.cycle.isCycleActive() || this.cycle.isCycleInPreparation();
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
