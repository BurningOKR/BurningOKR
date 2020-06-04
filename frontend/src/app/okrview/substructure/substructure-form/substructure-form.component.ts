import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NEVER } from 'rxjs';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { DepartmentDto } from '../../../shared/model/api/structure/department.dto';
import { ControlHelperService } from '../../../shared/services/helper/control-helper.service';
import { InactiveTeamService } from './inactive-team.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { StructureType } from '../../../shared/model/api/structure/structure-type.enum';
import { SubStructure } from '../../../shared/model/ui/OrganizationalUnit/sub-structure';

interface SubstructureFormData {
  department?: DepartmentUnit;
  companyId?: number;
  departmentId?: number;
}

@Component({
  selector: 'app-substructure-form',
  templateUrl: './substructure-form.component.html',
  styleUrls: ['./substructure-form.component.scss']
})
export class SubstructureFormComponent {
  department: DepartmentUnit;
  departmentForm: FormGroup;
  title: string;
  getErrorMessage = this.controlHelperService.getErrorMessage;

  constructor(private dialogRef: MatDialogRef<SubstructureFormComponent>,
              private departmentMapper: DepartmentMapper,
              private inactiveTeamService: InactiveTeamService,
              private i18n: I18n,
              private controlHelperService: ControlHelperService,
              @Inject(MAT_DIALOG_DATA) private formData: SubstructureFormData) {
    this.departmentForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      label: new FormControl(this.getDefaultLabel(), [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
      isActive: new FormControl(true)
    });

    if (this.formData.department) {
      this.department = this.formData.department;
      this.departmentForm.patchValue(this.formData.department);
    }

    const editText: string = this.i18n({
      id: 'edit',
      value: 'bearbeiten'
    });

    const createText: string = this.i18n({
      id: 'create',
      value: 'erstellen'
    });

    this.title = `${this.getDefaultLabel()} ${this.department ? editText : createText}`;
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  saveDepartment(): void {
    const department: DepartmentUnit | undefined = this.formData.department;

    if (department) {
      department.name = this.departmentForm.get('name').value;
      department.label = this.departmentForm.get('label').value;
      department.isActive = this.departmentForm.get('isActive').value;

      this.inactiveTeamService.handleTeam(department);

      this.dialogRef.close(this.departmentMapper.putDepartment$(department));
    } else {
      const formData: DepartmentUnit = this.departmentForm.getRawValue();
      const newDepartment: DepartmentDto = {
        structureName: formData.name,
        label: formData.label,
        okrMasterId: undefined,
        okrTopicSponsorId: undefined,
        okrMemberIds: undefined,
        isActive: formData.isActive,
        isParentStructureACorporateObjectiveStructure: false,
        __structureType: StructureType.DEPARTMENT,
        parentStructureId: undefined
      };

      this.inactiveTeamService.handleTeam(formData);

      if (this.formData.companyId) {
        newDepartment.parentStructureId = this.formData.companyId;
        this.dialogRef.close(this.departmentMapper.postDepartmentForCompany$(this.formData.companyId, newDepartment));
      } else if (this.formData.departmentId) {
        newDepartment.parentStructureId = this.formData.departmentId;
        this.dialogRef.close(this.departmentMapper.postDepartmentForDepartment$(this.formData.departmentId, newDepartment));
      }

    }
  }

  private getDefaultLabel(): string {
    if (this.formData.department) {
      return this.formData.department.label;
    } else {
      return this.i18n({id: 'substructure', value: 'Unterstruktur'});
    }
  }
}
