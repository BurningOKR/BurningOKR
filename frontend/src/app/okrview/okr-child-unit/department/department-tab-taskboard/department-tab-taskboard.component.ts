import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { RxStompService } from '@stomp/ng2-stompjs';
import { BehaviorSubject, forkJoin, Observable, Subscription } from 'rxjs';
import { filter, map, take, tap } from 'rxjs/operators';
import { TaskBoardViewEventService } from 'src/app/okrview/taskboard-services/task-board-view-event.service';
import { ConfirmationDialogData, ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';
import { TaskDto } from 'src/app/shared/model/api/task.dto';
import { ViewTaskBoardEvent } from 'src/app/shared/model/events/view-taskboard-event';
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
import { TaskBoardDragDropEvent } from './taskboard-column/taskboard-column.component';

@Component({
  selector: 'app-department-tab-taskboard',
  templateUrl: './department-tab-taskboard.component.html',
  styleUrls: ['./department-tab-taskboard.component.css']
})
export class DepartmentTabTaskboardComponent implements OnInit, OnDestroy {
  @Input() childUnit: OkrChildUnit;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  showSwimlanes: boolean = false;
  viewDataEmitter$: BehaviorSubject<ViewTaskBoardEvent> = new BehaviorSubject(null);

  viewData: ViewTaskBoardEvent = new ViewTaskBoardEvent();
  subscriptions: Subscription[];

  constructor(
    private taskMapperService: TaskMapperService,
    private taskStateMapper: TaskStateMapper,
    private taskHelper: TaskBoardGeneralHelper,
    private matDialog: MatDialog,
    private taskBoardEventService: TaskBoardViewEventService,
    private keyResultMapper: KeyResultMapper,
    private stompService: RxStompService,
    private i18n: I18n) { }

  ngOnInit(): void {
    this.subscriptions = [];
    this.subscriptions.push(
      forkJoin({
        tasks$: this.taskMapperService.getTasksForOkrUnit$(this.childUnit.id),
        states$: this.taskStateMapper.getTaskStates$(this.childUnit.id),
        keyResults$: this.keyResultMapper.getKeyResultsForOkrUnit(this.childUnit.id)
      })
        .subscribe(result => {
          this.viewData.tasks.push(...result["tasks$"]);
          this.viewData.taskStates.push(...result["states$"]);
          this.viewData.keyResults.push(...result['keyResults$']);

          this.viewData.tasks = this.taskHelper.orderTaskList(this.viewData.tasks);
          console.log("after sorting");
          console.log(this.viewData);

          this.viewDataEmitter$.next(this.viewData);
        })
    );

    this.subscriptions.push(
      this.taskBoardEventService.taskAddButtonClick$.subscribe(task => this.onTaskAddButtonClick(task)),
      this.taskBoardEventService.taskDeleteButtonClick$.subscribe(task => this.onTaskDeleteButtonClick(task)),
      this.taskBoardEventService.taskUpdateButtonClick$.subscribe(task => this.onTaskUpdateButtonClick(task)),
      this.taskBoardEventService.taskMovedInView$.subscribe(dropEvent => this.onTaskDragAndDrop(dropEvent)),
      this.stompService.watch({ destination: `/topic/unit/${this.childUnit.id}/tasks` })
        .pipe(
          map(taskDtosMessage => {
            const newAndUpdatedTasksDtos: TaskDto[] = JSON.parse(taskDtosMessage.body);
            const newAndUpdatedTasks: ViewTask[] = newAndUpdatedTasksDtos.map(this.taskMapperService.mapToViewTask);

            return newAndUpdatedTasks;
          })
        )
        .subscribe(newAndUpdatedTasks => {
          console.log("Websocket Topic");
          console.log(newAndUpdatedTasks);
          console.log(this.viewData);

          if (newAndUpdatedTasks && newAndUpdatedTasks[0]) {
            this.taskHelper.mergeTaskListWithNewOrUpdatedElements(this.viewData.tasks, newAndUpdatedTasks);
            this.viewData.tasks= this.taskHelper.orderTaskList(this.viewData.tasks);
            console.log('after merge');
            console.log(this.viewData);
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
          console.log("delete Topic");
          this.viewData.tasks = this.taskHelper.removeTaskAndUpdateTaskList(this.viewData.tasks, task);
          this.viewDataEmitter$.next(this.viewData);
        })
    );

    this.stompService.activate();
    this.stompService.webSocketErrors$.subscribe(error => console.log(error));
  }

  ngOnDestroy(): void {
    console.log("taskboard wird abgebaut");
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
    this.stompService.deactivate();
  }

  viewToggleChanged(): void {
    this.showSwimlanes = !this.showSwimlanes;
  }

  onTaskUpdateButtonClick(task: ViewTask): void {
    const states: ViewTaskState[] = this.viewData.taskStates;
    const formData: TaskFormData = {
      unitId: this.childUnit.id, defaultState: states.find(state => state.id === task.taskStateId), states, task
    };

    const updatedTask$: Observable<ViewTask> = this.openDialog$(formData);
    this.subscriptions.push(
      updatedTask$.pipe(
        tap(task => {
          console.log("updated task before send");
          console.log(task);
          this.stompService.publish({ destination: `/ws/unit/${this.childUnit.id}/tasks/update`, body: JSON.stringify(task) });
        })
      ).subscribe()
    );
  }

  onTaskAddButtonClick(taskState: ViewTaskState): void {
    const states: ViewTaskState[] = this.viewData.taskStates;
    const formData: TaskFormData = {
      unitId: this.childUnit.id, defaultState: taskState, states
    };

    const newTask$: Observable<ViewTask> = this.openDialog$(formData);
    this.subscriptions.push(
      newTask$.pipe(
        tap(task => {
          // task.previousTaskId = this.taskHelper.getTaskIdForNewTask(this.stateTaskMap, task.taskStateId);
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
    console.log("drop Task");
    console.log(movedAndUpdatedTask);

    this.viewData.tasks = this.moveTaskInList(this.viewData.tasks, movedAndUpdatedTask);

    console.log('update global tasklist after moving task');
    console.log(this.viewData.tasks);

    this.viewDataEmitter$.next(this.viewData);


    const updatedTaskDto = this.taskMapperService.mapToTaskDTO(movedAndUpdatedTask);
    console.log("Task moved");
    console.log(updatedTaskDto);


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
