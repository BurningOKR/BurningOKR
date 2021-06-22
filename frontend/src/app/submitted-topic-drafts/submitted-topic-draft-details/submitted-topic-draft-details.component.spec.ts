import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftDetailsComponent } from './submitted-topic-draft-details.component';
import { StatusDotComponent } from '../../shared/components/status-dot/status-dot.component';
import { UserMinibuttonComponent } from '../../shared/components/user-minibutton/user-minibutton.component';
import { UserAvatarComponent } from '../../shared/components/user-avatar/user-avatar.component';
import { AvatarComponent } from 'ngx-avatar';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { OAuthService } from 'angular-oauth2-oidc';
import {HttpClient} from "@angular/common/http";
import {AuthenticationService} from "../../core/auth/services/authentication.service";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {NGXLogger} from "ngx-logger";
import {Router} from "@angular/router";

describe('SubmittedTopicDraftDetailsComponent', () => {
  let component: SubmittedTopicDraftDetailsComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftDetailsComponent,
                      StatusDotComponent,
                      UserMinibuttonComponent,
                      AvatarComponent,
                      //MatDialogRef,
                      UserAvatarComponent],
      imports: [ MaterialTestingModule,
                 FormsModule,
                 ReactiveFormsModule],
      providers: [
        {provide: MatDialogRef, useValue: {}},
        {provide: OAuthService, useValue: {}},
        {provide: HttpClient, useValue: {}},
        {provide: AuthenticationService, useValue: {}},
        {provide: I18n, useValue: {}},
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
