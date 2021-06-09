import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftCardsWrapperComponent } from './submitted-topic-draft-cards-wrapper.component';

describe('SubmittedTopicDraftCardsWrapperComponent', () => {
  let component: SubmittedTopicDraftCardsWrapperComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftCardsWrapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubmittedTopicDraftCardsWrapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftCardsWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
