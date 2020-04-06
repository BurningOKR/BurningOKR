import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthErrors } from '../../auth-errors';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  errorType: AuthErrors;
  authErrors = AuthErrors;
  capsLock: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.loginForm = this.generateLoginForm();
  }

  login(): void {
    this.authenticationService.loginLocalUser(this.loginForm.get('email').value, this.loginForm.get('password').value)
      .then(() => {
        this.router.navigate(['/']);
      })
      .catch((error: HttpErrorResponse) => {
        this.errorType = error.error.error;
      });
  }

  private generateLoginForm(): FormGroup {
    return this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }
}
