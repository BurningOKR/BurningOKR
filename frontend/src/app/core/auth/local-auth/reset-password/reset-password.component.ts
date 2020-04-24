import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PasswordResetMailData, PasswordService } from '../password-service/password.service';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {

  passwordResetForm: FormGroup;
  emailHasBeenSentOut: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private passwordService: PasswordService
  ) {
    this.passwordResetForm = this.generateLoginForm();
  }

  sendResetPasswordMail(): void {
    const data: PasswordResetMailData = {email: this.passwordResetForm.get('email').value};

    this.passwordService.sendPasswordResetEmail$(data)
      .pipe(take(1))
      .subscribe(
        () => {
        this.emailHasBeenSentOut = true;
      });
  }

  private generateLoginForm(): FormGroup {
    return this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

}
