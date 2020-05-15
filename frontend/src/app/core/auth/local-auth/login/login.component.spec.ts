import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { OkrToolbarBareComponent } from '../../../../shared/components/okr-toolbar-bare/okr-toolbar-bare.component';
import { AuthenticationService } from '../../services/authentication.service';
import { AuthenticationServiceMock } from '../../../../shared/mocks/authentication-service-mock';
import { RouterTestingModule } from '@angular/router/testing';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
      ],
      declarations: [LoginComponent, OkrToolbarBareComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        FormBuilder,
        {provide: AuthenticationService, useValue: AuthenticationServiceMock}
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should run #generateLoginForm()', () => {
    expect(component.loginForm)
      .not
      .toBeNull();
  });
});
