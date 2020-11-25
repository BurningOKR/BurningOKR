import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { forkJoin, Observable, Subscription } from 'rxjs';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { CycleAdminCardComponent } from 'src/app/cycle-admin/cycle-admin-card/cycle-admin-card.component';

import { ContextRole } from 'src/app/shared/model/ui/context-role';
import { CycleUnit } from 'src/app/shared/model/ui/cycle-unit';
import { OkrChildUnit } from 'src/app/shared/model/ui/OrganizationalUnit/okr-child-unit';
import { StateTaskMap } from 'src/app/shared/model/ui/state-task-map';
import { ViewTask } from 'src/app/shared/model/ui/view-task';
import { ViewTaskState } from 'src/app/shared/model/ui/view-task-state';
import { TaskService } from 'src/app/shared/services/helper/task.service';
import { TaskStateMapperService } from 'src/app/shared/services/mapper/task-state.mapper';
import { TaskMapperService } from 'src/app/shared/services/mapper/task.mapper';
import { TaskFormComponent, TaskFormData } from '../department-tab-task-form/department-tab-task-form.component';



@Component({
  selector: 'app-department-tab-taskboard',
  templateUrl: './department-tab-taskboard.component.html',
  styleUrls: ['./department-tab-taskboard.component.css']
})
export class DepartmentTabTaskboardComponent implements OnInit {
  @Input() childUnit: OkrChildUnit;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  stateTaskMap: StateTaskMap[] = [];

  subscriptions: Subscription[];

  constructor(
    private taskMapperService: TaskMapperService,
    private taskStateMapperService: TaskStateMapperService,
    private taskHelper: TaskService,
    private matDialog: MatDialog) { }

  ngOnInit(): void {
    this.subscriptions = [];
    this.subscriptions.push(
      forkJoin({
        tasks$: this.taskMapperService.getTasksForOkrUnit$(this.childUnit.id),
        states$: this.taskStateMapperService.getTaskStates$(this.childUnit.id)
      })
        .subscribe(result => this.initialise(result["states$"], result["tasks$"]))
    );
  }

  initialise(states: ViewTaskState[], tasks: ViewTask[]): void {
    this.stateTaskMap.push(...this.taskHelper.generateStateTaskMapList(states, tasks));
  }

  onTaskUpdate(task: ViewTask): void {
    const oldStateId: number = task.stateId;
    const states: ViewTaskState[] = this.taskHelper.extractStates(this.stateTaskMap);
    const formData: TaskFormData = {
      unitId: this.childUnit.id, defaultState: states.find(state => state.id === task.stateId), states, task
    };

    const updatedTask$: Observable<ViewTask> = this.openDialog$(formData);
    this.subscriptions.push(
      updatedTask$.pipe(
        switchMap(updatedTask => this.taskMapperService.updateTask$(this.childUnit.id, updatedTask)),
      )
        .subscribe(referencedTask => {
          if (oldStateId !== referencedTask.stateId) {
            this.removeTaskFromList(referencedTask, oldStateId);
          }
          this.addOrUpdateTaskInList(referencedTask);
        })
    );

  }

  onTaskAdd(taskState: ViewTaskState): void {
    const states: ViewTaskState[] = this.taskHelper.extractStates(this.stateTaskMap);
    const formData: TaskFormData = {
      unitId: this.childUnit.id, defaultState: taskState, states
    };

    const newTask$: Observable<ViewTask> = this.openDialog$(formData);
    this.subscriptions.push(
      newTask$.pipe(
        switchMap(task => {
          return this.taskMapperService.createTaskForOkrUnit$(this.childUnit.id, task)
        }
        ),
      )
        .subscribe(task => { this.addOrUpdateTaskInList(task); })
    );
  }

  private openDialog$(formData: TaskFormData): Observable<ViewTask> {
    const dialogReference: MatDialogRef<TaskFormComponent, ViewTask> = this.matDialog.open(TaskFormComponent, { data: formData });

    return dialogReference
      .afterClosed()
      .pipe(
        filter(v => v ? true : false)
      );
  }

  onTaskDelete(task: ViewTask): void {
    console.log("removeTaskFromList");
    this.subscriptions.push(
      this.taskMapperService.deleteTask$(this.childUnit.id, task.id)
        .subscribe(_ => this.removeTaskFromList(task, task.stateId))
    );
  }

  onTaskMoved($event: ViewTask) {
    this.subscriptions.push(
      this.taskMapperService.updateTask$(this.childUnit.id, $event)
        .subscribe(task => {
          console.log("updated Task in Database");
          console.log(task);
          this.addOrUpdateTaskInList(task);
        })
    );
  }

  removeTaskFromList(task: ViewTask, oldStateId: number): void {
    console.log("removeTaskFromList");
    const index: number = this.taskHelper.getListIndex(oldStateId, this.stateTaskMap);
    if (index >= 0) {
      const taskIndex: number = this.stateTaskMap[index].tasks.findIndex(listTask => listTask.id === task.id);
      this.stateTaskMap[index].tasks.splice(taskIndex, 1);
    }
  }

  addOrUpdateTaskInList(task: ViewTask): void {
    console.log("addOrUpdateTaskInList");
    const mapIndex: number = this.taskHelper.getListIndex(task.stateId, this.stateTaskMap);
    if (mapIndex >= 0) {
      if (this.taskHelper.isTaskInList(task, this.stateTaskMap[mapIndex].tasks)) {
        const taskIndex: number = this.stateTaskMap[mapIndex].tasks.findIndex(listTask => listTask.id === task.id);
        this.stateTaskMap[mapIndex].tasks.splice(taskIndex, 1, task);
      } else {
        this.stateTaskMap[mapIndex].tasks.push(task);
      }
    }
  }
}
