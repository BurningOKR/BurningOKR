import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConvertSubmittedTopicDraftToTeam } from './convert-submitted-topic-draft-to-team.component';

describe('SubmittedTopicDraftsConvertToTeamComponent', () => {
  let component: ConvertSubmittedTopicDraftToTeam;
  let fixture: ComponentFixture<ConvertSubmittedTopicDraftToTeam>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConvertSubmittedTopicDraftToTeam ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConvertSubmittedTopicDraftToTeam);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
