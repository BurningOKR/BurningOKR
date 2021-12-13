import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftsConvertToTeamComponent } from './submitted-topic-drafts-convert-to-team.component';

describe('SubmittedTopicDraftsConvertToTeamComponent', () => {
  let component: SubmittedTopicDraftsConvertToTeamComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftsConvertToTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftsConvertToTeamComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftsConvertToTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
