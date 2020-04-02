// tslint:disable
import { TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, Directive, Input, NO_ERRORS_SCHEMA, Pipe, PipeTransform } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ResetPasswordComponent } from './reset-password.component';

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

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule],
      declarations: [
        ResetPasswordComponent,
        TranslatePipe, PhoneNumberPipe, SafeHtmlPipe,
        OneviewPermittedDirective
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        FormBuilder
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

  it('should run #resetPassword()', async () => {

    component.resetPassword({});

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
