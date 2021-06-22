import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftActionButtonComponent } from './submitted-topic-draft-action-button.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { MatDialog } from '@angular/material';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '../../core/auth/services/authentication.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { NGXLogger } from 'ngx-logger';
import { Router } from '@angular/router';

describe('SubmittedTopicDraftActionButtonComponent', () => {
  let component: SubmittedTopicDraftActionButtonComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftActionButtonComponent>;

  const i18nMock: any = jest.fn();
  const routerMock: any = jest.fn();

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftActionButtonComponent ],
      imports: [ MaterialTestingModule ],
      providers: [
        { provide: MatDialog, useValue: {} },
        { provide: HttpClient, useValue: {} },
        { provide: AuthenticationService, useValue: {} },
        { provide: I18n, useValue: i18nMock },
        { provide: NGXLogger, useValue: {} },
        { provide: Router, useValue: routerMock }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftActionButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
        .toBeTruthy();
  });
});
