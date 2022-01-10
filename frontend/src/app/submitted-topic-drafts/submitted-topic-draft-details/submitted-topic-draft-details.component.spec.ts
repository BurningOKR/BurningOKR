import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import {
  SubmittedTopicDraftDetailsComponent,
  SubmittedTopicDraftDetailsFormData
} from './submitted-topic-draft-details.component';
import { StatusDotComponent } from '../../shared/components/status-dot/status-dot.component';
import { UserAvatarComponent } from '../../shared/components/user-avatar/user-avatar.component';
import { AvatarComponent } from 'ngx-avatar';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '../../core/auth/services/authentication.service';
import { NGXLogger } from 'ngx-logger';
import { Router } from '@angular/router';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { User } from '../../shared/model/api/user';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Component, Input } from '@angular/core';

describe('SubmittedTopicDraftDetailsComponent', () => {
  let component: SubmittedTopicDraftDetailsComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftDetailsComponent>;
  const topicDraftMock: OkrTopicDraft = new OkrTopicDraft(
    0,
    status.approved,
    new User('1', 'Max', 'Mustermann', 'mmustermann@email.com', 'Consultant', 'IT', ''),
    0,
    'fghs',
    '',
    [],
    [],
    '',
    '',
    '',
    new Date(),
    '',
    '',
    '',
  );
  const formDataMock: SubmittedTopicDraftDetailsFormData = {
    topicDraft: topicDraftMock
  };

  @Component({
    selector: 'app-user-minibutton',
    template: ''
  })
  class UserMiniButtonMockComponent {
    @Input() userId: string;
    @Input() canBeRemoved = false;
  }

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftDetailsComponent,
                      StatusDotComponent,
                      AvatarComponent,
                      UserAvatarComponent,
                      UserMiniButtonMockComponent ],
      imports: [  MaterialTestingModule,
                  FormsModule,
                  MatDialogModule,
                  ReactiveFormsModule,
                  NoopAnimationsModule],
      providers: [
        {provide: MatDialog, useValue: {}},
        {provide: MatDialogRef, useValue: {}},
        {provide: MAT_DIALOG_DATA, useValue: formDataMock},
        {provide: OAuthService, useValue: {}},
        {provide: HttpClient, useValue: {}},
        {provide: AuthenticationService, useValue: {}},
        {provide: NGXLogger, useValue: {}},
        {provide: Router, useValue: {}}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
