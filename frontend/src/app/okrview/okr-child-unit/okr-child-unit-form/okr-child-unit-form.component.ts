import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NEVER } from 'rxjs';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { ControlHelperService } from '../../../shared/services/helper/control-helper.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { UnitType } from '../../../shared/model/api/OkrUnit/unit-type.enum';
import { OkrChildUnit } from '../../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { OkrUnitMapper } from '../../../shared/services/mapper/okr-unit.mapper.service';
import { OkrBranch } from '../../../shared/model/ui/OrganizationalUnit/okr-branch';
import { OkrBranchMapper } from '../../../shared/services/mapper/okr-branch-mapper.service';

interface OkrChildUnitFormData {
  childUnit?: OkrChildUnit;
  companyId?: number;
  childUnitId?: number;
}

@Component({
  selector: 'app-okr-child-unit-form',
  templateUrl: './okr-child-unit-form.component.html',
  styleUrls: ['./okr-child-unit-form.component.scss']
})
export class OkrChildUnitFormComponent {
  okrChildUnit: OkrChildUnit;
  childUnitForm: FormGroup;
  title: string;
  unitType = UnitType;
  getErrorMessage = this.controlHelperService.getErrorMessage;

  constructor(private dialogRef: MatDialogRef<OkrChildUnitFormComponent>,
              private okrUnitMapper: OkrUnitMapper,
              private departmentMapper: DepartmentMapper,
              private okrBranchMapper: OkrBranchMapper,
              private i18n: I18n,
              private controlHelperService: ControlHelperService,
              @Inject(MAT_DIALOG_DATA) private formData: OkrChildUnitFormData) {
    this.childUnitForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      label: new FormControl(this.getDefaultLabel(), [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
      isActive: new FormControl(true),
      unitType: new FormControl(UnitType.OKR_BRANCH)
    });

    if (this.formData.childUnit) {
      this.okrChildUnit = this.formData.childUnit;
      this.childUnitForm.patchValue(this.formData.childUnit);
    }

    const editText: string = this.i18n({
      id: 'edit',
      value: 'bearbeiten'
    });

    const createText: string = this.i18n({
      id: 'create',
      value: 'erstellen'
    });

    this.title = `${this.getDefaultLabel()} ${this.okrChildUnit ? editText : createText}`;
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  saveDepartment(): void {
    if (this.formData.childUnit) {
      this.updateChildUnit();
    } else {
      this.createChildUnit();
    }
  }

  updateChildUnit(): void {
    const childUnit: OkrChildUnit | undefined = this.formData.childUnit;
    childUnit.name = this.childUnitForm.get('name').value;
    childUnit.label = this.childUnitForm.get('label').value;
    childUnit.isActive = this.childUnitForm.get('isActive').value;

    this.dialogRef.close(this.okrUnitMapper.putOkrChildUnit$(childUnit));
  }

  createChildUnit(): void {
    const formData: OkrChildUnit = this.childUnitForm.getRawValue();
    const okrChildUnit1: OkrChildUnit = {
      id: undefined,
      parentUnitId: undefined,
      objectives: [],
      name: formData.name,
      label: formData.label,
      isActive: formData.isActive,
      isParentUnitABranch: false,
    };

    if (this.childUnitForm.get('unitType').value === UnitType.DEPARTMENT) {
      this.createDepartment(okrChildUnit1 as OkrDepartment);
    } else {
      this.createOkrBranch(okrChildUnit1 as OkrBranch);
    }
  }

  createDepartment(department: OkrDepartment): void {
    if (this.formData.companyId) {
      department.parentUnitId = this.formData.companyId;
      this.dialogRef.close(this.departmentMapper
        .postDepartmentForCompany$(this.formData.companyId, DepartmentMapper.mapDepartmentUnit(department)));

    } else if (this.formData.childUnitId) {
      department.parentUnitId = this.formData.childUnitId;
      this.dialogRef.close(this.departmentMapper
        .postDepartmentForOkrBranch$(this.formData.childUnitId, DepartmentMapper.mapDepartmentUnit(department)));
    }
  }

  createOkrBranch(okrBranch: OkrBranch): void {
    if (this.formData.companyId) {
      okrBranch.parentUnitId = this.formData.companyId;
      this.dialogRef.close(this.okrBranchMapper
        .createForCompany$(this.formData.companyId, okrBranch));
    } else if (this.formData.childUnitId) {
      okrBranch.parentUnitId = this.formData.childUnitId;
      this.dialogRef.close(this.okrBranchMapper
        .createForOkrBranch$(this.formData.childUnitId, okrBranch));
    }
  }

  private getDefaultLabel(): string {
    if (this.formData.childUnit) {
      return this.formData.childUnit.label;
    } else {
      return this.i18n({id: 'OkrBranch', value: 'Unterstruktur'});
    }
  }
}
