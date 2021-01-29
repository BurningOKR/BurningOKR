import { state } from '@angular/animations';
import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { NEVER, Observable, of, Subject, Subscription } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { CurrentOkrUnitSchemaService } from 'src/app/okrview/current-okr-unit-schema.service';
import { CurrentOkrviewService } from 'src/app/okrview/current-okrview.service';
import { User } from 'src/app/shared/model/api/user';
import { KeyResultMap } from 'src/app/shared/model/ui/key-result-map';
import { ViewTask } from 'src/app/shared/model/ui/taskboard/view-task';
import { ViewTaskState } from 'src/app/shared/model/ui/taskboard/view-task-state';
import { ViewKeyResult } from 'src/app/shared/model/ui/view-key-result';
import { ViewObjective } from 'src/app/shared/model/ui/view-objective';

import { UserService } from 'src/app/shared/services/helper/user.service';
import { KeyResultMapper } from 'src/app/shared/services/mapper/key-result.mapper';
import { ObjectiveViewMapper } from 'src/app/shared/services/mapper/objective-view.mapper';
import { TaskMapperService } from 'src/app/shared/services/mapper/task.mapper';

export interface TaskFormData {
  task?: ViewTask;
  unitId: number;
  defaultState: ViewTaskState;
  states: ViewTaskState[];
  keyResults: ViewKeyResult[];
}

@Component({
  selector: 'app-department-tab-task-form',
  templateUrl: './department-tab-task-form.component.html',
  styleUrls: ['./department-tab-task-form.component.css']
})
export class TaskFormComponent implements OnInit, OnDestroy {
  taskForm: FormGroup;
  parentElements$ = new Subject<TaskFormData[]>();
  users: User[];
  user: User;
  users$: Observable<User[]>;
  objectives: ViewObjective[];
  keyResultMaps$: Observable<KeyResultMap[]>;
  states: ViewTaskState[];
  subscriptions: Subscription[] = [];
  title: string;

  constructor(
    private dialogRef: MatDialogRef<TaskFormComponent>,
    private taskMapper: TaskMapperService,
    private objectiveMapper: ObjectiveViewMapper,
    private keyResultMapper: KeyResultMapper,
    private currentOkrViewService: CurrentOkrviewService,
    private currentOkrUnitSchemaService: CurrentOkrUnitSchemaService,
    private userService: UserService,
    private i18n: I18n,
    @Inject(MAT_DIALOG_DATA) private formData: TaskFormData
  ) { }

  ngOnInit(): void {
    this.taskForm = new FormGroup({
      title: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      description: new FormControl('', [Validators.maxLength(255)]),
      assignedUserIds: new FormControl(),
      assignedKeyResultId: new FormControl(),
      taskStateId: new FormControl(this.formData.defaultState.id, [Validators.required]),
    });

    this.states = this.formData.states;

    if (this.formData.task) {
      this.taskForm.setValue({
        title: this.formData.task.title,
        description: this.formData.task.description,
        assignedUserIds: this.formData.task.assignedUserIds,
        assignedKeyResultId: this.formData.task.assignedKeyResultId,
        taskStateId: this.formData.task.taskStateId
      });
    }

    this.users$ = this.userService.getAllUsers$();
    
    this.keyResultMaps$ = this.objectiveMapper.getObjectivesForUnit$(this.formData.unitId)
      .pipe(
        switchMap(objectives => {
          let keyResultMap: KeyResultMap[] = [];
          for (const objective of objectives) {
            keyResultMap.push({
              objective,
              keyResults: this.formData.keyResults.filter(keyResult => keyResult.parentObjectiveId === objective.id)
            });
          }

          return of(keyResultMap);
        })
      );
    const editText: string = this.i18n({
      id: 'edit',
      value: 'bearbeiten'
    });

    const createText: string = this.i18n({
      id: 'create',
      value: 'erstellen'
    });

    this.title = this.i18n({
      id: 'task_form_title',
      value: 'Aufgabe {{editOrCreateText}}'
    }, { editOrCreateText: this.formData.task ? editText : createText });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub: Subscription) => sub.unsubscribe());
    this.subscriptions = [];
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  onSelectUser($event: { value: User; }): void {
    this.user = $event.value;
  }

  saveObjective(): void {
    const task: ViewTask = this.formData.task;

    if (task) {
      console.log(task);
      task.title = this.taskForm.get('title').value;
      task.description = this.taskForm.get('description').value;
      task.assignedUserIds = this.taskForm.get('assignedUserIds').value;
      task.assignedKeyResultId = this.taskForm.get('assignedKeyResultId').value;
      task.taskStateId = this.taskForm.get('taskStateId').value;
      task.parentTaskBoardId = this.formData.unitId;

      this.dialogRef.close(task);
    } else {
      const formData: ViewTask = this.taskForm.getRawValue();
      console.log("Raw Value: ");
      console.log(this.taskForm.getRawValue());
      const newTask: ViewTask = new ViewTask(
        undefined,
        formData.title,
        formData.description,
        formData.assignedUserIds,
        formData.assignedKeyResultId,
        this.formData.unitId,
        formData.taskStateId,
        null,
        null
      );
      console.log(newTask);

      this.dialogRef.close(newTask);
    }
  }

}
