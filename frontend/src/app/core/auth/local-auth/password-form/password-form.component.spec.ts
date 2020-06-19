import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordFormComponent } from './password-form.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

describe('PasswordFormComponent', () => {
  const formBuilder: FormBuilder = new FormBuilder();

  let component: PasswordFormComponent;
  let fixture: ComponentFixture<PasswordFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ PasswordFormComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordFormComponent);
    component = fixture.componentInstance;
    component.newPasswordForm = formBuilder.group({
      previousPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(7)]],
      newPasswordRepetition: ['', [Validators.required]]
    });
    component.previousPasswordNecessary = true;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
