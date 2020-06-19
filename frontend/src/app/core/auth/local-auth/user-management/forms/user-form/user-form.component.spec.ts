import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFormComponent } from './user-form.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { emailAlreadyInUseValidatorFunction } from '../email-already-in-use-validator-function';

describe('UserFormComponent', () => {
  const formBuilder: FormBuilder = new FormBuilder();
  const userEmails: string[] = [];

  let component: UserFormComponent;
  let fixture: ComponentFixture<UserFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ UserFormComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
      ],
    })
    .compileComponents();
    fixture = TestBed.createComponent(UserFormComponent);
    component = fixture.componentInstance;
    component.userForm = formBuilder.group({
      id: [''],
      givenName: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, emailAlreadyInUseValidatorFunction(userEmails)]],
      jobTitle: [''],
      department: [''],
      photo: [''],
      isAdmin: [false],
      active: [true]
    });
    fixture.detectChanges();

  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
