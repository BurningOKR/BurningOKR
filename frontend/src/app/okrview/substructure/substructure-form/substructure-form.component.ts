import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NEVER } from 'rxjs';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { DepartmentDto } from '../../../shared/model/api/structure/department.dto';
import { ControlHelperService } from '../../../shared/services/helper/control-helper.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { StructureType } from '../../../shared/model/api/structure/structure-type.enum';
import { SubStructure } from '../../../shared/model/ui/OrganizationalUnit/sub-structure';
import { StructureMapper } from '../../../shared/services/mapper/structure.mapper';

interface SubstructureFormData {
  subStructure?: SubStructure;
  companyId?: number;
  subStructureId?: number;
}

@Component({
  selector: 'app-substructure-form',
  templateUrl: './substructure-form.component.html',
  styleUrls: ['./substructure-form.component.scss']
})
export class SubstructureFormComponent {
  subStructure: SubStructure;
  subStructureForm: FormGroup;
  title: string;
  getErrorMessage = this.controlHelperService.getErrorMessage;

  constructor(private dialogRef: MatDialogRef<SubstructureFormComponent>,
              private structureMapper: StructureMapper,
              private departmentMapper: DepartmentMapper,
              private i18n: I18n,
              private controlHelperService: ControlHelperService,
              @Inject(MAT_DIALOG_DATA) private formData: SubstructureFormData) {
    this.subStructureForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      label: new FormControl(this.getDefaultLabel(), [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
      isActive: new FormControl(true)
    });

    if (this.formData.subStructure) {
      this.subStructure = this.formData.subStructure;
      this.subStructureForm.patchValue(this.formData.subStructure);
    }

    const editText: string = this.i18n({
      id: 'edit',
      value: 'bearbeiten'
    });

    const createText: string = this.i18n({
      id: 'create',
      value: 'erstellen'
    });

    this.title = `${this.getDefaultLabel()} ${this.subStructure ? editText : createText}`;
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  // TODO: Save department
  saveDepartment(): void {
    const subStructure: SubStructure | undefined = this.formData.subStructure;

    if (subStructure) {
      subStructure.name = this.subStructureForm.get('name').value;
      subStructure.label = this.subStructureForm.get('label').value;
      subStructure.isActive = this.subStructureForm.get('isActive').value;

      this.dialogRef.close(this.structureMapper.putSubStructure$(subStructure));
    } else {
      const formData: DepartmentUnit = this.subStructureForm.getRawValue();
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

      if (this.formData.companyId) {
        newDepartment.parentStructureId = this.formData.companyId;
        this.dialogRef.close(this.departmentMapper.postDepartmentForCompany$(this.formData.companyId, newDepartment));
      } else if (this.formData.subStructureId) {
        newDepartment.parentStructureId = this.formData.subStructureId;
        this.dialogRef.close(this.departmentMapper.postDepartmentForDepartment$(this.formData.subStructureId, newDepartment));
      }

    }
  }

  private getDefaultLabel(): string {
    if (this.formData.subStructure) {
      return this.formData.subStructure.label;
    } else {
      return this.i18n({id: 'substructure', value: 'Unterstruktur'});
    }
  }
}
