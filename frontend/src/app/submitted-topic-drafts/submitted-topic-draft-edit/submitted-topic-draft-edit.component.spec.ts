import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftEditComponent } from './submitted-topic-draft-edit.component';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { User } from '../../shared/model/api/user';
import { SubmittedTopicDraftDetailsFormData } from '../submitted-topic-draft-details/submitted-topic-draft-details.component';
import { MAT_DIALOG_DATA } from '@angular/material';
import { DialogComponent } from '../../shared/components/dialog-component/dialog.component';
import { OkrTopicDescriptionFormComponent } from '../../okrview/okr-topic-description-form/okr-topic-description-form.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { FormErrorComponent } from '../../shared/components/form-error/form-error.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from '../../core/auth/services/authentication.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { NGXLogger } from 'ngx-logger';
import { Router } from '@angular/router';

describe('SubmittedTopicDraftEditComponent', () => {
  let component: SubmittedTopicDraftEditComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftEditComponent>;
  const i18nMock: any = jest.fn();
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

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        SubmittedTopicDraftEditComponent,
        DialogComponent,
        OkrTopicDescriptionFormComponent,
        FormErrorComponent
      ],
      imports: [
        MaterialTestingModule,
        ReactiveFormsModule,
        MatDialogModule
      ],
      providers: [
        {provide: MatDialog, useValue: {}},
        {provide: MatDialogRef, useValue: {}},
        {provide: MAT_DIALOG_DATA, useValue: formDataMock},
        {provide: OAuthService, useValue: {}},
        {provide: HttpClient, useValue: {}},
        {provide: AuthenticationService, useValue: {}},
        {provide: NGXLogger, useValue: {}},
        {provide: I18n, useValue: i18nMock},
        {provide: Router, useValue: {}}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
