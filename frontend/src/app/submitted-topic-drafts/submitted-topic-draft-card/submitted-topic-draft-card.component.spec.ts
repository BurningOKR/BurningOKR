import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftCardComponent } from './submitted-topic-draft-card.component';

describe('SubmittedTopicDraftCardComponent', () => {
  let component: SubmittedTopicDraftCardComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
