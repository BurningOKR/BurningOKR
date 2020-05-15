// tslint:disable
import { TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, Directive, Input, NO_ERRORS_SCHEMA, Pipe, PipeTransform } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ResetPasswordComponent } from './reset-password.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatSnackBarModule } from '@angular/material';
import { PasswordService } from '../password-service/password.service';
import { PasswordServiceMock } from '../../../../shared/mocks/password-service-mock';

@Directive({selector: '[oneviewPermitted]'})
class OneviewPermittedDirective {
  @Input() oneviewPermitted;
}

@Pipe({name: 'translate'})
class TranslatePipe implements PipeTransform {
  transform(value) {
    return value;
  }
}

@Pipe({name: 'phoneNumber'})
class PhoneNumberPipe implements PipeTransform {
  transform(value) {
    return value;
  }
}

@Pipe({name: 'safeHtml'})
class SafeHtmlPipe implements PipeTransform {
  transform(value) {
    return value;
  }
}

describe('ResetPasswordComponent', () => {
  let fixture;
  let component;
  const passwordServiceMock: PasswordServiceMock = new PasswordServiceMock();

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule,
        MatSnackBarModule
      ],
      declarations: [
        ResetPasswordComponent,
        TranslatePipe,
        PhoneNumberPipe,
        SafeHtmlPipe,
        OneviewPermittedDirective
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        FormBuilder,
        {provide: PasswordService, useValue: passwordServiceMock},
      ]
    }).overrideComponent(ResetPasswordComponent, {}).compileComponents();
    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.debugElement.componentInstance;
  });

  afterEach(() => {
    component.ngOnDestroy = function() {
    };
    fixture.destroy();
  });

  it('should run #constructor()', async () => {
    expect(component).toBeTruthy();
  });

  it('should run GetterDeclaration #f', async () => {
    component.resetForm = component.resetForm || {};
    component.resetForm.controls = 'controls';
    const f = component.f;

  });

  it('should run #sendResetPasswordMail()', async () => {

    component.sendResetPasswordMail();

  });

  it('should run #generateLoginForm()', async () => {
    component.formBuilder = component.formBuilder || {};
    component.formBuilder.group = jest.fn();
    component.generateLoginForm();
    expect(component.formBuilder.group).toHaveBeenCalled();
  });

  // new test by ngentest
  it('should run GetterDeclaration #f', async () => {
    component.resetForm = component.resetForm || {};
    component.resetForm.controls = 'controls';
    const f = component.f;

  });
});
