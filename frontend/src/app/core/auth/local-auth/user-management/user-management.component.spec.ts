import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserManagementComponent } from './user-management.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog, MatTableModule } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { LocalUserApiService } from '../../../../shared/services/api/local-user-api.service';
import { LocalUserApiServiceMock } from '../../../../shared/mocks/local-user-api-service-mock';

describe('UserManagementComponent', () => {
  const localUserApiServiceMock: LocalUserApiServiceMock =  new LocalUserApiServiceMock();

  let component: UserManagementComponent;
  let fixture: ComponentFixture<UserManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [UserManagementComponent],
      imports: [
        MatTableModule,
        RouterTestingModule,
        HttpClientTestingModule,
      ],
      providers: [
        {provide: MatDialog, useValue: {}},
        {provide: LocalUserApiService, useValue: localUserApiServiceMock},
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(UserManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
