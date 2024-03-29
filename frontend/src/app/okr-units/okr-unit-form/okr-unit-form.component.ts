import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { DialogComponent } from '../../shared/components/dialog-component/dialog.component';
import { CompanyDto } from '../../shared/model/api/OkrUnit/company.dto';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { ValidationErrorService } from '../../shared/services/helper/validation-error.service';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';

interface CompanyFormData {
  company?: CompanyUnit;
}

@Component({
  selector: 'app-okr-unit-form',
  templateUrl: './okr-unit-form.component.html',
  styleUrls: ['./okr-unit-form.component.scss'],
})

export class OkrUnitFormComponent {
  companyForm: FormGroup;
  title: string;
  structureTranslation: string;

  constructor(
    private dialogRef: MatDialogRef<DialogComponent<CompanyFormData>>,
    private companyMapper: CompanyMapper,
    @Inject(MAT_DIALOG_DATA) private formData: CompanyFormData,
    private x: ValidationErrorService,
    private translate: TranslateService,
  ) {
    this.companyForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      label: new FormControl(
        this.getDefaultLabel(),
        [Validators.required, Validators.minLength(1), Validators.maxLength(255)],
      ),
    });

    if (this.formData.company) {
      this.companyForm.patchValue(this.formData.company);
    }

    this.structureTranslation = this.translate.instant('okr-unit-form.structure');

    const saveTranslation: string = translate.instant('okr-unit-form.save', { value: this.getDefaultLabel() });
    const createTranslation: string = translate.instant('okr-unit-form.create', { value: this.getDefaultLabel() });

    this.title = this.formData.company ? saveTranslation : createTranslation;
  }

  saveCompany(): void {
    const company: CompanyUnit = this.formData.company;

    if (company) {
      company.name = this.companyForm.get('name').value;
      company.label = this.companyForm.get('label').value;
      this.dialogRef.close(this.companyMapper.putCompany$(company));
    } else {
      const formData: CompanyUnit = this.companyForm.getRawValue();
      const newCompany: CompanyDto = { unitName: formData.name, cycleId: formData.cycleId, label: formData.label };
      this.dialogRef.close(this.companyMapper.postCompany$(newCompany));
    }
  }

  private getDefaultLabel(): string {
    if (this.formData.company) {
      return this.formData.company.label;
    } else {
      return this.structureTranslation;
    }
  }
}
