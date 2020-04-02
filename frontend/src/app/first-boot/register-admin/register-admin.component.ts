import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';

@Component({
  selector: 'app-register-admin',
  templateUrl: './register-admin.component.html',
  styleUrls: ['./register-admin.component.css']
})
export class RegisterAdminComponent {

  registerAdminForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder
  ) {
    this.registerAdminForm = this.generateRegisterAdminForm();
    this.registerAdminForm.setValidators(this.comparisonValidator());
  }

  // convenience getter for easy access to form fields
  get f(): { [p: string]: AbstractControl } {
    return this.registerAdminForm.controls;
  }

  comparisonValidator(): ValidatorFn {
    return (group: FormGroup): ValidationErrors => {
      if (group.controls.password1.value !== group.controls.password2.value) {
        group.controls.password2.setErrors({passwordsNotEqual: true});
      }

      if (group.controls.email1.value !== group.controls.email2.value) {
        group.controls.email2.setErrors({emailsNotEqual: true});
      }

      return;
    };
  }

  registerInitialAdmin(formData: any): void {
    return;
  }

  private generateRegisterAdminForm(): FormGroup {
    return this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email1: ['', [Validators.required, Validators.email]],
      email2: ['', [Validators.required, Validators.email]],
      password1: ['', [Validators.required, Validators.minLength(8)]],
      password2: ['', [Validators.required, Validators.minLength(8)]]
    });
  }
}
