import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
// import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { MAT_DIALOG_DATA,  MatDialogRef } from '@angular/material/dialog';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { NEVER, Observable, of, Subject, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ViewTaskState } from '../../../../shared/model/ui/taskboard/view-task-state';
import { ViewTask } from '../../../../shared/model/ui/taskboard/view-task';
import { ViewKeyResult } from '../../../../shared/model/ui/view-key-result';
import { User } from '../../../../shared/model/api/user';
import { ViewObjective } from '../../../../shared/model/ui/view-objective';
import { KeyResultMap } from '../../../../shared/model/ui/key-result-map';
import { TaskMapperService } from '../../../../shared/services/mapper/task.mapper';
import { ObjectiveViewMapper } from '../../../../shared/services/mapper/objective-view.mapper';
import { KeyResultMapper } from '../../../../shared/services/mapper/key-result.mapper';
import { CurrentOkrviewService } from '../../../current-okrview.service';
import { CurrentOkrUnitSchemaService } from '../../../current-okr-unit-schema.service';
import { UserService } from '../../../../shared/services/helper/user.service';

export interface TaskFormData {
  task?: ViewTask;
  unitId: number;
  defaultState: ViewTaskState;
  states: ViewTaskState[];
  keyResults: ViewKeyResult[];
  isInteractive: boolean;
}

@Component({
  selector: 'app-department-tab-task-form',
  templateUrl: './department-tab-task-form.component.html',
  styleUrls: ['./department-tab-task-form.component.css']
})
export class TaskFormComponent implements OnInit, OnDestroy {
  taskForm: FormGroup;
  users: User[];
  user: User;
  users$: Observable<User[]>;
  objectives: ViewObjective[];
  keyResultMaps$: Observable<KeyResultMap[]>;
  states: ViewTaskState[];
  subscriptions: Subscription[] = [];
  title: string;
  isInteractive: boolean;

  constructor(
    private objectiveMapper: ObjectiveViewMapper,
    private dialogRef: MatDialogRef<TaskFormComponent>,
    private userService: UserService,
    private i18n: I18n,
    @Inject(MAT_DIALOG_DATA) private formData: (TaskFormData | any)
  ) { }

  ngOnInit(): void {
    this.isInteractive = this.formData.isInteractive;
    this.taskForm = new FormGroup({
      title: new FormControl({ value: '', disabled: !this.isInteractive }, [Validators.required, Validators.maxLength(255)]),
      description: new FormControl({ value: '', disabled: !this.isInteractive }, [Validators.maxLength(255)]),
      assignedUserIds: new FormControl({ value: null, disabled: !this.isInteractive }),
      assignedKeyResultId: new FormControl({ value: null, disabled: !this.isInteractive }),
      taskStateId: new FormControl({ value: this.formData.defaultState.id, disabled: !this.isInteractive }, [Validators.required]),
    });

    this.states = this.formData.states;

    if (this.formData.task) {
      this.taskForm.setValue({
        title: this.formData.task.title,
        description: this.formData.task.description,
        assignedUserIds: this.formData.task.assignedUserIds,
        assignedKeyResultId: this.formData.task.assignedKeyResultId,
        taskStateId: this.formData.task.taskStateId,
      });
    }

    this.users$ = this.userService.getAllUsers$();

    this.keyResultMaps$ = this.objectiveMapper.getObjectivesForUnit$(this.formData.unitId)
      .pipe(
        switchMap(objectives => {
          const keyResultMap: KeyResultMap[] = [];
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
      id: 'small_edit',
      value: 'bearbeiten'
    });

    const createText: string = this.i18n({
      id: 'small_create',
      value: 'erstellen'
    });

    this.title = this.i18n({
      id: 'task_form_title',
      value: 'Aufgabe {{editOrCreateText}}'
    }, {editOrCreateText: this.formData.task ? editText : createText});
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
      task.title = this.taskForm.get('title').value;
      task.description = this.taskForm.get('description').value;
      task.assignedUserIds = this.taskForm.get('assignedUserIds').value;
      task.assignedKeyResultId = this.taskForm.get('assignedKeyResultId').value;
      task.taskStateId = this.taskForm.get('taskStateId').value;
      task.parentTaskBoardId = this.formData.unitId;

      this.dialogRef.close(task);
    } else {
      const formData: ViewTask = this.taskForm.getRawValue();

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

      this.dialogRef.close(newTask);
    }
  }

}
