import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SubmittedTopicDraftCardComponent } from './submitted-topic-draft-card.component';
import { Component, Input } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { User } from '../../shared/model/api/user';
import { StatusDotComponent } from '../../shared/components/status-dot/status-dot.component';
import { MatDialog } from '@angular/material/dialog';
import { i18nMock } from '../../shared/mocks/i18n-mock';
import { I18n } from '@ngx-translate/i18n-polyfill';

describe('SubmittedTopicDraftCardComponent', () => {
  let component: SubmittedTopicDraftCardComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftCardComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftCardComponent, SubmittedTopicDraftActionButtonMock, StatusDotComponent ],
      imports: [ MaterialTestingModule ],
      providers: [
        { provide: MatDialog, useValue: {} },
        { provide: I18n, useValue: i18nMock}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftCardComponent);
    component = fixture.componentInstance;
    component.topicDraft = new OkrTopicDraft
    (1, status.submitted, new User('1', 'T', 'T'), 0, '', '', null, null, '', '', '', new Date(), '', '', '');
    fixture.detectChanges();
  });

  @Component({
    // tslint:disable-next-line:component-selector
    selector: 'app-submitted-topic-draft-action-button',
    template: ''
  })
  class SubmittedTopicDraftActionButtonMock {
    @Input() topicDraft: OkrTopicDraft;
  }

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
