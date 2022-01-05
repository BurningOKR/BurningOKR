import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConvertSubmittedTopicDraftToTeamComponent } from './convert-submitted-topic-draft-to-team.component';

describe('SubmittedTopicDraftsConvertToTeamComponent', () => {
  let component: ConvertSubmittedTopicDraftToTeamComponent;
  let fixture: ComponentFixture<ConvertSubmittedTopicDraftToTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConvertSubmittedTopicDraftToTeamComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConvertSubmittedTopicDraftToTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
