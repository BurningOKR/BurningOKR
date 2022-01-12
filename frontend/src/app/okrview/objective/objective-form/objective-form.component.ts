import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { User } from '../../../shared/model/api/user';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { forkJoin, NEVER, Observable, Subject, Subscription } from 'rxjs';
import { ObjectiveViewMapper } from '../../../shared/services/mapper/objective-view.mapper';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ViewObjective } from '../../../shared/model/ui/view-objective';
import { OkrUnitSchema } from '../../../shared/model/ui/okr-unit-schema';
import { OkrUnit } from '../../../shared/model/ui/OrganizationalUnit/okr-unit';
import { CurrentOkrUnitSchemaService } from '../../current-okr-unit-schema.service';
import { map, switchMap } from 'rxjs/operators';
import { UserService } from '../../../shared/services/helper/user.service';
import { TranslateService } from '@ngx-translate/core';

interface ObjectiveFormData {
  objective?: ViewObjective;
  unitId?: number;
  currentItem: OkrUnit;
}

class DepartmentObjectiveSchema {
  department: OkrUnitSchema;
  objectiveList: ViewObjective[];

  constructor(department: OkrUnitSchema, objectiveList: ViewObjective[]) {
    this.department = department;
    this.objectiveList = objectiveList;
  }
}

@Component({
  selector: 'app-objective-form',
  templateUrl: './objective-form.component.html',
  styleUrls: ['./objective-form.component.scss']
})
export class ObjectiveFormComponent implements OnInit, OnDestroy {
  objective: ViewObjective;
  objectiveForm: FormGroup;
  parentElements$ = new Subject<DepartmentObjectiveSchema[]>();
  users: User[];
  user: User;
  users$: Observable<User[]>;

  subscriptions: Subscription[] = [];
  title: string;

  constructor(
    private dialogRef: MatDialogRef<ObjectiveFormComponent>,
    private objectiveMapper: ObjectiveViewMapper,
    private currentOkrViewService: CurrentOkrviewService,
    private currentOkrUnitSchemaService: CurrentOkrUnitSchemaService,
    private userService: UserService,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) private formData: ObjectiveFormData
  ) {
  }

  ngOnInit(): void {
    this.objectiveForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      parentObjectiveId: new FormControl(undefined),
      description: new FormControl('', [Validators.maxLength(255)]),
      remark: new FormControl('', [Validators.maxLength(255)]),
      contactPersonId: new FormControl(),
      isActive: new FormControl(true)
    });

    if (this.formData.objective) {
      this.objective = this.formData.objective;
      this.objectiveForm.patchValue(this.formData.objective);
      this.fetchParentObjectives(this.objective.parentUnitId);
    } else {
      this.fetchParentObjectives(this.formData.unitId);
    }

    this.users$ = this.userService.getAllUsers$();

    const editText: string = this.translate.instant('objective-form.dialog.edit');
    const createText: string = this.translate.instant('objective-form.dialog.create');
    this.title = this.translate.instant('objective-form.dialog.title',
      {editOrCreateText: this.objective ? editText : createText});
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
    const objective: ViewObjective = this.formData.objective;

    if (objective) {
      objective.name = this.objectiveForm.get('name').value;
      objective.parentObjectiveId = this.objectiveForm.get('parentObjectiveId').value;
      objective.description = this.objectiveForm.get('description').value;
      objective.remark = this.objectiveForm.get('remark').value;
      objective.contactPersonId = this.objectiveForm.get('contactPersonId').value;
      objective.isActive = this.objectiveForm.get('isActive').value;
      this.dialogRef.close(this.objectiveMapper.putObjective$(objective));
    } else {
      const formData: ViewObjective = this.objectiveForm.getRawValue();
      const newObjective: ViewObjective = new ViewObjective(
        undefined,
        formData.name,
        formData.description,
        formData.remark,
        undefined,
        undefined,
        formData.isActive,
        formData.parentObjectiveId,
        formData.parentUnitId,
        formData.contactPersonId,
        undefined,
        undefined
      );
      newObjective.parentUnitId = this.formData.unitId;
      this.dialogRef.close(this.objectiveMapper.postObjectiveForUnit$(this.formData.unitId, newObjective));
    }
  }

  fetchParentObjectives(departmentId: number): void {
    this.currentOkrUnitSchemaService.getUnitSchemasToReachUnitWithId$(departmentId)
      .pipe(
        switchMap((departmentList: OkrUnitSchema[]) => {
          return this.getDepartmentObjectiveUnitsForDepartments$(departmentList);
        })
      )
      .subscribe((departmentObjectiveSchemas: DepartmentObjectiveSchema[]) => {
        this.parentElements$.next(departmentObjectiveSchemas);
      });
  }

  private getDepartmentObjectiveUnitsForDepartments$(departmentList: OkrUnitSchema[]):
    Observable<DepartmentObjectiveSchema[]> {
    return forkJoin(departmentList.map(currentUnit => {
      return this.objectiveMapper.getObjectivesForDepartment$(currentUnit.id)
        .pipe(
          map((objectiveList: ViewObjective[]) => {
            return new DepartmentObjectiveSchema(currentUnit, objectiveList);
          })
        );
    }));
  }
}
