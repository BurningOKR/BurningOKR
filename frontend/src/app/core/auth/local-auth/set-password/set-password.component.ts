import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PasswordResetData, PasswordService } from '../password-service/password.service';
import { passwordMatchValidatorFunction } from './passwords-match-validator-function';
import { take } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';

@Component({
  selector: 'app-set-password',
  templateUrl: './set-password.component.html',
  styleUrls: ['./set-password.component.css']
})
export class SetPasswordComponent implements OnInit {

  emailIdentifier: string;
  newPasswordForm: FormGroup;
  private passwordSuccessfullyChangedMessage: string = this.i18n({
    id: 'passwordChangedSuccessfully',
    description: 'message to be shown after a password was changed successfully',
    value: 'Passwort erfolgreich geändert ✅'
  });
  submitted: boolean = false;

  constructor(
    private passwordService: PasswordService,
    private router: Router,
    private route: ActivatedRoute,
    private matSnackBar: MatSnackBar,
    private i18n: I18n
  ) { }

  ngOnInit(): void {
    this.emailIdentifier = this.route.snapshot.paramMap.get('emailIdentifier');
    this.newPasswordForm = this.generateNewPasswordForm();
  }

  setPassword(): void {
    const formData: PasswordResetData = this.getPasswordResetData();
    this.disableForm();

    this.passwordService.setPasswordWithEmailIdentifier$(formData)
      .pipe(take(1))
      .subscribe(response => {
        this.displaySuccessSnackBarAndRedirectToLogin();
      }, () => this.enableForm());
  }

  generateNewPasswordForm(): FormGroup {
    return new FormGroup({
      newPassword: new FormControl('', [Validators.required, Validators.minLength(7)]),
      newPasswordRepetition: new FormControl('', [Validators.required])
    }, [passwordMatchValidatorFunction]);
  }

  private displaySuccessSnackBarAndRedirectToLogin(): void {
    this.router.navigate(['/auth/login']);
    setTimeout(() => {    this.matSnackBar.open(this.passwordSuccessfullyChangedMessage, undefined, {
      verticalPosition: 'top',
      duration: 3500
    }); }, 100);  // setTimeout is needed to navigate in non chromium based browsers /TG 09.03.2020
  }

  private redirectToResetPasswordComponent(): void {
    return; // TODO: A user should be routed to the reset pasword form, when the identifier is unvalid
  }

  private getPasswordResetData(): PasswordResetData {
    return {
      emailIdentifier: this.emailIdentifier,
      password: this.newPasswordForm.get('newPassword').value
    };
  }

  private disableForm(): void {
    this.newPasswordForm.disable();
    this.submitted = true;
  }

  private enableForm(): void {
    this.newPasswordForm.enable();
    this.submitted = false;
  }
}
