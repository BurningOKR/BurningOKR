import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftActionButtonComponent } from './submitted-topic-draft-action-button.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '../../core/auth/services/authentication.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { NGXLogger } from 'ngx-logger';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { User } from '../../shared/model/api/user';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { CurrentUserService } from '../../core/services/current-user.service';
import { of } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';

class CurrentUserServiceMock {

  isCurrentUserAdmin$(): Observable<boolean> {
    return of(true);
  }

  isCurrentUserAdminOrCreator$(): Observable<boolean> {
    return of(true);
  }

  isCurrentUserAdminOrAuditor$(): Observable<boolean> {
    return of(true);
  }
}

describe('SubmittedTopicDraftActionButtonComponent', () => {
  let component: SubmittedTopicDraftActionButtonComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftActionButtonComponent>;

  let currentUser: User;
  let topicDraft: OkrTopicDraft;

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
        { provide: Router, useValue: routerMock },
        { provide: OAuthService, useValue: {} },
        { provide: CurrentUserService, useClass: CurrentUserServiceMock }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    currentUser = new User('1', '', '', '', '', '', '', true);
    topicDraft = new OkrTopicDraft(-1, status.submitted, currentUser, 1, '', '1', [], [], '', '', '', new Date(), '', '', '');

    fixture = TestBed.createComponent(SubmittedTopicDraftActionButtonComponent);
    component = fixture.componentInstance;
    component.topicDraft = topicDraft;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
        .toBeTruthy();
  });
});
