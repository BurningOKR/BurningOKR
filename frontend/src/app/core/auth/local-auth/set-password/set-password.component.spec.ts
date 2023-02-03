import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SetPasswordComponent } from './set-password.component';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { PasswordService } from '../password-service/password.service';
import { PasswordServiceMock } from '../../../../shared/mocks/password-service-mock';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('SetPasswordComponent', () => {
  let component: SetPasswordComponent;
  let fixture: ComponentFixture<SetPasswordComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [SetPasswordComponent],
      imports: [
        MatCardModule,
        HttpClientTestingModule,
        RouterTestingModule,
        MaterialTestingModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: PasswordService, useValue: PasswordServiceMock },
        { provide: MatSnackBar, useValue: {} },
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
