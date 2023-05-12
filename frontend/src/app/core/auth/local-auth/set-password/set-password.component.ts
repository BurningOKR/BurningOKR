import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { take } from 'rxjs/operators';
import { PasswordsMatchValidator } from '../../../../shared/validators/password-match-validator/passwords-match-validator-function';
import { PasswordResetData, PasswordService } from '../password-service/password.service';

@Component({
  selector: 'app-set-password',
  templateUrl: './set-password.component.html',
  styleUrls: ['./set-password.component.scss'],
})
export class SetPasswordComponent implements OnInit {

  emailIdentifier: string;
  newPasswordForm: FormGroup;
  submitted: boolean = false;
  private passwordSuccessfullyChangedMessage: string;

  constructor(
    private passwordService: PasswordService,
    private router: Router,
    private route: ActivatedRoute,
    private matSnackBar: MatSnackBar,
    private translate: TranslateService,
  ) {
  }

  ngOnInit(): void {
    this.emailIdentifier = this.route.snapshot.paramMap.get('emailIdentifier');
    this.newPasswordForm = this.generateNewPasswordForm();
    this.passwordSuccessfullyChangedMessage = this.translate.instant('set-password.password-changed-message');
  }

  setPassword(): void {
    const formData: PasswordResetData = this.getPasswordResetData();
    this.disableForm();

    this.passwordService.setPasswordWithEmailIdentifier$(formData)
      .pipe(take(1))
      .subscribe(() => {
        this.displaySuccessSnackBarAndRedirectToLogin();
      }, () => this.enableForm());
  }

  generateNewPasswordForm(): FormGroup {
    return new FormGroup({
      newPassword: new FormControl('', [Validators.required, Validators.minLength(7)]),
      newPasswordRepetition: new FormControl('', [Validators.required]),
    }, [PasswordsMatchValidator.Validate]);
  }

  private displaySuccessSnackBarAndRedirectToLogin(): void {
    this.router.navigate(['/auth/login']);
    setTimeout(() => {
      this.matSnackBar.open(this.passwordSuccessfullyChangedMessage, undefined, {
        verticalPosition: 'top',
        duration: 3500,
      });
    }, 100);  // setTimeout is needed to navigate in non chromium based browsers /TG 09.03.2020
  }

  private getPasswordResetData(): PasswordResetData {
    return {
      emailIdentifier: this.emailIdentifier,
      password: this.newPasswordForm.get('newPassword').value,
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
