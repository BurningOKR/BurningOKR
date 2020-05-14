import { Component, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FormGroupTyped } from '../../../../../../../../typings';
import { NewPasswordForm } from '../../../../../../../shared/model/forms/new-password-form';
import { AdminUserForm } from '../../../../../../../shared/model/forms/admin-user-form';
import { passwordMatchValidatorFunction } from '../../../../set-password/passwords-match-validator-function';
import { InitStateFormComponent } from '../init-state-form/init-state-form.component';
import { InitService } from '../../../../../../services/init.service';
import { PostLocalAdminUserData } from '../../../../../../../shared/model/api/post-local-admin-user-data';
import { User } from '../../../../../../../shared/model/api/user';
import { InitState } from '../../../../../../../shared/model/api/init-state';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-create-user-state-form',
  templateUrl: './create-user-init-state-form.component.html',
  styleUrls: ['./create-user-init-state-form.component.css']
})
export class CreateUserInitStateFormComponent extends InitStateFormComponent implements OnInit {
  adminUserForm: FormGroupTyped<AdminUserForm>;
  newPasswordForm: FormGroupTyped<NewPasswordForm>;
  form: FormGroup;
  eventEmitter: EventEmitter<InitState> = new EventEmitter<InitState>();

  constructor(
    private formBuilder: FormBuilder,
    private initService: InitService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.generateAdminUserForm();
    this.generateNewPasswordForm();
    this.createForm();
  }

  generateAdminUserForm(): void {
    this.adminUserForm = this.formBuilder.group({
      givenName: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      jobTitle: [''],
      department: [''],
      photo: [''],
      isAdmin: [{value: true, disabled: true}],
      active: [{value: true, disabled: true}]
    }) as FormGroupTyped<AdminUserForm>;
  }

  generateNewPasswordForm(): void {
    const createdNewPasswordForm: FormGroup = this.formBuilder.group({
      previousPassword: [''],
      newPassword: ['', [Validators.required, Validators.minLength(7)]],
      newPasswordRepetition: ['', [Validators.required]]
    });
    createdNewPasswordForm.setValidators(passwordMatchValidatorFunction);
    this.newPasswordForm = createdNewPasswordForm as FormGroupTyped<NewPasswordForm>;
  }

  private handleSubmitClick(): void {
    this.form.disable();
    this.submitData$()
      .subscribe(initState => {
        this.form.enable();
        this.eventEmitter.emit(initState);
      }, error => {
        this.form.enable();
      });
  }

  private createForm(): void {
    this.form = this.formBuilder.group({
      adminUserForm: this.adminUserForm,
      newPasswordForm: this.newPasswordForm
    });
  }

  submitData$(): Observable<InitState> {
    return this.initService.postLocalAdminUser$(this.getFormData());
  }

  private getFormData(): PostLocalAdminUserData {
    return {
      password: this.getPasswordFromForm(),
      userDto: this.getAdminUserFromForm()
    };
  }

  private getPasswordFromForm(): string {
    return this.newPasswordForm.get('newPassword').value;
  }

  private getAdminUserFromForm(): User {
    return new User(
      '',
      this.adminUserForm.get('givenName').value,
      this.adminUserForm.get('surname').value,
      this.adminUserForm.get('email').value,
      this.adminUserForm.get('jobTitle').value,
      this.adminUserForm.get('department').value,
      this.adminUserForm.get('photo').value
      );
  }
}
