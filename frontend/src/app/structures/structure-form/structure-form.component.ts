import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CompanyDto } from '../../shared/model/api/company.dto';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { DialogComponent } from '../../shared/components/dialog-component/dialog.component';
import { ControlHelperService } from '../../shared/services/helper/control-helper.service';
import { I18n } from '@ngx-translate/i18n-polyfill';

interface CompanyFormData {
  company?: CompanyUnit;
}

@Component({
  selector: 'app-structure-form',
  templateUrl: './structure-form.component.html',
  styleUrls: ['./structure-form.component.scss']
})

export class StructureFormComponent {
  companyForm: FormGroup;
  getErrorMessage = this.controlHelperService.getErrorMessage;
  title: string;

  constructor(private dialogRef: MatDialogRef<DialogComponent<CompanyFormData>>,
              private companyMapper: CompanyMapper,
              private i18n: I18n,
              private controlHelperService: ControlHelperService,
              @Inject(MAT_DIALOG_DATA) private formData: CompanyFormData) {
    this.companyForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      label: new FormControl(this.getDefaultLabel(), [Validators.required, Validators.minLength(1),  Validators.maxLength(255)])
    });

    if (this.formData.company) {
      this.companyForm.patchValue(this.formData.company);
    }
    let saveOrCreateText: string;
    if (this.formData.company) {
      saveOrCreateText = this.i18n({
        id: 'component_companyForm_saveText',
        value: 'speichern.'
      });
    } else {
      saveOrCreateText = this.i18n({
        id: 'component_companyForm_createText',
        value: 'erstellen.'
      });
    }

    this.title = `${this.getDefaultLabel()} ${saveOrCreateText}`;
    this.title = this.i18n({
      id: 'component_companyForm_createTitle',
      value: '{{label}} {{saveOrCreateText}}'
    }, {label: this.getDefaultLabel(), saveOrCreateText});
  }

  saveCompany(): void {
    const company: CompanyUnit = this.formData.company;

    if (company) {
      company.name = this.companyForm.get('name').value;
      company.label = this.companyForm.get('label').value;
      this.dialogRef.close(this.companyMapper.putCompany$(company));
    } else {
      const formData: CompanyUnit = this.companyForm.getRawValue();
      const newCompany: CompanyDto = {structureName: formData.name, cycleId: formData.cycleId, label: formData.label};
      this.dialogRef.close(this.companyMapper.postCompany$(newCompany));
    }
  }

  private getDefaultLabel(): string {
    if (this.formData.company) {
      return this.formData.company.label;
    } else {
      return this.i18n({id: 'structure', value: 'Struktur'});
    }
  }
}
