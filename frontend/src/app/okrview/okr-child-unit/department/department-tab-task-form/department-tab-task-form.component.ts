import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NEVER, Observable, of, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ViewTaskState } from '../../../../shared/model/ui/taskboard/view-task-state';
import { ViewTask } from '../../../../shared/model/ui/taskboard/view-task';
import { ViewKeyResult } from '../../../../shared/model/ui/view-key-result';
import { User } from '../../../../shared/model/api/user';
import { ViewObjective } from '../../../../shared/model/ui/view-objective';
import { KeyResultMap } from '../../../../shared/model/ui/key-result-map';
import { ObjectiveViewMapper } from '../../../../shared/services/mapper/objective-view.mapper';
import { UserService } from '../../../../shared/services/helper/user.service';
import { TranslateService } from '@ngx-translate/core';
import { ApiHttpService } from '../../../../core/services/api-http.service';
import { RevisionMapperService } from '../../../../shared/services/mapper/revision.mapper.service';
import { RevisionDto } from '../../../../shared/model/api/revision-dto';

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
  styleUrls: ['./department-tab-task-form.component.scss'],
})
export class TaskFormComponent implements OnInit, OnDestroy {
  taskForm: FormGroup;
  user: User;
  users$: Observable<User[]>;
  objectives: ViewObjective[];
  keyResultMaps$: Observable<KeyResultMap[]>;
  states: ViewTaskState[];
  subscriptions: Subscription[] = [];
  title: string;
  isInteractive: boolean;
  taskRevisions$: Observable<RevisionDto[]>;
  currentLanguage: string;

  constructor(
    private objectiveMapper: ObjectiveViewMapper,
    private dialogRef: MatDialogRef<TaskFormComponent>,
    private userService: UserService,
    private translate: TranslateService,
    private revisionMapper: RevisionMapperService,
    private api: ApiHttpService,
    private translateService: TranslateService,
    @Inject(MAT_DIALOG_DATA) private formData: (TaskFormData | any),
  ) {
  }

  ngOnInit(): void {
    this.isInteractive = this.formData.isInteractive;
    this.currentLanguage = this.translateService.currentLang;
    this.taskForm = new FormGroup({
      title: new FormControl({
        value: '',
        disabled: !this.isInteractive,
      }, [Validators.required, Validators.maxLength(255)]),
      description: new FormControl({ value: '', disabled: !this.isInteractive }, [Validators.maxLength(2000)]),
      assignedUserIds: new FormControl({ value: null, disabled: !this.isInteractive }),
      assignedKeyResultId: new FormControl({ value: null, disabled: !this.isInteractive }),
      taskStateId: new FormControl({
        value: this.formData.defaultState.id,
        disabled: !this.isInteractive,
      }, [Validators.required]),
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
    this.taskRevisions$ = this.formData.task?.id ? this.revisionMapper.getRevisionsForTask$(this.formData.task.id) : of(
      []);
    this.keyResultMaps$ = this.objectiveMapper.getObjectivesForUnit$(this.formData.unitId)
      .pipe(
        switchMap(objectives => {
          const keyResultMap: KeyResultMap[] = [];
          for (const objective of objectives) {
            keyResultMap.push({
              objective,
              keyResults: this.formData.keyResults.filter(keyResult => keyResult.parentObjectiveId === objective.id),
            });
          }

          return of(keyResultMap);
        }),
      );
    const editText: string = this.translate.instant('department-tab-task-form.dialog.edit');
    const createText: string = this.translate.instant('department-tab-task-form.dialog.create');
    this.title = this.translate.instant(
      'department-tab-task-form.dialog.title',
      { editOrCreateText: this.formData.task ? editText : createText },
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub: Subscription) => sub.unsubscribe());
    this.subscriptions = [];
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  onSelectUser($event: { value: User }): void {
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
        null,
      );

      this.dialogRef.close(newTask);
    }
  }

}
