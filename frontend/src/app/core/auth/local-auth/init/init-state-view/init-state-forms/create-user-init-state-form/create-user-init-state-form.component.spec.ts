import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUserInitStateFormComponent } from './create-user-init-state-form.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { InitService } from '../../../../../../services/init.service';
import { InitServiceMock } from '../../../../../../../shared/mocks/init-service-mock';

describe('CreateUserInitStateFormComponent', () => {
  const formBuilder: FormBuilder = new FormBuilder();
  const initServiceMock: InitServiceMock = new InitServiceMock();

  let component: CreateUserInitStateFormComponent;
  let fixture: ComponentFixture<CreateUserInitStateFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateUserInitStateFormComponent ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
        {provide: FormBuilder, useValue: formBuilder},
        {provide: InitService, useValue: initServiceMock},
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUserInitStateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
