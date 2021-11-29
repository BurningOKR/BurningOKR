import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PasswordResetData, PasswordService } from '../password-service/password.service';
import { take } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { PasswordsMatchValidator } from '../../../../shared/validators/password-match-validator/passwords-match-validator-function';

@Component({
  selector: 'app-set-password',
  templateUrl: './set-password.component.html',
  styleUrls: ['./set-password.component.css']
})
export class SetPasswordComponent implements OnInit {

  emailIdentifier: string;
  newPasswordForm: FormGroup;
  submitted: boolean = false;
  private passwordSuccessfullyChangedMessage: string = this.i18n({
    id: 'passwordChangedSuccessfully',
    description: 'message to be shown after a password was changed successfully',
    value: 'Passwort erfolgreich geändert ✅'
  });

  constructor(
    private passwordService: PasswordService,
    private router: Router,
    private route: ActivatedRoute,
    private matSnackBar: MatSnackBar,
    private i18n: I18n
  ) {
  }

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
    }, [PasswordsMatchValidator.Validate]);
  }

  private displaySuccessSnackBarAndRedirectToLogin(): void {
    this.router.navigate(['/auth/login']);
    setTimeout(() => {
      this.matSnackBar.open(this.passwordSuccessfullyChangedMessage, undefined, {
        verticalPosition: 'top',
        duration: 3500
      });
    }, 100);  // setTimeout is needed to navigate in non chromium based browsers /TG 09.03.2020
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
