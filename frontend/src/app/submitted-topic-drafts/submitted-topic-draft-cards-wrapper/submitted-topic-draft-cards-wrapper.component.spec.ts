import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SubmittedTopicDraftCardsWrapperComponent } from './submitted-topic-draft-cards-wrapper.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MaterialTestingModule } from "../../testing/material-testing.module";

describe('SubmittedTopicDraftCardsWrapperComponent', () => {
  let component: SubmittedTopicDraftCardsWrapperComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftCardsWrapperComponent>;
  const topicDraftMock: any = jest.fn();

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      imports: [ MaterialTestingModule ],
      declarations: [ SubmittedTopicDraftCardsWrapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftCardsWrapperComponent);
    component = fixture.componentInstance;
    component.topicDrafts = topicDraftMock;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
        .toBeTruthy();
  });
});
