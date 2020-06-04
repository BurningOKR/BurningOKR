import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { User } from '../../../shared/model/api/user';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { forkJoin, NEVER, Observable, Subject, Subscription } from 'rxjs';
import { ObjectiveViewMapper } from '../../../shared/services/mapper/objective-view.mapper';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { ControlHelperService } from '../../../shared/services/helper/control-helper.service';
import { ViewObjective } from '../../../shared/model/ui/view-objective';
import { DepartmentStructure } from '../../../shared/model/ui/department-structure';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { CompanyStructure } from '../../../shared/model/ui/OrganizationalUnit/company-structure';
import { CurrentDepartmentStructureService } from '../../current-department-structure.service';
import { map, switchMap } from 'rxjs/operators';

interface ObjectiveFormData {
  objective?: ViewObjective;
  departmentId?: number;
  currentItem: CompanyStructure;
}

class DepartmentObjectiveStructure {
  department: DepartmentStructure;
  objectiveList: ViewObjective[];

  constructor(department: DepartmentStructure, objectiveList: ViewObjective[]) {
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
  parentElements$ = new Subject<DepartmentObjectiveStructure[]>();
  users: User[];
  user: User;
  getErrorMessage = this.controlHelperService.getErrorMessage;

  subscriptions: Subscription[] = [];
  title: string;

  constructor(
    private dialogRef: MatDialogRef<ObjectiveFormComponent>,
    private objectiveMapper: ObjectiveViewMapper,
    private currentOkrViewService: CurrentOkrviewService,
    private currentDepartmentStructureService: CurrentDepartmentStructureService,
    private i18n: I18n,
    private controlHelperService: ControlHelperService,
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
      this.fetchParentObjectives(this.objective.parentStructureId);
    } else {
      this.fetchParentObjectives(this.formData.departmentId);
    }

    const editText: string = this.i18n({
      id: 'edit',
      value: 'bearbeiten'
    });

    const createText: string = this.i18n({
      id: 'create',
      value: 'erstellen'
    });

    this.title = `Objective ${this.objective ? editText : createText}`;
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
        formData.parentStructureId,
        formData.contactPersonId,
        undefined
      );
      newObjective.parentStructureId = this.formData.departmentId;
      this.dialogRef.close(this.objectiveMapper.postObjectiveForDepartment$(this.formData.departmentId, newObjective));
    }
  }

  fetchParentObjectives(departmentId: number): void {
    this.currentDepartmentStructureService.getDepartmentStructuresToReachDepartmentWithId$(departmentId)
      .pipe(
        switchMap((departmentList: DepartmentStructure[]) => {
          return this.getDepartmentObjectiveStructuresForDepartments$(departmentList);
        })
      )
      .subscribe((departmentObjectiveStructures: DepartmentObjectiveStructure[]) => {
        this.parentElements$.next(departmentObjectiveStructures);
      });
  }

  private getDepartmentObjectiveStructuresForDepartments$(departmentList: DepartmentStructure[]):
    Observable<DepartmentObjectiveStructure[]> {
    return forkJoin(departmentList.map(currentStructure => {
      return this.objectiveMapper.getObjectivesForDepartment$(currentStructure.id)
        .pipe(
          map((objectiveList: ViewObjective[]) => {
            return new DepartmentObjectiveStructure(currentStructure, objectiveList);
          })
        );
    }));
  }
}
